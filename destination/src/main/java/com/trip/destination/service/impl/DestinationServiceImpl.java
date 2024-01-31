package com.trip.destination.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trip.destination.dao.DestinationDao;
import com.trip.destination.pojo.DestinationPojo;
import com.trip.destination.service.DestinationService;
import com.trip.destination.vo.DestinationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("destinationService")
public class DestinationServiceImpl extends ServiceImpl<DestinationDao, DestinationPojo> implements DestinationService {

    @Autowired
    DestinationDao destinationDao;

    @Override
    public List<DestinationInfo> getStateAttractions(String state) {
        List<DestinationPojo> attractions = this.list(new QueryWrapper<DestinationPojo>().eq("state", state));
        List<DestinationInfo> collect = attractions.stream().map(attraction -> {
            DestinationInfo vo = new DestinationInfo();
                    vo.setId(attraction.getId());
                    vo.setAttraction(attraction.getAttraction());
                    vo.setAddress(attraction.getAddress());
                    return vo;
                }
        ).collect(Collectors.toList());
        return collect;
    }

    @Override
    public DestinationPojo getAttraction(Long attractionid) {
        return this.getOne(new QueryWrapper<DestinationPojo>().eq("id", attractionid));
    }

    @Override
    public DestinationInfo getBriefAttraction(Long id) {
        DestinationPojo destination = this.getOne(new QueryWrapper<DestinationPojo>().eq("id", id));
        DestinationInfo destinationInfo = new DestinationInfo();
        destinationInfo.setId(destination.getId());
        destinationInfo.setAttraction(destination.getAttraction());
        destinationInfo.setAddress(destination.getAddress());
        return destinationInfo;
    }
}
