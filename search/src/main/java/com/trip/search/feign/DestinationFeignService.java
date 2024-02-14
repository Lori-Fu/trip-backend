package com.trip.search.feign;

import com.trip.common.utils.R;
import com.trip.search.vo.DestinationPojo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("destination")
public interface DestinationFeignService {

    @GetMapping("/initAttractionES")
    R initAttractionES(@RequestParam Integer current, @RequestParam Integer limit);

    @GetMapping("/search")
    R searchAttraction(@RequestParam String keyword);
}
