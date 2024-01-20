package com.trip.user.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName("userinfo")
public class UserPojo {
    @TableId
    private Long id;
    private String username;
    private String pwd;
    private String email;
}
