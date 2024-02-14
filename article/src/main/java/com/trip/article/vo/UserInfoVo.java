package com.trip.article.vo;

import lombok.Data;

import java.util.List;

@Data
public class UserInfoVo {
    private Long id;
    private String username;
    private List<Integer> collect_list;
}
