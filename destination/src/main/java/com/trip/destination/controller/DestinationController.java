package com.trip.destination.controller;

import com.trip.common.utils.R;
import com.trip.destination.pojo.DestinationPojo;
import com.trip.destination.service.DestinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class DestinationController {

    @Autowired
    private DestinationService destinationService;

    @GetMapping("/")
    public R list() {
        List<DestinationPojo> destinations = destinationService.getDestinations();
        return R.ok().put("data", destinations);
    }

}

