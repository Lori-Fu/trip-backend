package com.trip.destination.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("states")
public class StatePojo {
    private Long id;
    private String name;
    private String abbr;
    private String description;

}
