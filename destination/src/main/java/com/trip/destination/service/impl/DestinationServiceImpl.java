package com.trip.destination.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trip.destination.dao.DestinationDao;
import com.trip.destination.pojo.DestinationPojo;
import com.trip.destination.service.DestinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("destinationService")
public class DestinationServiceImpl extends ServiceImpl<DestinationDao, DestinationPojo> implements DestinationService {

    @Autowired
    DestinationDao destinationDao;

    @Override
    public List<DestinationPojo> getDestinations() {
        return baseMapper.selectList(null);
    }
}
