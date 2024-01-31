package com.trip.article.vo;


import lombok.Data;

import java.util.List;

@Data
public class DayContent {
    private String content;
    private List<DestinationInfo> itinerary;
}
