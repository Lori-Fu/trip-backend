package com.trip.article.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trip.article.dao.ArticleContentDao;
import com.trip.article.feign.UserFeignService;
import com.trip.article.pojo.ArticleContentPojo;
import com.trip.article.service.ArticleContentService;
import com.trip.article.service.ArticleDayService;
import com.trip.article.service.ArticleRouteService;
import com.trip.article.vo.*;
import com.trip.article.interceptor.LoginInterceptor;
import com.trip.common.exception.BusinessException;
import com.trip.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ArticleContentServiceImpl extends ServiceImpl<ArticleContentDao, ArticleContentPojo> implements ArticleContentService {
    @Autowired
    ArticleDayService articleDayService;

    @Autowired
    ArticleRouteService articleRouteService;

    @Autowired
    UserFeignService userFeignService;

    @Override
    public ArticleResponseVo getArticle(Long article_id) {
        ArticleResponseVo articleResponseVo = new ArticleResponseVo();
        ArticleContentPojo content = this.getOne(new QueryWrapper<ArticleContentPojo>().eq("id", article_id));
        articleResponseVo.setId(article_id);

        R result = userFeignService.getArticleAuthor(content.getUser_id());
        if (result.getCode() != 0){
            throw new BusinessException(500, "Feign Service Query Failed");
        }
        ArticleAuthor author = result.getData(new TypeReference<ArticleAuthor>() {});
        articleResponseVo.setAuthor(author);

        articleResponseVo.setState(content.getState());
        Integer total_day = content.getDay_count();
        articleResponseVo.setHeader(content.getHeader());
        articleResponseVo.setDay_count(total_day);
        articleResponseVo.setCreate_time(content.getCreate_time());
        articleResponseVo.setContent_head(content.getContent_head());
        articleResponseVo.setContent_tail(content.getContent_tail());

        List<String> contentByDay = articleDayService.getContentByDay(article_id);
        List<List<DestinationInfo>> routeByDay = articleRouteService.getRouteByDay(article_id);
        List<DayContent> dayContents = new ArrayList<>(total_day);
        for (int i = 0; i < total_day; i++) {
            DayContent dayContent = new DayContent();
            dayContent.setContent(contentByDay.get(i));
            dayContent.setItinerary(routeByDay.get(i));
            dayContents.add(dayContent);
        }
        articleResponseVo.setContent_per_day(dayContents);

        articleResponseVo.setVisibility(content.isPost_status());
        return articleResponseVo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long createArticle(ArticleVo articleVo) {
        UserResponseVo userResponseVo = LoginInterceptor.loginUser.get();
        ArticleContentPojo article = new ArticleContentPojo();

        article.setUser_id(userResponseVo.getId());
        article.setState(articleVo.getState());
        article.setDay_count(articleVo.getDay_count());
        article.setHeader(articleVo.getHeader());
        article.setContent_head(articleVo.getContent_head());
        article.setContent_tail(articleVo.getContent_tail());
        article.setCreate_time(new Date());
        article.setPost_status(articleVo.isVisibility());
        this.save(article);
        Long article_id = article.getId();

        articleDayService.createArticleDay(article_id, articleVo.getDay_count(), articleVo.getArticleBody());
        List<List<DestinationInfo>> itinerary = articleVo.getItinerary();
        List<List<Long>> itinerary_ids = new ArrayList<>();
        for (int i = 0; i < itinerary.size(); i++) {
            List<Long> l = new ArrayList<>();
            for (int j = 0; j < itinerary.get(i).size(); j++) {
                System.out.println(itinerary);
                System.out.println(itinerary.get(i).get(j));
                System.out.println(itinerary.get(i).get(j).getId());
                System.out.println (itinerary.get(i).get(j).getId() instanceof Long);
                System.out.println (itinerary.get(i).get(j).getId()  instanceof Object);
                l.add(itinerary.get(i).get(j).getId());
            }
            itinerary_ids.add(l);
        }
        articleRouteService.createArticleRoute(article_id, articleVo.getDay_count(), itinerary_ids);
        return article_id;

    }
}
