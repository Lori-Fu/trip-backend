package com.trip.comment.vo;


import lombok.Data;

import java.util.Date;


@Data
public class CommentCommentResponseVo {
    private Long id;
    private Long comment_id;
    private Long user_id;
    private String username;
    private Date create_time;
    private String content;
}
