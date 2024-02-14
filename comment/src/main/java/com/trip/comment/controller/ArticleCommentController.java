package com.trip.comment.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trip.comment.annotation.RequireLogin;
import com.trip.comment.pojo.ArticleCommentPojo;
import com.trip.comment.service.ArticleCommentService;
import com.trip.comment.service.CommentCommentService;
import com.trip.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/")
public class ArticleCommentController {

    @Autowired
    ArticleCommentService articleCommentService;

    @Autowired
    CommentCommentService commentCommentService;

    @GetMapping("/article/{id}/{page}")
    public R getCommentByArticle(@PathVariable("id") Long article_id, @PathVariable("page") Integer page){
        Page<ArticleCommentPojo> comments = articleCommentService.getCommentByArticle(article_id, page);
        return R.ok().put("data",comments);
    }

    @RequireLogin
    @PostMapping("/article/new")
    public R newArticleComment(@RequestBody Map<String, Object> map){
        String content = (String) map.get("content");
        Long article_id = Long.valueOf((Integer)map.get("article_id"));
        articleCommentService.incrementReplyNum(article_id);
        ArticleCommentPojo comment = articleCommentService.createComment(article_id, content);
        articleCommentService.incrementReplyNum(article_id);
        return R.ok().put("comment",comment);
    }

    @RequireLogin
    @GetMapping("/article/like/{id}")
    public R likeArticleComment(@PathVariable("id") Long comment_id){
        articleCommentService.likeComment(comment_id);
        return R.ok();
    }

    @RequireLogin
    @PostMapping("/comment/new")
    public R newCommentComment(@RequestBody Map<String, Object> map){
        String content = (String) map.get("content");
        Long comment_id = Long.valueOf((Integer)map.get("comment_id"));
        commentCommentService.createComment(comment_id,content);
        return R.ok();
    }

}
