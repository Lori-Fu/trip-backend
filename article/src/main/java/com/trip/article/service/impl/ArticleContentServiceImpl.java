package com.trip.article.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.trip.article.dao.ArticleContentDao;
import com.trip.article.feign.CommentFeignService;
import com.trip.article.feign.DestinationFeignService;
import com.trip.article.feign.SearchFeignService;
import com.trip.article.feign.UserFeignService;
import com.trip.article.pojo.ArticleContentPojo;
import com.trip.article.service.ArticleContentService;
import com.trip.article.service.ArticleRankService;
import com.trip.article.utils.AuthenticationUtils;
import com.trip.article.vo.*;
import com.trip.article.interceptor.LoginInterceptor;
import com.trip.common.exception.BusinessException;
import com.trip.common.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

import static com.trip.common.constant.ArticleConstant.*;

@Service
public class ArticleContentServiceImpl extends ServiceImpl<ArticleContentDao, ArticleContentPojo> implements ArticleContentService {

    @Autowired
    ArticleRankService articleRankService;

    @Autowired
    UserFeignService userFeignService;

    @Autowired
    DestinationFeignService destinationFeignService;

    @Autowired
    CommentFeignService commentFeignService;

    @Autowired
    SearchFeignService searchFeignService;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    LoginInterceptor loginInterceptor;

