package com.trip.destination.controller;

import com.trip.common.utils.R;
import com.trip.destination.pojo.DestinationPojo;
import com.trip.destination.pojo.StatePojo;
import com.trip.destination.service.DestinationService;
import com.trip.destination.service.StateService;
import com.trip.destination.vo.DestinationInfo;
import com.trip.destination.vo.StateAttractionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class DestinationController {

    @Autowired
    private DestinationService destinationService;

    @Autowired
    private StateService stateService;

    @GetMapping("/{state}")
    public R getStateInfo(@PathVariable String state) {
        StatePojo stateInfo = stateService.getStateDesc(state);
        return R.ok().put("data", stateInfo);
    }

    @GetMapping("/{state}/attractions")
    public R getStateAttractions(@PathVariable String state) {
        List<DestinationInfo> destinationInfo = destinationService.getStateAttractions(state);
        return R.ok().put("data", destinationInfo);
    }

    @GetMapping("/attraction/{attractionid}")
    public R getAttractions(@PathVariable Long attractionid) {
        DestinationPojo attraction = destinationService.getAttraction(attractionid);
        return R.ok().put("data", attraction);
    }

    @GetMapping("/briefInfo")
    public R getDestinationForRoute(Long id){
        DestinationInfo destinationInfo = destinationService.getBriefAttraction(id);
        return R.ok().put("data",destinationInfo);
    }

}

