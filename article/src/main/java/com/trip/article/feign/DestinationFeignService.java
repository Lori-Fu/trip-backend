package com.trip.article.feign;

import com.trip.article.vo.DestinationInfo;
import com.trip.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("destination")
public interface DestinationFeignService {

    @GetMapping("/briefInfo")
    R getDestinationForRoute(@RequestParam Long id);

}