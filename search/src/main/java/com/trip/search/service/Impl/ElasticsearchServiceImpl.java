package com.trip.search.service.Impl;

//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.SearchTemplateRequest;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.trip.search.pojo.ArticleEs;
import com.trip.search.service.ElasticsearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.client.elc.QueryBuilders;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilterBuilder;
import org.springframework.data.elasticsearch.core.query.SearchTemplateQuery;
import org.springframework.data.elasticsearch.core.query.SearchTemplateQueryBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static co.elastic.clients.elasticsearch._types.query_dsl.Operator.Or;

@Service
public class ElasticsearchServiceImpl implements ElasticsearchService {

    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @Override
    public void save(Object entity) {
        template.save(entity);
    }

    @Override
    public void save(Iterable iterable) {
        template.save(iterable);
    }

    @Override
    public void delete(String id, Class<?> clazz) {
        template.delete(id, clazz);
    }

    @Override
    public List<ArticleEs> searchRoute(String keyword) {
        MatchQuery.Builder match = new MatchQuery.Builder();
        MatchQuery.Builder builder = match.query(keyword).field("route");
        try {
            SearchResponse<ArticleEs> articlesFound = elasticsearchClient.search(s -> s.index("article").query(Query.of(q -> q.match(builder.build()))), ArticleEs.class);
            List<Hit<ArticleEs>> hits = articlesFound.hits().hits();
            List<ArticleEs> articles = new ArrayList<>();
            hits.forEach(hit -> articles.add(hit.source()));
            return articles;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ArticleEs> searchText(String keyword) {
        MultiMatchQuery.Builder multimatch = new MultiMatchQuery.Builder();
        MultiMatchQuery.Builder builder = multimatch.query(keyword).fields("title", "content_head", "content_body", "content_tail");
        try {
            SearchResponse<ArticleEs> articlesFound = elasticsearchClient.search(s -> s.index("article").query(Query.of(q->q.multiMatch(builder.build()))), ArticleEs.class);
            List<Hit<ArticleEs>> hits = articlesFound.hits().hits();
            List<ArticleEs> articles = new ArrayList<>();
            hits.forEach(hit -> articles.add(hit.source()));
            return articles;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
