package com.trip.statistic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.trip.statistic.pojo.ArticleContentPojo;

public interface ArticleStatSyncService extends IService<ArticleContentPojo> {

    boolean syncStat(Long article_id);
}
