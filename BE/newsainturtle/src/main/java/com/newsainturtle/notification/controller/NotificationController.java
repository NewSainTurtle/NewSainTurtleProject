package com.newsainturtle.notification.controller;

import com.newsainturtle.common.dto.BaseResponse;
import com.newsainturtle.common.security.UserDetails;
import com.newsainturtle.notification.dto.NotificationListResponse;
import com.newsainturtle.notification.dto.NotificationResponseRequest;
import com.newsainturtle.notification.service.NotificationService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

import static com.newsainturtle.notification.constant.NotificationConstant.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("")
    @ApiOperation(value = "알림 조회", notes = "사용자가 받은 알림 목록 조회")
    public ResponseEntity<BaseResponse> selectNotificationList(@ApiIgnore Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getDetails();
        String email = userDetails.getUsername();
        NotificationListResponse notificationListResponse = notificationService.selectNotificationList(email);
        return new ResponseEntity<>(BaseResponse.from(
                true,
                SELECT_NOTIFICATION_LIST_SUCCESS_MESSAGE,
                notificationListResponse)
                , HttpStatus.OK);
    }

    @DeleteMapping("/{notification_id}")
    @ApiOperation(value = "알림 개별 삭제", notes = "{notification_id} 알림 삭제")
    public ResponseEntity<BaseResponse> removeNotification(@ApiIgnore Authentication authentication,
                                                           @PathVariable(name = "notification_id") final Long notificationId) {
        UserDetails userDetails = (UserDetails) authentication.getDetails();
        String email = userDetails.getUsername();
        notificationService.removeNotification(email, notificationId);
        return new ResponseEntity<>(BaseResponse.from(
                true,
                REMOVE_NOTIFICATION_ITEM_SUCCESS_MESSAGE)
                , HttpStatus.OK);
    }

    @DeleteMapping("")
    @ApiOperation(value = "알림 전체 삭제", notes = "사용자가 응답한 알림 전체 삭제")
    public ResponseEntity<BaseResponse> removeNotificationAll(@ApiIgnore Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getDetails();
        String email = userDetails.getUsername();
        notificationService.removeNotificationAll(email);
        return new ResponseEntity<>(BaseResponse.from(
                true,
                REMOVE_NOTIFICATION_ALL_SUCCESS_MESSAGE)
                , HttpStatus.OK);
    }

    @PostMapping("")
    @ApiOperation(value = "알림 응답", notes = "사용자가 알림에 응답함")
    public ResponseEntity<BaseResponse> responseNotification(@ApiIgnore Authentication authentication,
                                                             @RequestBody @Valid @ApiParam(value = "알림 응답", required = true)final NotificationResponseRequest notificationResponseRequest) {
        UserDetails userDetails = (UserDetails) authentication.getDetails();
        String email = userDetails.getUsername();
        notificationService.responseNotification(email, notificationResponseRequest) ;
        return new ResponseEntity<>(BaseResponse.from(
                true,
                RESPONSE_NOTIFICATION_SUCCESS_MESSAGE)
                , HttpStatus.OK);
    }
}
