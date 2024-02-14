package com.trip.article.feign;

import com.trip.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("user")
public interface UserFeignService {

    @GetMapping("/info/{id}")
    R getUserInfo(@PathVariable("id") Long id);

    @GetMapping("/statistic/{id}")
    R getStatList(@PathVariable("id") Long id);
}