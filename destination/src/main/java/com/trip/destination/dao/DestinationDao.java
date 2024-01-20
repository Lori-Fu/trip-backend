package com.trip.destination.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trip.destination.pojo.DestinationPojo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DestinationDao extends BaseMapper<DestinationPojo> {
}
