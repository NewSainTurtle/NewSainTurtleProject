package com.newsainturtle.friend.repository;

import com.newsainturtle.friend.entity.Friend;
import com.newsainturtle.user.entity.User;
import com.newsainturtle.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("친구 레포지토리 테스트")
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
class FriendRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FriendRepository friendRepository;

    @Nested
    @DisplayName("친구 요청 확인")
    class FriendRequestSentCheck {
        @Test
        @DisplayName("[성공] - 친구요청: 보낸 친구 요청 없음 & 받은 친구 요청이 없음")
        void NoFriendRequestSentAndNoFriendRequestReceived(){
            //given
            final User requestUser = User.builder()
                    .email("test@naver.com")
                    .nickname("Kuuuna98")
                    .password("pwd1234")
                    .kakao(false)
                    .profile("path")
                    .withdraw(false)
                    .build();
            final User receiveUser = User.builder()
                    .email("test1@naver.com")
                    .nickname("johnny")
                    .password("pwd127")
                    .kakao(false)
                    .profile("path")
                    .withdraw(false)
                    .build();
            userRepository.save(requestUser);
            userRepository.save(receiveUser);

            //when
            //내가 이미 친구 요청을 보냈는지 확인
            Friend result1 = friendRepository.findByRequestUserAndReceiveUser(requestUser,receiveUser);
            //친구 요청이 왔는지 확인
            Friend result2 = friendRepository.findByRequestUserAndReceiveUser(receiveUser,requestUser);

            //then
            assertAll(
                    ()-> assertNull(result1),
                    ()-> assertNull(result2)
            );
        }

        @Test
        @DisplayName("[성공] - 친구요청: 보낸 친구 요청 없음 & 받은 친구 요청이 있음")
        void NoFriendRequestSentAndFriendRequestReceived(){
            //given
            final User requestUser = User.builder()
                    .email("test@naver.com")
                    .nickname("Kuuuna98")
                    .password("pwd1234")
                    .kakao(false)
                    .profile("path")
                    .withdraw(false)
                    .build();
            final User receiveUser = User.builder()
                    .email("test1@naver.com")
                    .nickname("johnny")
                    .password("pwd127")
                    .kakao(false)
                    .profile("path")
                    .withdraw(false)
                    .build();
            final Friend friend = Friend.builder()
                    .isAccept(false)
                    .receiveUser(requestUser)
                    .requestUser(receiveUser)
                    .build();
            userRepository.save(requestUser);
            userRepository.save(receiveUser);
            friendRepository.save(friend);

            //when
            //내가 이미 친구 요청을 보냈는지 확인
            Friend result1 = friendRepository.findByRequestUserAndReceiveUser(requestUser,receiveUser);
            //친구 요청이 왔는지 확인
            Friend result2 = friendRepository.findByRequestUserAndReceiveUser(receiveUser,requestUser);

            //then
            assertAll(
                    ()-> assertNull(result1),
                    ()-> assertNotNull(result2),
                    ()-> assertFalse(result2.isAccept())
            );
        }

        @Test
        @DisplayName("[성공] - 요청완료: 이미 요청을 보낸 상태")
        void alreadyFriendRequestSent(){
            //given
            final User requestUser = User.builder()
                    .email("test@naver.com")
                    .nickname("Kuuuna98")
                    .password("pwd1234")
                    .kakao(false)
                    .profile("path")
                    .withdraw(false)
                    .build();
            final User receiveUser = User.builder()
                    .email("test1@naver.com")
                    .nickname("johnny")
                    .password("pwd127")
                    .kakao(false)
                    .profile("path")
                    .withdraw(false)
                    .build();
            final Friend friend = Friend.builder()
                    .isAccept(false)
                    .receiveUser(receiveUser)
                    .requestUser(requestUser)
                    .build();
            userRepository.save(requestUser);
            userRepository.save(receiveUser);
            friendRepository.save(friend);

            //when
            //내가 이미 친구 요청을 보냈는지 확인
            Friend result = friendRepository.findByRequestUserAndReceiveUser(requestUser,receiveUser);

            //then
            assertAll(
                    ()-> assertNotNull(result),
                    ()-> assertFalse(result.isAccept())
            );
        }

        @Test
        @DisplayName("[성공] - 친구")
        void alreadyFriendStatus(){
            //given
            final User requestUser = User.builder()
                    .email("test@naver.com")
                    .nickname("Kuuuna98")
                    .password("pwd1234")
                    .kakao(false)
                    .profile("path")
                    .withdraw(false)
                    .build();
            final User receiveUser = User.builder()
                    .email("test1@naver.com")
                    .nickname("johnny")
                    .password("pwd127")
                    .kakao(false)
                    .profile("path")
                    .withdraw(false)
                    .build();
            final Friend friend = Friend.builder()
                    .isAccept(true)
                    .receiveUser(requestUser)
                    .requestUser(receiveUser)
                    .build();
            userRepository.save(requestUser);
            userRepository.save(receiveUser);
            friendRepository.save(friend);

            //when
            //내가 이미 친구 요청을 보냈는지 확인
            Friend result1 = friendRepository.findByRequestUserAndReceiveUser(requestUser,receiveUser);
            //친구 요청이 왔는지 확인
            Friend result2 = friendRepository.findByRequestUserAndReceiveUser(receiveUser,requestUser);

            //then
            assertAll(
                    ()-> assertNull(result1),
                    ()-> assertNotNull(result2),
                    ()-> assertTrue(result2.isAccept())
            );
        }
    }

    @Nested
    @DisplayName("친구 목록 확인")
    class SelectFriendList {
        @Test
        @DisplayName("[성공] - 친구가 없는 경우")
        void 친구가없는경우(){
            //given
            final User user = User.builder()
                    .email("test@naver.com")
                    .nickname("Kuuuna98")
                    .password("pwd1234")
                    .kakao(false)
                    .profile("path")
                    .withdraw(false)
                    .build();
            userRepository.save(user);

            //when
            List<Friend> result = friendRepository.findByFriendList(user);

            //then
            assertEquals(result.size(), 0);
        }

        @Test
        @DisplayName("[성공] - 친구가 있는 경우")
        void 친구가있는경우(){
            //given
            final User user = User.builder()
                    .email("test@naver.com")
                    .nickname("Kuuuna98")
                    .password("pwd1234")
                    .kakao(false)
                    .profile("path")
                    .withdraw(false)
                    .build();
            final User friendUser1 = User.builder()
                    .email("test1@naver.com")
                    .nickname("johnny")
                    .password("pwd127")
                    .kakao(false)
                    .profile("path")
                    .withdraw(false)
                    .build();
            final User friendUser2 = User.builder()
                    .email("mark127@naver.com")
                    .nickname("mark")
                    .password("pwd127")
                    .kakao(false)
                    .profile("path")
                    .withdraw(false)
                    .build();
            final Friend friend1 = Friend.builder()
                    .isAccept(true)
                    .receiveUser(user)
                    .requestUser(friendUser1)
                    .build();
            final Friend friend2 = Friend.builder()
                    .isAccept(true)
                    .receiveUser(user)
                    .requestUser(friendUser2)
                    .build();
            userRepository.save(user);
            userRepository.save(friendUser1);
            userRepository.save(friendUser2);
            friendRepository.save(friend1);
            friendRepository.save(friend2);

            //when
            List<Friend> result = friendRepository.findByFriendList(user);

            //then
            assertAll(
                    ()-> assertNotNull(result),
                    ()-> assertEquals(result.size(), 2)
            );
        }
    }

    @Nested
    @DisplayName("친구 삭제")
    class RemoveFriend {
        @Test
        @DisplayName("[실패] - 친구가 아닌 경우")
        void 친구가아닌경우(){
            //given
            final User user = User.builder()
                    .email("test@naver.com")
                    .nickname("Kuuuna98")
                    .password("pwd1234")
                    .kakao(false)
                    .profile("path")
                    .withdraw(false)
                    .build();
            final User friendUser = User.builder()
                    .email("test1@naver.com")
                    .nickname("johnny")
                    .password("pwd127")
                    .kakao(false)
                    .profile("path")
                    .withdraw(false)
                    .build();
            userRepository.save(user);
            userRepository.save(friendUser);

            //when
            Friend result = friendRepository.findByFriend(user,friendUser);
            //then
            assertNull(result);
        }

        @Test
        @DisplayName("[성공] - 친구맞음->친구삭제")
        void 친구삭제성공(){
            //given
            final User user = User.builder()
                    .email("test@naver.com")
                    .nickname("Kuuuna98")
                    .password("pwd1234")
                    .kakao(false)
                    .profile("path")
                    .withdraw(false)
                    .build();
            final User friendUser = User.builder()
                    .email("test1@naver.com")
                    .nickname("johnny")
                    .password("pwd127")
                    .kakao(false)
                    .profile("path")
                    .withdraw(false)
                    .build();

            final Friend friend = Friend.builder()
                    .isAccept(true)
                    .receiveUser(user)
                    .requestUser(friendUser)
                    .build();

            userRepository.save(user);
            userRepository.save(friendUser);
            friendRepository.save(friend);

            //when
            Friend resultBeforeDelete = friendRepository.findByFriend(user,friendUser);
            friendRepository.deleteByFriend(user,friendUser);
            Friend resultAfterDelete = friendRepository.findByFriend(user,friendUser);

            //then
            assertAll(
                    ()-> assertNotNull(resultBeforeDelete),
                    ()-> assertNull(resultAfterDelete)
            );
        }
    }

    @Nested
    @DisplayName("요청 거절 당했을 때, 친구 데이터 삭제")
    class RemoveRejectedUser {

        @Test
        @DisplayName("[성공] - 친구삭제")
        void 친구삭제성공(){
            //given
            final User user = User.builder()
                    .email("test@naver.com")
                    .nickname("Kuuuna98")
                    .password("pwd1234")
                    .kakao(false)
                    .profile("path")
                    .withdraw(false)
                    .build();
            final User friendUser = User.builder()
                    .email("test1@naver.com")
                    .nickname("johnny")
                    .password("pwd127")
                    .kakao(false)
                    .profile("path")
                    .withdraw(false)
                    .build();

            final Friend friend = Friend.builder()
                    .isAccept(true)
                    .receiveUser(user)
                    .requestUser(friendUser)
                    .build();

            userRepository.save(user);
            userRepository.save(friendUser);
            friendRepository.save(friend);

            //when
            friendRepository.deleteRejectedUser(user,friendUser);

            //then
        }
    }
}