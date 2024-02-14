package com.trip.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.trip.article.pojo.ArticleContentPojo;
import com.trip.article.vo.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ArticleContentService extends IService<ArticleContentPojo> {
    Long createArticle(ArticleVo articleVo);

    Map<String, Object> getArticle(Long article_id);

    List<ArticleContentResponseVo> getArticleByUser(Long user_id);

    List<ArticleContentResponseVo> getMyArticle();

    void incrementViewNum(Long article_id);

    List<ArticleBriefVoToEs> initES(Integer current, Integer limit);

    List<ArticleBriefVoToEs> getArticles(List<Long> list);
}
