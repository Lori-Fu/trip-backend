package com.trip.search.vo;


import lombok.Data;

import java.util.List;


@Data
public class ArticleBriefVo {
    private Long id;
    private String title;
    private Integer day;
    private String content_head;
    private String content_tail;
    private List<String> content_body;
    private List<List<String>> route;
}
