package com.trip.destination.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("destination")
public class DestinationPojo {
    private Long id;
    private String attraction;
    private String state;
    private String detail;
    private String address;
    private String pic;
}
