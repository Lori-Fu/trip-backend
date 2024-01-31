package com.trip.destination.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.trip.destination.pojo.DestinationPojo;
import com.trip.destination.vo.DestinationInfo;
import com.trip.destination.vo.StateAttractionVo;

import java.util.List;

public interface DestinationService extends IService<DestinationPojo> {

    List<DestinationInfo> getStateAttractions(String state);
    DestinationPojo getAttraction(Long attractionid);
    DestinationInfo getBriefAttraction(Long id);
}
