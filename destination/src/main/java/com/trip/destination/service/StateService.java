package com.trip.destination.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.trip.destination.pojo.DestinationPojo;
import com.trip.destination.pojo.StatePojo;

import java.util.List;

public interface StateService extends IService<StatePojo> {
    StatePojo getStateDesc(String abbr);
}
