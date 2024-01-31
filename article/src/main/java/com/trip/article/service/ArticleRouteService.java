package com.trip.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.trip.article.pojo.ArticleRoutePojo;
import com.trip.article.vo.DestinationInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ArticleRouteService extends IService<ArticleRoutePojo> {
    void createArticleRoute(Long article_id, Integer day_count, List<List<Long>> itinerary);
    List<List<DestinationInfo>> getRouteByDay(Long article_id);
}
