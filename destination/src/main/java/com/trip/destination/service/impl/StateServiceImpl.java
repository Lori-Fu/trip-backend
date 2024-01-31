package com.trip.destination.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trip.destination.dao.StateDao;
import com.trip.destination.pojo.StatePojo;
import com.trip.destination.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("stateService")
public class StateServiceImpl extends ServiceImpl<StateDao, StatePojo> implements StateService {

    @Autowired
    StateDao stateDao;

    @Override
    public StatePojo getStateDesc(String abbr) {
        return this.getOne(new QueryWrapper<StatePojo>().eq("abbr", abbr));
    }
}
