package com.trip.search.feign;

import com.trip.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("article")
public interface ArticleFeignService {

    @GetMapping("/initArticleES")
    R initArticleES(@RequestParam Integer current,@RequestParam Integer limit);
}
