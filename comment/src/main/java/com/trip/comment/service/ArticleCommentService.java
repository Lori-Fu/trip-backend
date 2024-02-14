package com.trip.comment.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trip.comment.pojo.ArticleCommentPojo;
import org.springframework.stereotype.Service;

@Service
public interface ArticleCommentService extends IService<ArticleCommentPojo> {
    ArticleCommentPojo createComment(Long article_id, String content);

    void likeComment(Long comment_id);
    Page<ArticleCommentPojo> getCommentByArticle(Long article_id, Integer page);

    void incrementReplyNum(Long articleId);
}
