package com.newsainturtle.notification.service;

import com.newsainturtle.friend.entity.Friend;
import com.newsainturtle.friend.repository.FriendRepository;
import com.newsainturtle.notification.dto.NotificationListResponse;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.newsainturtle.notification.constant.NotificationConstant.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@DisplayName("알림 서비스 테스트")
class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private ScheduleRepository scheduleRepository;
    @Mock
    private FriendRepository friendRepository;

    @Nested
    @DisplayName("알림 조회 테스트")
    class SelectNotificationListTest {
        @Test
        @DisplayName("[성공] - 알림 없음")
        void nullNotificationList() {
            //given
            final String email = "yunaghgh@naver.com";
            final User user = User.builder()
                    .email("yunaghgh@naver.com")
                    .nickname("Kuuuna98")
                    .password("pwd1234")
                    .kakao(false)
                    .profile("path")
                    .withdraw(false)
                    .build();
            List<Notification> notifications = new ArrayList<>();

            doReturn(user).when(userRepository).findByEmail(email);
            doReturn(notifications).when(notificationRepository).findByReceiveUser(user);
            //when
            final NotificationListResponse result = notificationService.selectNotificationList(email);
            //then
            assertEquals(result.getNotifications().size(), 0);
        }

        @Test
        @DisplayName("[성공] - 알림 있음")
        void getNotificationList() {
            //given
            final String email = "yunaghgh@naver.com";
            final User user = User.builder()
                    .email("yunaghgh@naver.com")
                    .nickname("Kuuuna98")
                    .password("pwd1234")
                    .kakao(false)
                    .profile("path")
                    .withdraw(false)
                    .build();
            final User sender = User.builder()
                    .email("test127@naver.com")
                    .nickname("johnny")
                    .password("pwd127")
                    .kakao(false)
                    .profile("path")
                    .withdraw(false)
                    .build();
            final Notification friendNotification1 = FriendNotification.builder()
                    .receiveUser(user)
                    .sendUserId(sender.getUserId())
                    .notificationStatus(NotificationStatus.ACCEPT)
                    .build();
            final Notification scheduleNotification1 = ScheduleNotification.builder()
                    .receiveUser(user)
                    .sendUserId(sender.getUserId())
                    .notificationStatus(NotificationStatus.NO_RESPONSE)
                    .scheduleId(1L)
                    .build();
            final Notification scheduleNotification2 = ScheduleNotification.builder()
                    .receiveUser(user)
                    .sendUserId(sender.getUserId())
                    .notificationStatus(NotificationStatus.REJECT)
                    .scheduleId(2L)
                    .build();
            final Optional<Schedule> schedule1 = Optional.of(Schedule.builder()
                    .scheduleName("부산 여행")
                    .build());
            final Optional<Schedule> schedule2 = Optional.of(Schedule.builder()
                    .scheduleName("서울 여행")
                    .build());

            List<Notification> notifications = new ArrayList<>();
            notifications.add(friendNotification1);
            notifications.add(scheduleNotification1);
            notifications.add(scheduleNotification2);

            final Optional<User> optionalSender = Optional.of(User.builder()
                    .email("test127@naver.com")
                    .nickname("johnny")
                    .password("pwd127")
                    .kakao(false)
                    .profile("path")
                    .withdraw(false)
                    .build());

            doReturn(user).when(userRepository).findByEmail(email);
            doReturn(optionalSender).when(userRepository).findById(sender.getUserId());
            doReturn(schedule1).when(scheduleRepository).findById(1L);
            doReturn(schedule2).when(scheduleRepository).findById(2L);
            doReturn(notifications).when(notificationRepository).findByReceiveUser(user);
            //when
            final NotificationListResponse result = notificationService.selectNotificationList(email);
            //then
            assertAll(
                    () -> assertNotNull(result.getNotifications()),
                    () -> assertEquals(result.getNotifications().size(), 3)
            );
        }
    }

    @Nested
    @DisplayName("알림 개별 삭제 테스트")
    class RemoveNotificationItemTest {
        @Test
        @DisplayName("[실패] - 사용자 소유의 알림이 아님")
        void 사용자소유의알림이아님() {
            //given
            final String email = "yunaghgh@naver.com";
            final User user = User.builder()
                    .email("yunaghgh@naver.com")
                    .nickname("Kuuuna98")
                    .password("pwd1234")
                    .kakao(false)
                    .profile("path")
                    .withdraw(false)
                    .build();
            final User sender = User.builder()
                    .email("test127@naver.com")
                    .nickname("johnny")
                    .password("pwd127")
                    .kakao(false)
                    .profile("path")
                    .withdraw(false)
                    .build();
            final Notification friendNotification = FriendNotification.builder()
                    .receiveUser(user)
                    .sendUserId(sender.getUserId())
                    .notificationStatus(NotificationStatus.ACCEPT)
                    .build();

            doReturn(user).when(userRepository).findByEmail(email);
            doReturn(null).when(notificationRepository).findByNotificationIdAndReceiveUser(friendNotification.getNotificationId(), user);

            //when
            NotUserOwnNotificationException result = assertThrows(NotUserOwnNotificationException.class,
                    () -> notificationService.removeNotification(email, friendNotification.getNotificationId()));
            //then
            assertEquals(NOT_USER_OWN_NOTIFICATION_MESSAGE, result.getMessage());
        }

        @Test
        @DisplayName("[실패] - 응답하지않은 알림은 삭제할 수 없음")
        void 응답하지않은알림() {
            //given
            final String email = "yunaghgh@naver.com";
            final User user = User.builder()
                    .email("yunaghgh@naver.com")
                    .nickname("Kuuuna98")
                    .password("pwd1234")
                    .kakao(false)
                    .profile("path")
                    .withdraw(false)
                    .build();
            final User sender = User.builder()
                    .email("test127@naver.com")
                    .nickname("johnny")
                    .password("pwd127")
                    .kakao(false)
                    .profile("path")
                    .withdraw(false)
                    .build();
            final Notification friendNotification = FriendNotification.builder()
                    .receiveUser(user)
                    .sendUserId(sender.getUserId())
                    .notificationStatus(NotificationStatus.NO_RESPONSE)
                    .build();

            doReturn(user).when(userRepository).findByEmail(email);
            doReturn(friendNotification).when(notificationRepository).findByNotificationIdAndReceiveUser(friendNotification.getNotificationId(), user);

            //when
            NoResponseNotificationException result = assertThrows(NoResponseNotificationException.class,
                    () -> notificationService.removeNotification(email, friendNotification.getNotificationId()));
            //then
            assertEquals(NO_RESPONSE_NOTIFICATION_MESSAGE, result.getMessage());
        }

        @Test
        @DisplayName("[성공] - 알림 삭제")
        void 알림삭제() {
            //given
            final String email = "yunaghgh@naver.com";
            final User user = User.builder()
                    .email("yunaghgh@naver.com")
                    .nickname("Kuuuna98")
                    .password("pwd1234")
                    .kakao(false)
                    .profile("path")
                    .withdraw(false)
                    .build();
            final User sender = User.builder()
                    .email("test127@naver.com")
                    .nickname("johnny")
                    .password("pwd127")
                    .kakao(false)
                    .profile("path")
                    .withdraw(false)
                    .build();
            final Notification friendNotification = FriendNotification.builder()
                    .receiveUser(user)
                    .sendUserId(sender.getUserId())
                    .notificationStatus(NotificationStatus.ACCEPT)
                    .build();

            doReturn(user).when(userRepository).findByEmail(email);
            doReturn(friendNotification).when(notificationRepository).findByNotificationIdAndReceiveUser(friendNotification.getNotificationId(), user);

            //when
            notificationService.removeNotification(email, friendNotification.getNotificationId());
            //then
        }
    }

    @Nested
    @DisplayName("알림 전체 삭제 테스트")
    class RemoveNotificationAllTest {

        @Test
        @DisplayName("[성공] - 알림 전체 삭제")
        void 알림전체삭제() {
            //given
            final String email = "yunaghgh@naver.com";
            final User user = User.builder()
                    .email("yunaghgh@naver.com")
                    .nickname("Kuuuna98")
                    .password("pwd1234")
                    .kakao(false)
                    .profile("path")
                    .withdraw(false)
                    .build();

            doReturn(user).when(userRepository).findByEmail(email);
            //when
            notificationService.removeNotificationAll(email);
            //then
        }
    }

    @Nested
    @DisplayName("친구 알림 생성 테스트")
    class sendFriendNotificationTest {

        @Test
        @DisplayName("[성공] - 친구 알림 생성")
        void 친구알림생성() {
            //given
            final User user = User.builder()
                    .email("yunaghgh@naver.com")
                    .nickname("Kuuuna98")
                    .password("pwd1234")
                    .kakao(false)
                    .profile("path")
                    .withdraw(false)
                    .build();
            final User sender = User.builder()
                    .email("test127@naver.com")
                    .nickname("johnny")
                    .password("pwd127")
                    .kakao(false)
                    .profile("path")
                    .withdraw(false)
                    .build();

            //when
            notificationService.sendFriendNotification(user.getUserId(), sender);
            //then
        }
    }

    @Nested
    @DisplayName("알림 응답 테스트")
    class responseNotificationTest {

        @Test
        @DisplayName("[실패] - 올바르지 않은 요청값")
        void 올바르지않은요청값() {
            //given
            final String email = "yunaghgh@naver.com";
            final User user = User.builder()
                    .email("yunaghgh@naver.com")
                    .nickname("Kuuuna98")
                    .password("pwd1234")
                    .kakao(false)
                    .profile("path")
                    .withdraw(false)
                    .build();
            final User sender = User.builder()
                    .email("test127@naver.com")
                    .nickname("johnny")
                    .password("pwd127")
                    .kakao(false)
                    .profile("path")
                    .withdraw(false)
                    .build();

            final NotificationResponseRequest notificationResponseRequest = NotificationResponseRequest.builder()
                    .notificationId(1L)
                    .isAccept(true)
                    .type("aa")
                    .build();

            doReturn(user).when(userRepository).findByEmail(email);
            doReturn(null).when(notificationRepository).findByNotificationIdAndReceiveUser(1L, user);

            //when
            NotificationResponseBadRequestException result = assertThrows(NotificationResponseBadRequestException.class,
                    () -> notificationService.responseNotification(email, notificationResponseRequest));
            //then
            assertEquals(result.getMessage(), RESPONSE_NOTIFICATION_BAD_REQUEST_MESSAGE);
        }

        @Test
        @DisplayName("[성공] - 친구 알림 응답 - 수락")
        void 친구알림수락() {
            //given
            final String email = "yunaghgh@naver.com";
            final User user = User.builder()
                    .email("yunaghgh@naver.com")
                    .nickname("Kuuuna98")
                    .password("pwd1234")
                    .kakao(false)
                    .profile("path")
                    .withdraw(false)
                    .build();
            final User sender = User.builder()
                    .email("test127@naver.com")
                    .nickname("johnny")
                    .password("pwd127")
                    .kakao(false)
                    .profile("path")
                    .withdraw(false)
                    .build();

            final Friend friend = Friend.builder()
                    .isAccept(false)
                    .requestUser(sender)
                    .receiveUser(user)
                    .build();
            final FriendNotification notification = FriendNotification.builder()
                    .notificationId(1L)
                    .sendUserId(sender.getUserId())
                    .receiveUser(user)
                    .notificationStatus(NotificationStatus.NO_RESPONSE)
                    .build();
            final NotificationResponseRequest notificationResponseRequest = NotificationResponseRequest.builder()
                    .notificationId(1L)
                    .isAccept(true)
                    .type("friend")
                    .build();

            doReturn(user).when(userRepository).findByEmail(email);
            doReturn(notification).when(notificationRepository).findByNotificationIdAndReceiveUser(1L, user);
            doReturn(Optional.of(sender)).when(userRepository).findById(sender.getUserId());
            doReturn(friend).when(friendRepository).findByRequestUserAndReceiveUser(sender, user);

            //when
            notificationService.responseNotification(email, notificationResponseRequest);
            //then
        }

        @Test
        @DisplayName("[성공] - 친구 알림 응답 - 거절")
        void 친구알림응답거절() {
            //given
            final String email = "yunaghgh@naver.com";
            final User user = User.builder()
                    .email("yunaghgh@naver.com")
                    .nickname("Kuuuna98")
                    .password("pwd1234")
                    .kakao(false)
                    .profile("path")
                    .withdraw(false)
                    .build();
            final User sender = User.builder()
                    .email("test127@naver.com")
                    .nickname("johnny")
                    .password("pwd127")
                    .kakao(false)
                    .profile("path")
                    .withdraw(false)
                    .build();

            final Friend friend = Friend.builder()
                    .isAccept(false)
                    .requestUser(sender)
                    .receiveUser(user)
                    .build();
            final FriendNotification notification = FriendNotification.builder()
                    .notificationId(1L)
                    .sendUserId(sender.getUserId())
                    .receiveUser(user)
                    .notificationStatus(NotificationStatus.NO_RESPONSE)
                    .build();
            final NotificationResponseRequest notificationResponseRequest = NotificationResponseRequest.builder()
                    .notificationId(1L)
                    .isAccept(false)
                    .type("friend")
                    .build();

            doReturn(user).when(userRepository).findByEmail(email);
            doReturn(notification).when(notificationRepository).findByNotificationIdAndReceiveUser(1L, user);
            doReturn(Optional.of(sender)).when(userRepository).findById(sender.getUserId());

            //when
            notificationService.responseNotification(email, notificationResponseRequest);
            //then
        }
    }
}