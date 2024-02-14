package com.trip.user.feign;

import com.trip.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("article")
public interface ArticleFeignService {

    @GetMapping("/myCollections")
    R getArticles(@RequestParam List<Long> list);

}
