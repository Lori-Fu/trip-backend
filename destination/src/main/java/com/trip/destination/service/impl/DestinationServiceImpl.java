package com.trip.destination.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trip.destination.dao.DestinationDao;
import com.trip.destination.pojo.DestinationPojo;
import com.trip.destination.service.DestinationService;
import com.trip.destination.vo.DestinationInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
                    vo.setPic(attraction.getPic());
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
    public List<List<DestinationInfo>> getDestinationForRoute(List<List<Long>> route) {
        // TODO: thread
        List<List<DestinationInfo>> result = new ArrayList<>();
        for (int i = 0; i < route.size(); i++) {
            List<Long> routeByDay = route.get(i);
            List<DestinationInfo> responseByDay = new ArrayList<>();
            for (int j = 0; j < routeByDay.size(); j++) {
                DestinationPojo destination = this.getOne(new QueryWrapper<DestinationPojo>().eq("id", routeByDay.get(j)));
                DestinationInfo destinationInfo = new DestinationInfo();
                BeanUtils.copyProperties(destination, destinationInfo);
                responseByDay.add(destinationInfo);
            }
            result.add(responseByDay);
        }
        return result;
    }

    @Override
    public List<DestinationPojo> initES(Integer current, Integer limit) {
        int offset = (current - 1) * limit;
        QueryWrapper<DestinationPojo> queryWrapper = new QueryWrapper<DestinationPojo>().last("limit " + offset + "," + limit);
        List<DestinationPojo> list = this.list(queryWrapper);
        return list;
    }

    @Override
    public DestinationPojo searchAttraction(String keyword) {
        DestinationPojo attraction = this.getOne(new QueryWrapper<DestinationPojo>().eq("attraction", keyword));
        return attraction;
    }
}
