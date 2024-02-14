package com.trip.destination.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("state")
public class StatePojo {
    @TableId
    private Long id;
    private String name;
    private String abbr;
    private String description;
}
