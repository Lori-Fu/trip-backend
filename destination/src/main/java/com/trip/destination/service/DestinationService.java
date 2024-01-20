package com.trip.destination.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.trip.destination.pojo.DestinationPojo;

import java.util.List;

public interface DestinationService extends IService<DestinationPojo> {
    List<DestinationPojo> getDestinations();
}
