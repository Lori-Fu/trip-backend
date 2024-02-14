package com.trip.comment.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trip.comment.dao.CommentCommentDao;
import com.trip.comment.feign.UserFeignService;
import com.trip.comment.interceptor.LoginInterceptor;
import com.trip.comment.pojo.CommentCommentPojo;
import com.trip.comment.service.CommentCommentService;
import com.trip.comment.vo.CommentCommentResponseVo;
import com.trip.comment.vo.UserInfoVo;
import com.trip.comment.vo.UserResponseVo;
import com.trip.common.exception.BusinessException;
import com.trip.common.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentCommentServiceImpl extends ServiceImpl<CommentCommentDao, CommentCommentPojo> implements CommentCommentService {
    @Autowired
    UserFeignService userFeignService;

    @Override
    public void createComment(Long comment_id, String content) {
        UserResponseVo userResponseVo = LoginInterceptor.loginUser.get();
        CommentCommentPojo pojo = new CommentCommentPojo();
        pojo.setUser_id(userResponseVo.getId());
        pojo.setComment_id(comment_id);
        pojo.setCreate_time(new Date());
        pojo.setContent(content);
        this.save(pojo);
    }

    @Override
    public List<CommentCommentResponseVo> getCommentByComment(Long comment_id) {
        LambdaQueryWrapper<CommentCommentPojo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommentCommentPojo::getComment_id, comment_id);
        wrapper.orderByDesc(CommentCommentPojo::getCreate_time);
        List<CommentCommentPojo> comments = this.list(wrapper);
        List<CommentCommentResponseVo> results = new ArrayList<>();
        for (CommentCommentPojo comment : comments){
            Long userId = comment.getUser_id();
            R result = userFeignService.getUserInfo(userId);
            if (result.getCode() != 0){
                throw new BusinessException(500, "Feign Service Query Failed");
            }
            UserInfoVo author = result.getData(new TypeReference<UserInfoVo>() {});
            CommentCommentResponseVo commentCommentResponseVo = new CommentCommentResponseVo();
            BeanUtils.copyProperties(comment,commentCommentResponseVo);
            commentCommentResponseVo.setUsername(author.getUsername());
            results.add(commentCommentResponseVo);
        }
        return results;
    }
}
