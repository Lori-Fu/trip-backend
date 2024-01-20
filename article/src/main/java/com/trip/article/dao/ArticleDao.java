package com.trip.article.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trip.article.pojo.ArticlePojo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleDao extends BaseMapper<ArticlePojo>{
}
