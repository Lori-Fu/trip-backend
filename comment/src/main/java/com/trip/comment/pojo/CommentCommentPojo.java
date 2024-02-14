package com.trip.comment.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;


@Data
@TableName("comment_comments")
public class CommentCommentPojo {
    @TableId
    private Long id;
    private Long user_id;
    private Long comment_id;
    private String content;
    private Date create_time;
}