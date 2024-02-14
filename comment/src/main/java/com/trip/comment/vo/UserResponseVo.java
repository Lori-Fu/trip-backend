package com.trip.comment.vo;


import lombok.Data;


@Data
public class UserResponseVo {
    private Long id;
    private String username;
    private Long login_time;
    private Long expire_time;
}
