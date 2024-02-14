package com.trip.user.vo;


import lombok.Data;

import java.util.List;


@Data
public class ArticleBriefVoToEs {
    private Long id;
    private String title;
    private Integer day;
    private String content_head;
    private String content_tail;
    private List<String> content_body;
    private List<String> route;
    private String cover_url;
}
