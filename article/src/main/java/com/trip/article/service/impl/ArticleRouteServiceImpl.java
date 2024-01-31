package com.trip.article.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trip.article.dao.ArticleRouteDao;
import com.trip.article.feign.DestinationFeignService;
import com.trip.article.pojo.ArticleRoutePojo;
import com.trip.article.service.ArticleRouteService;
import com.trip.article.vo.DestinationInfo;
import com.trip.common.exception.BusinessException;
import com.trip.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleRouteServiceImpl extends ServiceImpl<ArticleRouteDao, ArticleRoutePojo> implements ArticleRouteService {

    @Autowired
    private DestinationFeignService destinationFeignService;

    @Override
    public List<List<DestinationInfo>> getRouteByDay(Long article_id) {
        LambdaQueryWrapper<ArticleRoutePojo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleRoutePojo::getArticle_id,article_id);
        wrapper.orderByAsc(ArticleRoutePojo::getDay_id);
        wrapper.orderByAsc(ArticleRoutePojo::getDest_order);
        List<ArticleRoutePojo> list = this.list(wrapper);
        Integer total_day = list.get(0).getDay_count();
        List<List<DestinationInfo>> groupedByDay = new ArrayList<>();
        for (int i = 0; i < total_day; i++) {
            groupedByDay.add(new ArrayList<>());
        }

        for (int i = 0; i < list.size(); i++) {
            Long destinationId = list.get(i).getDestination_id();
            R result = destinationFeignService.getDestinationForRoute(destinationId);
            if (result.getCode() != 0){
                throw new BusinessException(500, "Feign Service Query Failed");
            }
            Integer day_id = list.get(i).getDay_id();
            DestinationInfo data = result.getData(new TypeReference<DestinationInfo>() {
            });
            groupedByDay.get(day_id).add(data);
        }
        return groupedByDay;
    }

    @Transactional
    @Override
    public void createArticleRoute(Long article_id, Integer day_count, List<List<Long>> itinerary) {
        for (int i = 0; i < day_count; i++) {
            List<Long> itinerary_list = itinerary.get(i);
            for (int j = 0; j < itinerary_list.size(); j++) {
                ArticleRoutePojo articleRoutePojo = new ArticleRoutePojo();
                articleRoutePojo.setArticle_id(article_id);
                articleRoutePojo.setDay_id(i);
                articleRoutePojo.setDay_count(day_count);
                articleRoutePojo.setDest_order(j);
                articleRoutePojo.setDestination_id(itinerary_list.get(j));
                this.save(articleRoutePojo);
            }
        }
    }
}
