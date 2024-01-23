package com.trip.user.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trip.user.pojo.UserPojo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao extends BaseMapper<UserPojo> {

}