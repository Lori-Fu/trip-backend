package com.trip.article.feign;

import com.trip.article.vo.ArticleBriefVoToEs;
import com.trip.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("search")
public interface SearchFeignService {

    @PostMapping("/search/newArticle")
    R createArticle(@RequestBody ArticleBriefVoToEs article);
}