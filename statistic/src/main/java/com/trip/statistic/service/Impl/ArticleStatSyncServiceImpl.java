package com.trip.statistic.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trip.statistic.mapper.ArticleContentMapper;
import com.trip.statistic.pojo.ArticleContentPojo;
import com.trip.statistic.service.ArticleStatSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import static com.trip.common.constant.ArticleConstant.*;

@Service
public class ArticleStatSyncServiceImpl extends ServiceImpl<ArticleContentMapper, ArticleContentPojo> implements ArticleStatSyncService {

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public boolean syncStat(Long article_id) {
        ArticleContentPojo article = this.getOne(new QueryWrapper<ArticleContentPojo>().eq("id", article_id));
        HashOperations ops = redisTemplate.opsForHash();
        Object view_num = ops.get(REDIS_KEY_ARTICLE_STATISTIC_NUM + article_id, REDIS_KEY_ARTICLE_STATISTIC_VIEW_NUM);
        Object collect_num = ops.get(REDIS_KEY_ARTICLE_STATISTIC_NUM + article_id, REDIS_KEY_ARTICLE_STATISTIC_COLLECT_NUM);
        Object thumb_num = ops.get(REDIS_KEY_ARTICLE_STATISTIC_NUM + article_id, REDIS_KEY_ARTICLE_STATISTIC_LIKE_NUM);
        Object reply_num = ops.get(REDIS_KEY_ARTICLE_STATISTIC_NUM + article_id, REDIS_KEY_ARTICLE_STATISTIC_REPLY_NUM);
        article.setView_num((Integer)view_num);
        article.setCollect_num((Integer)collect_num);
        article.setThumbup_num((Integer)thumb_num);
        article.setReply_num((Integer)reply_num);
        return this.saveOrUpdate(article);
    }
}
