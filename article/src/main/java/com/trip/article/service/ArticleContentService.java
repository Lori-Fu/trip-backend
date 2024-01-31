package com.trip.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.trip.article.pojo.ArticleContentPojo;
import com.trip.article.vo.ArticleResponseVo;
import com.trip.article.vo.ArticleVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ArticleContentService extends IService<ArticleContentPojo> {
    Long createArticle(ArticleVo articleVo);

    ArticleResponseVo getArticle(Long article_id);
}
