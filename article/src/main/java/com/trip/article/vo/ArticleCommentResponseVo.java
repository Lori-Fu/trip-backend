package com.trip.article.vo;


import lombok.Data;

import java.util.Date;


@Data
public class ArticleCommentResponseVo {
    private Long id;
    private Long article_id;
    private Long user_id;
    private String username;
    private Date create_time;
    private String content;
}
