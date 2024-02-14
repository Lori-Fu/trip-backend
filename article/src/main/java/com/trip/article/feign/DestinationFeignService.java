package com.trip.article.feign;

import com.trip.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("destination")
public interface DestinationFeignService {

    @GetMapping("/briefInfo")
    R getDestinationForRoute(@RequestParam("route") List<List<Long>> route);

}