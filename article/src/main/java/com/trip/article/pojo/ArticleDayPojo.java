package com.trip.article.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;


@Data
@TableName("article_day")
public class ArticleDayPojo {
    @TableId
    private Long id;
    private Long article_id;
    private Integer day_id;
    private Integer day_count;
    private String content;
}