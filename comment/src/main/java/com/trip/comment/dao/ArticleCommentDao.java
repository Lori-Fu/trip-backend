package com.trip.comment.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trip.comment.pojo.ArticleCommentPojo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleCommentDao extends BaseMapper<ArticleCommentPojo> {
}
