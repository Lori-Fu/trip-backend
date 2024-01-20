package com.trip.comments.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trip.comments.entity.CommentsPojo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentsDao extends BaseMapper<CommentsPojo> {
}
