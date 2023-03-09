package com.newsainturtle.schedule.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class ScheduleResponse {

    private String hostEmail;

    private String scheduleRegion;

    private String scheduleName;

    private boolean isPrivate;

    private String scheduleStartDay;

    private String scheduleEndDay;

    private String scheduleStartLocation;

    private String scheduleEndLocation;

    private String vehicle;

    private List<ScheduleLocationResponse> scheduleLocations = new ArrayList<>();

}
