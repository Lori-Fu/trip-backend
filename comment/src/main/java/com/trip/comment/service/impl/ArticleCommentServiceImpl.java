package com.trip.comment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trip.comment.dao.ArticleCommentDao;
import com.trip.comment.feign.UserFeignService;
import com.trip.comment.interceptor.LoginInterceptor;
import com.trip.comment.pojo.ArticleCommentPojo;
import com.trip.comment.service.ArticleCommentService;
import com.trip.comment.vo.UserResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

import static com.trip.common.constant.ArticleConstant.*;
import static com.trip.common.constant.CommentConstant.PAGE_SIZE;

@Service
public class ArticleCommentServiceImpl extends ServiceImpl<ArticleCommentDao, ArticleCommentPojo> implements ArticleCommentService {
    @Autowired
    UserFeignService userFeignService;

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public ArticleCommentPojo createComment(Long article_id, String content) {
        UserResponseVo userResponseVo = LoginInterceptor.loginUser.get();
        ArticleCommentPojo pojo = new ArticleCommentPojo();
        pojo.setUser_id(userResponseVo.getId());
        pojo.setUsername(userResponseVo.getUsername());
        pojo.setArticle_id(article_id);
        pojo.setCreate_time(new Date());
        pojo.setContent(content);
        pojo.setThumbup_num(0);
        pojo.setThumbup_list(new ArrayList<>());
        this.save(pojo);
        return pojo;
    }

    @Override
    public void incrementReplyNum(Long article_id) {
        HashOperations ops = redisTemplate.opsForHash();
        Object o = ops.get(REDIS_KEY_ARTICLE_STATISTIC_NUM + article_id, REDIS_KEY_ARTICLE_STATISTIC_REPLY_NUM);
        ops.put(REDIS_KEY_ARTICLE_STATISTIC_NUM + article_id, REDIS_KEY_ARTICLE_STATISTIC_REPLY_NUM, (Integer)o + 1);
    }

    @Override
    public void likeComment(Long comment_id) {
        UserResponseVo userResponseVo = LoginInterceptor.loginUser.get();
        ArticleCommentPojo comment = this.getOne(new QueryWrapper<ArticleCommentPojo>().eq("id", comment_id));
        boolean contains = comment.getThumbup_list().contains(Integer.valueOf(userResponseVo.getId()+""));
        if (contains){
            comment.setThumbup_num(comment.getThumbup_num()-1);
            comment.getThumbup_list().remove(Integer.valueOf(userResponseVo.getId()+""));
        }else{
            comment.setThumbup_num(comment.getThumbup_num()+1);
            comment.getThumbup_list().add(Integer.valueOf(userResponseVo.getId()+""));
        }
        this.saveOrUpdate(comment);
    }

    @Override
    public Page<ArticleCommentPojo> getCommentByArticle(Long article_id, Integer page) {
        Page queryPage = new Page<>(page, PAGE_SIZE, true);
        LambdaQueryWrapper<ArticleCommentPojo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleCommentPojo::getArticle_id, article_id);
        wrapper.orderByDesc(ArticleCommentPojo::getCreate_time);
        Page<ArticleCommentPojo> comments = this.page(queryPage, wrapper);
        return comments;
    }
}