    @Override
    public Map<String, Object> getArticle(Long article_id) {
        //TODO: CompletableFuture
        ArticleContentPojo content = this.getOne(new QueryWrapper<ArticleContentPojo>().eq("id", article_id));
        ArticleContentResponseVo articleContentFromDb = new ArticleContentResponseVo();
        BeanUtils.copyProperties(content, articleContentFromDb);
        R userFeignresult = userFeignService.getUserInfo(content.getUser_id());
        if (userFeignresult.getCode() != 0){
            throw new BusinessException(500, "Feign Service Query Failed");
        }
        UserInfoVo author = userFeignresult.getData(new TypeReference<>() {});
        articleContentFromDb.setUsername(author.getUsername());
        List<List<Long>> routeById = content.getRoute();
        List<List<DestinationInfo>> route = fetchDestinationInfo(routeById);
        articleContentFromDb.setRoute(route);
        ArticleContentResponseVo articleContent = getStatistic(articleContentFromDb);

        UserResponseVo loginUser;
        try {
            loginUser = AuthenticationUtils.getUser();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        boolean isCollect;
        boolean isThumbup;
        isCollect = false;
        isThumbup = false;
        if (loginUser != null) {
            R userStatListResult = userFeignService.getStatList(loginUser.getId());
            if (userStatListResult.getCode() != 0) {
                throw new BusinessException(500, "Feign Service Query Failed");
            }
            Map<String,List<Integer>> userCollectList = userStatListResult.getData(new TypeReference<>() {
            });
            List<Integer> collectList = userCollectList.get("collect_list");
            List<Integer> thumbupList = userCollectList.get("thumbup_list");
            if (collectList.contains(Integer.valueOf(article_id+""))) {
                isCollect = true;
            }
            if (thumbupList.contains(Integer.valueOf(article_id+""))) {
                isThumbup = true;
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("content", articleContent);
        result.put("isCollect", isCollect);
        result.put("isThumbup", isThumbup);
        return  result;
    }


    public List<List<DestinationInfo>> fetchDestinationInfo(List<List<Long>> route){
        R destinationFeignResult = destinationFeignService.getDestinationForRoute(route);
        if (destinationFeignResult.getCode() != 0){
            throw new BusinessException(500, "Feign Service Query Failed");
        }
        List<List<DestinationInfo>> routesInfo = destinationFeignResult.getData(new TypeReference<>() {});
        return routesInfo;
    }

    @Override
    public List<ArticleContentResponseVo> getArticleByUser(Long user_id) {
        // TODO: thread
        LambdaQueryWrapper<ArticleContentPojo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleContentPojo::getUser_id,user_id);
        wrapper.orderByDesc(ArticleContentPojo::getCreate_time);
        List<ArticleContentPojo> articleByUser = this.list(wrapper);
        R userFeignresult = userFeignService.getUserInfo(user_id);
        if (userFeignresult.getCode() != 0){
            throw new BusinessException(500, "Feign Service Query Failed");
        }
        UserInfoVo user = userFeignresult.getData(new TypeReference<>() {
        });

        List<ArticleContentResponseVo> articles = new ArrayList<>();
        for (int i = 0; i < articleByUser.size(); i++) {
            ArticleContentResponseVo vo = new ArticleContentResponseVo();
            BeanUtils.copyProperties(articleByUser.get(i),vo);
            vo.setUsername(user.getUsername());
            System.out.println(vo);
            articles.add(vo);
        }
        return articles;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long createArticle(ArticleVo articleVo) {
        UserResponseVo userResponseVo = LoginInterceptor.loginUser.get();
        ArticleContentPojo article = new ArticleContentPojo();

        article.setUser_id(userResponseVo.getId());
        article.setState(articleVo.getState());
        article.setDay_count(articleVo.getDay_count());
        article.setTitle(articleVo.getTitle());
        article.setContent_head(articleVo.getContent_head());
        article.setContent_body(articleVo.getContent_body());

        List<List<DestinationInfo>> itinerary = articleVo.getItinerary();
        article.setRoute(itinerary.stream().map(routes -> routes.stream()
                .map(DestinationInfo::getId)
                .collect(Collectors.toList()))
                .collect(Collectors.toList()));

        article.setContent_tail(articleVo.getContent_tail());
        article.setCreate_time(new Date());
        article.setPost_status(articleVo.isVisibility());
        article.setCover_url(articleVo.getCover_url());
        article.setThumbup_num(0);
        article.setCollect_num(0);
        article.setReply_num(0);
        article.setView_num(0);
        this.save(article);
        Long article_id = article.getId();

        ArticleBriefVoToEs articleEs = new ArticleBriefVoToEs();
        articleEs.setId(article.getId());
        articleEs.setTitle(article.getTitle());
        articleEs.setDay(article.getDay_count());
        articleEs.setContent_head(article.getContent_head());
        articleEs.setContent_body(article.getContent_body());
        articleEs.setContent_tail(article.getContent_tail());
        List<String> route = flattenRoute(itinerary);
        articleEs.setRoute(route);
        articleEs.setCover_url(article.getCover_url());
        try {
            R articleToES = searchFeignService.createArticle(articleEs);
            System.out.println(articleToES);
            if (articleToES.getCode() != 0) {
                // log
            }

            return article_id;
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<ArticleContentResponseVo> getMyArticle() {
        UserResponseVo userResponseVo = LoginInterceptor.loginUser.get();
        return getArticleByUser(userResponseVo.getId());
    }

    @Override
    public void incrementViewNum(Long article_id) {
        HashOperations ops = redisTemplate.opsForHash();
        Object o = ops.get(REDIS_KEY_ARTICLE_STATISTIC_NUM + article_id, REDIS_KEY_ARTICLE_STATISTIC_VIEW_NUM);
        ops.put(REDIS_KEY_ARTICLE_STATISTIC_NUM + article_id, REDIS_KEY_ARTICLE_STATISTIC_VIEW_NUM, (Integer)o + 1);
    }

    public ArticleContentResponseVo getStatistic(ArticleContentResponseVo articleContentFromDb){
        HashOperations ops = redisTemplate.opsForHash();
        Map map = ops.entries(REDIS_KEY_ARTICLE_STATISTIC_NUM + articleContentFromDb.getId());
        if (map.isEmpty()) {
            ops.put(REDIS_KEY_ARTICLE_STATISTIC_NUM + articleContentFromDb.getId(), REDIS_KEY_ARTICLE_STATISTIC_VIEW_NUM, articleContentFromDb.getView_num());
            ops.put(REDIS_KEY_ARTICLE_STATISTIC_NUM + articleContentFromDb.getId(), REDIS_KEY_ARTICLE_STATISTIC_COLLECT_NUM, articleContentFromDb.getCollect_num());
            ops.put(REDIS_KEY_ARTICLE_STATISTIC_NUM + articleContentFromDb.getId(), REDIS_KEY_ARTICLE_STATISTIC_LIKE_NUM, articleContentFromDb.getThumbup_num());
            ops.put(REDIS_KEY_ARTICLE_STATISTIC_NUM + articleContentFromDb.getId(), REDIS_KEY_ARTICLE_STATISTIC_REPLY_NUM, articleContentFromDb.getReply_num());
        }
        Object view_num = ops.get(REDIS_KEY_ARTICLE_STATISTIC_NUM + articleContentFromDb.getId(), REDIS_KEY_ARTICLE_STATISTIC_VIEW_NUM);
        Object collect_num = ops.get(REDIS_KEY_ARTICLE_STATISTIC_NUM + articleContentFromDb.getId(), REDIS_KEY_ARTICLE_STATISTIC_COLLECT_NUM);
        Object thumbup_num = ops.get(REDIS_KEY_ARTICLE_STATISTIC_NUM + articleContentFromDb.getId(), REDIS_KEY_ARTICLE_STATISTIC_LIKE_NUM);
        Object reply_num = ops.get(REDIS_KEY_ARTICLE_STATISTIC_NUM + articleContentFromDb.getId(), REDIS_KEY_ARTICLE_STATISTIC_REPLY_NUM);

        articleContentFromDb.setView_num((Integer) view_num);
        articleContentFromDb.setCollect_num((Integer)collect_num);
        articleContentFromDb.setThumbup_num((Integer)thumbup_num);
        articleContentFromDb.setReply_num((Integer)reply_num);
        return articleContentFromDb;

    }

    @Override
    public List<ArticleBriefVoToEs> initES(Integer current, Integer limit) {
        int offset = (current - 1) * limit;
        QueryWrapper<ArticleContentPojo> queryWrapper = new QueryWrapper<ArticleContentPojo>().last("limit " + offset + "," + limit);
        List<ArticleContentPojo> list = this.list(queryWrapper);
        List<ArticleBriefVoToEs> result = new ArrayList<>(list.size());
        for (int i = 0; i < list.size(); i++) {
            ArticleContentPojo pojo = list.get(i);
            ArticleBriefVoToEs vo = new ArticleBriefVoToEs();
            vo.setId(pojo.getId());
            vo.setTitle(pojo.getTitle());
            vo.setDay(pojo.getDay_count());
            vo.setContent_head(pojo.getContent_head());
            vo.setContent_tail(pojo.getContent_tail());
            vo.setContent_body(pojo.getContent_body());
            List<List<Long>> routeById = pojo.getRoute();
            List<List<DestinationInfo>> routes = fetchDestinationInfo(routeById);
            List<String> flattenRoute = flattenRoute(routes);
            vo.setRoute(flattenRoute);
            vo.setCover_url(pojo.getCover_url());
            result.add(vo);
        }
        return result;
    }

    public List<String> flattenRoute(List<List<DestinationInfo>> routes){
        List<List<String>> route = routes.stream().map(r -> r.stream()
                        .map(DestinationInfo::getAttraction)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());

        List<String> flattenRoute = new ArrayList<>();
        for (int j = 0; j < route.size(); j++) {
            List<String> route_per_day = route.get(j);
            for (int k = 0; k < route_per_day.size(); k++) {
                String attr = route_per_day.get(k);
                flattenRoute.add(attr);
            }
        }
        return flattenRoute;
    }

    @Override
    public List<ArticleBriefVoToEs> getArticles(List<Long> list) {
        List<ArticleContentPojo> articleCollection = this.listByIds(list);
        List<ArticleBriefVoToEs> articles = new ArrayList<>();
        for (int i = 0; i < articleCollection.size(); i++) {
            ArticleBriefVoToEs vo = new ArticleBriefVoToEs();
            BeanUtils.copyProperties(articleCollection.get(i),vo);
            articles.add(vo);
        }
        return articles;
    }
}
