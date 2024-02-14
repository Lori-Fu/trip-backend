package com.trip.comment.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Data
@TableName(value = "article_comments", autoResultMap = true)
public class ArticleCommentPojo {
    @TableId
    private Long id;
    private Long user_id;
    private String username;
    private Long article_id;
    private String content;
    private Date create_time;
    private Integer thumbup_num;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Integer> thumbup_list;

}