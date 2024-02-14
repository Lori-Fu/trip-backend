package com.trip.statistic.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trip.statistic.pojo.ArticleContentPojo;
import com.trip.statistic.pojo.ArticleRankPojo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticleContentMapper extends BaseMapper<ArticleContentPojo>{

    List<ArticleRankPojo> queryArticleRank();
}
