package com.trip.statistic.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trip.statistic.mapper.ArticleContentMapper;
import com.trip.statistic.mapper.ArticleRankMapper;
import com.trip.statistic.pojo.ArticleRankPojo;
import com.trip.statistic.service.ArticleRankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Component
public class ArticleRankStatistic {

    @Autowired
    ArticleContentMapper articleContentMapper;

    @Autowired
    ArticleRankMapper articleRankMapper;

    @Autowired
    ArticleRankService articleRankService;

    @Transactional(rollbackFor = Exception.class)
    @Scheduled(cron = "0 0 0/1 * * *")
    void statisticRank(){
        Date now = new Date();
        List<ArticleRankPojo> articleRanks = articleContentMapper.queryArticleRank();
        for (ArticleRankPojo rank: articleRanks) {
            rank.setStatistic_time(now);
        }
        articleRankService.saveBatch(articleRanks);
        articleRankService.remove(new QueryWrapper<ArticleRankPojo>().lt("UNIX_TIMESTAMP(statistic_time)", now.getTime()/1000-5));
    }
}
