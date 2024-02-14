package com.trip.destination.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

@Data
@TableName("region")
public class RegionPojo {
    private Long id;
    private String name;
    private List<Long> states;
}
