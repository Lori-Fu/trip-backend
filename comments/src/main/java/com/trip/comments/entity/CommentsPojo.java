package com.trip.comments.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;


@Data
@TableName("comment")
public class CommentsPojo {
    @TableId
    private Long id;
    private Long user_id;
    private String route;
    private String content;
    private String pic;
    private Date create_time;
}