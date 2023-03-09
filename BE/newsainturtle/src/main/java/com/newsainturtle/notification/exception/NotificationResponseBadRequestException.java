package com.newsainturtle.notification.exception;

import com.newsainturtle.common.exception.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.newsainturtle.notification.constant.NotificationConstant.RESPONSE_NOTIFICATION_BAD_REQUEST_MESSAGE;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotificationResponseBadRequestException extends BadRequestException {
    public NotificationResponseBadRequestException() {
        super(RESPONSE_NOTIFICATION_BAD_REQUEST_MESSAGE);
    }
}