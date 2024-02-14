package com.trip.search.service;

//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trip.search.pojo.ArticleEs;
import com.trip.search.vo.ArticleBriefVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ElasticsearchService {
    void save(Object entity);
    void save(Iterable iterable);

    void delete(String id, Class<?> clazz);

    List<ArticleEs> searchRoute(String keyword);

    List<ArticleEs> searchText(String keyword);

}
