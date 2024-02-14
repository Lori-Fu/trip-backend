package com.trip.user.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;


@Data
@TableName(value = "user_info", autoResultMap = true)
public class UserPojo {
    @TableId
    private Long id;
    private String username;
    private String password;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Integer> collect_list;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Integer> thumbup_list;
}
