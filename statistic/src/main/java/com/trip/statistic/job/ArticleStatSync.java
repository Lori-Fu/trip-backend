package com.trip.statistic.job;

import com.trip.statistic.service.ArticleStatSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.trip.common.constant.ArticleConstant.REDIS_KEY_ARTICLE_STATISTIC_NUM;

@Component
public class ArticleStatSync {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    ArticleStatSyncService articleStatSyncService;

    @Scheduled(cron = "0 */10 * * * *")
    public void task() {

        Set<String> keys = redisTemplate.keys(REDIS_KEY_ARTICLE_STATISTIC_NUM + "*");
        for (String key : keys) {
            Long article_id = Long.parseLong(key.split("_")[2]);
            boolean success = articleStatSyncService.syncStat(article_id);
            if (success){
                redisTemplate.delete(key);
            }
        }
    }

}
