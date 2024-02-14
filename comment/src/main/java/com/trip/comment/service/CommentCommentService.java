package com.trip.comment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.trip.comment.pojo.CommentCommentPojo;
import com.trip.comment.vo.CommentCommentResponseVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentCommentService extends IService<CommentCommentPojo> {
    void createComment(Long comment_id, String content);

    List<CommentCommentResponseVo> getCommentByComment(Long comment_id);
}
