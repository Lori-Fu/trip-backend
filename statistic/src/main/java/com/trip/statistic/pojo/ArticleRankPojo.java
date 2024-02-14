package com.trip.statistic.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;


@Data
@TableName("article_rank")
public class ArticleRankPojo {
    @TableId
    private Long id;
    private Long article_id;
    private String state;
    private String title;
    private String content_head;
    private Date create_time;
    private String cover_url;
    private Date statistic_time;
    private Integer thumbup_num;
    private Integer collect_num;
    private Integer reply_num;
    private Integer view_num;
    private Integer statistic_num;
}