package com.trip.search.controller;

import com.alibaba.fastjson.TypeReference;
import com.trip.common.exception.BusinessException;
import com.trip.common.utils.R;
import com.trip.search.feign.DestinationFeignService;
import com.trip.search.pojo.ArticleEs;
import com.trip.search.service.ElasticsearchService;
import com.trip.search.vo.ArticleBriefVo;
import com.trip.search.vo.ArticleBriefVoToEs;
import com.trip.search.vo.DestinationPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    DestinationFeignService destinationFeignService;

    @Autowired
    ElasticsearchService elasticsearchService;

    @GetMapping("/{keyword}")
    public R search(@PathVariable("keyword") String keyword){
        R destinationFeignResult = destinationFeignService.searchAttraction(keyword);
        if (destinationFeignResult.getCode() != 0) {
            throw new BusinessException(500, "Feign Service Error");
        }
        DestinationPojo attraction = destinationFeignResult.getData(new TypeReference<>() {
        });

        List<ArticleEs> articles;
        if (attraction != null){
            articles = elasticsearchService.searchRoute(keyword);
        }else{
            articles = elasticsearchService.searchText(keyword);
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("attraction", attraction);
        map.put("article", articles);

        return R.ok().put("data", map);
    }

    @PostMapping("/newArticle")
    public R createArticle(@RequestBody ArticleBriefVoToEs article){
        ArticleEs articleEs = new ArticleEs();
        articleEs.setId(article.getId());
        articleEs.setTitle(article.getTitle());
        articleEs.setDay(article.getDay());
        articleEs.setContent_head(article.getContent_head());
        articleEs.setContent_body(article.getContent_body());
        articleEs.setContent_tail(article.getContent_tail());
        articleEs.setRoute(article.getRoute());
        articleEs.setCover_url(article.getCover_url());
        elasticsearchService.save(articleEs);
        return R.ok();
    }
}
