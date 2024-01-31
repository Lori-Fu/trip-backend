package com.trip.article.vo;

import lombok.Data;

import java.util.List;

@Data
public class ArticleVo {
    String state;
    Integer day_count;
    String content_head;
    String content_tail;
    String header;
    List<List<DestinationInfo>> itinerary;
    List<String> articleBody;
    boolean visibility;
}
