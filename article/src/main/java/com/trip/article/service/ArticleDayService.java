package com.trip.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.trip.article.pojo.ArticleDayPojo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ArticleDayService extends IService<ArticleDayPojo> {
    void createArticleDay(Long article_id, Integer day_count, List<String> articleBody);

    List<String> getContentByDay(Long article_id);
}
