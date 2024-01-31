package com.trip.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trip.article.dao.ArticleDayDao;
import com.trip.article.pojo.ArticleDayPojo;
import com.trip.article.service.ArticleDayService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleDayServiceImpl extends ServiceImpl<ArticleDayDao, ArticleDayPojo> implements ArticleDayService {

    @Override
    public List<String> getContentByDay(Long article_id) {
        LambdaQueryWrapper<ArticleDayPojo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleDayPojo::getArticle_id,article_id);
        wrapper.orderByAsc(ArticleDayPojo::getDay_id);
        List<ArticleDayPojo> list = this.list(wrapper);
        ArrayList<String> contentByDay = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            contentByDay.add(list.get(i).getContent());
        }
        return contentByDay;
    }

    @Transactional
    @Override
    public void createArticleDay(Long article_id, Integer day_count, List<String> articleBody) {
        for (int i = 0; i < day_count; i++) {
            ArticleDayPojo articleDayPojo = new ArticleDayPojo();
            articleDayPojo.setArticle_id(article_id);
            articleDayPojo.setDay_id(i);
            articleDayPojo.setDay_count(day_count);
            articleDayPojo.setContent(articleBody.get(i));
            this.save(articleDayPojo);
        }



    }
}
