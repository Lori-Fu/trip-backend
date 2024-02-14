package com.trip.article.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trip.article.pojo.ArticleRankPojo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleRankDao extends BaseMapper<ArticleRankPojo>{
}
