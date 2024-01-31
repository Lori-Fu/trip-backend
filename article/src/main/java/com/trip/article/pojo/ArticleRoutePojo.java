package com.trip.article.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName("article_route")
public class ArticleRoutePojo {
    @TableId
    private Long id;
    private Long article_id;
    private Integer day_id;
    private Integer day_count;
    private Integer dest_order;
    private Long destination_id;
}