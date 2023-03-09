package com.newsainturtle.notification.service;

import com.newsainturtle.friend.entity.Friend;
import com.newsainturtle.friend.repository.FriendRepository;
import com.newsainturtle.notification.dto.NotificationListResponse;
import com.newsainturtle.notification.dto.NotificationResponse;
import com.newsainturtle.notification.dto.NotificationResponseRequest;
import com.newsainturtle.notification.entity.FriendNotification;
import com.newsainturtle.notification.entity.Notification;
import com.newsainturtle.notification.entity.NotificationStatus;
import com.newsainturtle.notification.entity.ScheduleNotification;
import com.newsainturtle.notification.exception.NoResponseNotificationException;
import com.newsainturtle.notification.exception.NotUserOwnNotificationException;
import com.newsainturtle.notification.exception.NotificationResponseBadRequestException;
import com.newsainturtle.notification.repository.NotificationRepository;
import com.newsainturtle.schedule.entity.Schedule;
import com.newsainturtle.schedule.repository.ScheduleRepository;
import com.newsainturtle.user.entity.User;
import com.newsainturtle.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final ScheduleRepository scheduleRepository;
    private final FriendRepository friendRepository;

    @Transactional(readOnly = true)
    public NotificationListResponse selectNotificationList(String email) {
        User user = userRepository.findByEmail(email);
        List<Notification> notifications = notificationRepository.findByReceiveUser(user);
        List<NotificationResponse> notificationResponses = new ArrayList<>();

        for (Notification notification : notifications) {
            User sender = userRepository.findById(notification.getSendUserId()).orElse(null);
            if (sender == null || sender.isWithdraw()) {
                continue; //알림 데이터 삭제?
            }
            String type, content;
            if (notification instanceof FriendNotification) {
                type = "friend";
                content = "친구 요청";
            } else {
                type = "schedule";
                Schedule schedule = scheduleRepository.findById(((ScheduleNotification) notification).getScheduleId()).orElse(null);
                if (schedule == null) {
                    continue; //알림 데이터 삭제?
                } else {
                    content = schedule.getScheduleName();
                }
            }

            notificationResponses.add(NotificationResponse.builder()
                    .notificationId(notification.getNotificationId())
                    .senderNickname(sender.getNickname())
                    .type(type)
                    .content(content)
                    .status(notification.getNotificationStatus().name())
                    .build());
        }
        return NotificationListResponse.builder().notifications(notificationResponses).build();
    }

    public void removeNotification(String email, Long notificationId) {
        User user = userRepository.findByEmail(email);
        Notification notification = notificationRepository.findByNotificationIdAndReceiveUser(notificationId, user);
        if (notification == null) {
            throw new NotUserOwnNotificationException();
        }
        if (notification.getNotificationStatus() == NotificationStatus.NO_RESPONSE) {
            throw new NoResponseNotificationException();
        }
        notificationRepository.deleteByNotificationIdAndReceiveUser(notificationId, user);
    }

    public void removeNotificationAll(String email) {
        User user = userRepository.findByEmail(email);
        notificationRepository.deleteByReceiveUser(user);
    }

    public void sendFriendNotification(Long senderId, User receiver) {
        FriendNotification notification = FriendNotification.builder()
                .receiveUser(receiver)
                .sendUserId(senderId)
                .notificationStatus(NotificationStatus.NO_RESPONSE)
                .build();
        notificationRepository.save(notification);
    }

    public void changeFriendNotification(Long senderId, User receiver, NotificationStatus notificationStatus) {
        FriendNotification notification = (FriendNotification) notificationRepository.findBySendUserIdAndReceiveUser(senderId, receiver);
        notification.setNotificationStatus(notificationStatus);
    }

    public void responseNotification(String email, NotificationResponseRequest notificationResponseRequest) {
        User user = userRepository.findByEmail(email);
        long notificationId = notificationResponseRequest.getNotificationId();
        Notification notification = notificationRepository.findByNotificationIdAndReceiveUser(notificationId, user);
        if (notification != null) {
            User requestUser = userRepository.findById(notification.getSendUserId()).orElse(null);
            if (notificationResponseRequest.getType().equals("friend")) {
                responseFriendNotification((FriendNotification) notification, notificationResponseRequest.getIsAccept(), user, requestUser);
                return;
            } else if (notificationResponseRequest.getType().equals("schedule")) {
                //schedule
                return;
            }
        }
        throw new NotificationResponseBadRequestException();
    }

    public void responseFriendNotification(FriendNotification notification, boolean isAccept, User user, User requestUser) {
        if (isAccept) {
            Friend friend = friendRepository.findByRequestUserAndReceiveUser(requestUser, user);
            friend.updateIsAccept(true);
            notification.setNotificationStatus(NotificationStatus.ACCEPT);
        } else {
            friendRepository.deleteRejectedUser(requestUser, user);
            notification.setNotificationStatus(NotificationStatus.REJECT);
        }
    }
}