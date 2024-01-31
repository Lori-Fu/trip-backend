package com.trip.article.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trip.article.pojo.ArticleContentPojo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleContentDao extends BaseMapper<ArticleContentPojo>{
}
