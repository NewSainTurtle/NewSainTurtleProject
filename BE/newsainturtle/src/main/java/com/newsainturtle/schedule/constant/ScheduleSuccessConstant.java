package com.newsainturtle.schedule.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ScheduleSuccessConstant {

    public static final String CREATE_REGION_SUCCESS_MESSAGE = "지역 등록에 성공했습니다.";

    public static final String FIND_REGION_SUCCESS_MESSAGE = "지역 조회에 성공했습니다.";

    public static final String FIND_SCHEDULE_SUCCESS_MESSAGE = "일정 조회에 성공했습니다.";

    public static final String CREATE_SCHEDULE_SUCCESS_MESSAGE = "일정 생성에 성공했습니다.";
}
