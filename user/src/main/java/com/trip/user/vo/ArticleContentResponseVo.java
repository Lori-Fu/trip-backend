package com.trip.user.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ArticleContentResponseVo {
    private Long id;
    private Long user_id;
    private String username;
    private String state;
    private Integer day_count;
    private String title;
    private String content_head;
    private List<String> content_body;
    private List<List<DestinationInfo>> route;
    private String content_tail;
    private Date create_time;
    private boolean post_status;
    private String cover_url;
    private Integer thumbup_num;
    private Integer collect_num;
    private Integer reply_num;
    private Integer view_num;
}
