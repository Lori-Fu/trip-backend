package com.trip.search.controller;

import com.alibaba.fastjson.TypeReference;
import com.trip.common.exception.BusinessException;
import com.trip.common.utils.R;
import com.trip.search.feign.ArticleFeignService;
import com.trip.search.feign.DestinationFeignService;
import com.trip.search.pojo.ArticleEs;
//import com.trip.search.pojo.AttractionEs;
import com.trip.search.service.ElasticsearchService;
import com.trip.search.vo.ArticleBriefVo;
import jakarta.annotation.PostConstruct;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

@RestController
@RequestMapping("/init")
public class InitController {

    @Value("${es.init.key}")
    private String initKey;
    private final Map<String, Function<Integer, R>> DATA_HANDLER_MAP = new HashMap<>();
    private final Map<String, Class<?>> DATA_CLASS_MAP = new HashMap<>();

    private static final Integer BATCH_COUNT = 200;
    private static final String INIT_DESTINATION = "destination";
    private static final String INIT_ARTICLE = "article";


    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Autowired
    private DestinationFeignService destinationFeignService;

    @Autowired
    private ArticleFeignService articleFeignService;
    
    @GetMapping("/{uuid}/{type}")
    public ResponseEntity init(@PathVariable("uuid") String key, @PathVariable("type") String type){
        if (!StringUtils.hasText(key) || !initKey.equals(key)){
            ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Boolean inited = redisTemplate.opsForValue().setIfAbsent("ES_INIT_" + type, "inited");
        if (inited == null || !inited){
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        this.doInit(type);
        return ResponseEntity.ok().body("init success");
    }

    public void doInit(String type){
        List<Object> data = null;
        int current = 1;
        R r = null;
        do {
            data = handleRemoteDataList(current++, type);
            if (data == null || data.size() == 0) {
                return;
            }
            elasticsearchService.save(data);
        } while (true);
    }

    @PostConstruct
    public void postConstruct(){
        DATA_HANDLER_MAP.put("article", current->articleFeignService.initArticleES(current, BATCH_COUNT));
//        DATA_HANDLER_MAP.put("destination", current->destinationFeignService.initAttractionES(current, BATCH_COUNT));
        DATA_CLASS_MAP.put("article", ArticleEs.class);
//        DATA_CLASS_MAP.put("destination", AttractionEs.class);
    }

    public List<Object> handleRemoteDataList(Integer current, String type){
        Function<Integer, R> function = DATA_HANDLER_MAP.get(type);
        if (function == null){
            throw new BusinessException(500, "INITIALIZE TYPE ERROR");
        }
        R r = function.apply(current);
        if (r.getCode() != 0) {
            throw new BusinessException(500, "Feign Service Error");
        }
        List<Object> list = r.getData(new TypeReference<>() {
        });
        if (list == null || list.size() == 0) {
            return null;
        }
        try{
            List<Object> data = new ArrayList<>(list.size());
            Class<?> clazz = DATA_CLASS_MAP.get(type);
            for (Object dto : list) {
                Object es = clazz.getDeclaredConstructor().newInstance();
                BeanUtils.copyProperties(es, dto);
                data.add(es);
            }
            return data;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
