package com.trip.article.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ArticleResponseVo {
    private Long id;
    private ArticleAuthor author;
    private String state;
    private Integer day_count;
    private String header;
    private String content_head;
    private String content_tail;
    private List<DayContent> content_per_day;
    private Date create_time;
    private boolean visibility;
}
