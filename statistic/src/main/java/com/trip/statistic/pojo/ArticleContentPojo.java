package com.trip.statistic.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
@TableName(value = "article_content", autoResultMap = true)
public class ArticleContentPojo {
    @TableId
    private Long id;
    private Long user_id;
    private String state;
    private Integer day_count;
    private String title;
    private String content_head;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> content_body;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<List<Long>> route;
    private String content_tail;
    private Date create_time;
    private boolean post_status;
    private String cover_url;
    private Integer thumbup_num;
    private Integer collect_num;
    private Integer reply_num;
    private Integer view_num;
}