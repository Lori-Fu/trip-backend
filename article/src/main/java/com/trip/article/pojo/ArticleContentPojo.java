package com.trip.article.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;


@Data
@TableName("article_content")
public class ArticleContentPojo {
    @TableId
    private Long id;
    private Long user_id;
    private String state;
    private Integer day_count;
    private String header;
    private String content_head;
    private String content_tail;
    private Date create_time;
    private boolean post_status;
}