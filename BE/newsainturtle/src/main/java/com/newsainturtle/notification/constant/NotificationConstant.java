package com.newsainturtle.notification.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationConstant {
    public static final String SELECT_NOTIFICATION_LIST_SUCCESS_MESSAGE = "알림 목록 조회를 성공했습니다.";
    public static final String REMOVE_NOTIFICATION_ITEM_SUCCESS_MESSAGE = "알림 개별 삭제를 성공했습니다.";
    public static final String REMOVE_NOTIFICATION_ALL_SUCCESS_MESSAGE = "알림 전체 삭제를 성공했습니다.";
    public static final String RESPONSE_NOTIFICATION_SUCCESS_MESSAGE = "알림 응답에 성공했습니다.";

    public static final String NOT_USER_OWN_NOTIFICATION_MESSAGE = "사용자의 알림이 아닙니다.";
    public static final String NO_RESPONSE_NOTIFICATION_MESSAGE = "삭제할 수 없는 알림입니다.";
    public static final String RESPONSE_NOTIFICATION_BAD_REQUEST_MESSAGE = "잘못된 값이 들어왔습니다.";
}
