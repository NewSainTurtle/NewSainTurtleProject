package com.newsainturtle.schedule.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScheduleLocationResponse {

    private LocationResponse location;

    private Long day;

    private Long sequence;

    private String startTime;

    private String endTime;
}
