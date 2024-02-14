package com.trip.article.feign;

import com.trip.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("comment")
public interface CommentFeignService {

    @GetMapping("/article/{id}")
    R getComment(@PathVariable("id") Long id);

}