package com.newsainturtle.friend.service;

import com.newsainturtle.friend.dto.*;
import com.newsainturtle.friend.entity.Friend;
import com.newsainturtle.friend.exception.UnableToRequestFriendFollowException;
import com.newsainturtle.friend.repository.FriendRepository;
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
public class FriendService {
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    @Transactional(readOnly = true)
    public UserSearchResponse searchUser(String email, UserSearchRequest userSearchRequest) {
        User user = userRepository.findByEmail(userSearchRequest.getEmail());
        if (user == null || user.isWithdraw()) {
            return UserSearchResponse.builder().isExist(false).build();
        }
        String status = checkUserRelationships(email,user);
        return UserSearchResponse.builder()
                .isExist(true)
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profile(user.getProfile())
                .status(status)
                .build();
    }

    String checkUserRelationships(String email, User receiveUser){
        if(receiveUser.getEmail().equals(email)){
            return  "본인";
        }else {
            User requestUser = userRepository.findByEmail(email);
            Friend friend = friendRepository.findByRequestUserAndReceiveUser(requestUser,receiveUser);
            if(friend == null){
                friend = friendRepository.findByRequestUserAndReceiveUser(receiveUser,requestUser);
                if(friend == null || !friend.isAccept()){
                    return "친구요청";
                }
            }else if(!friend.isAccept()){
                return "요청완료";
            }
        }
        return "친구";
    }

    public void followUser(String email, FriendFollowRequest friendFollowRequest) {
        User receiveUser = userRepository.findByEmail(friendFollowRequest.getEmail());
        if (receiveUser == null || receiveUser.isWithdraw() || receiveUser.getEmail().equals(email)) {
            throw new UnableToRequestFriendFollowException();
        }
        User requestUser = userRepository.findByEmail(email);
        Friend friend = friendRepository.findByRequestUserAndReceiveUser(requestUser,receiveUser);
        if(friend == null){
            friend = friendRepository.findByRequestUserAndReceiveUser(receiveUser,requestUser);
            if(friend == null){
                Friend newFriend = Friend.builder()
                        .isAccept(false)
                        .requestUser(requestUser)
                        .receiveUser(receiveUser)
                        .build();
                friendRepository.save(newFriend);
                //상대방에게 알림 전송
                return;
            }else if(!friend.isAccept()){
                friend.updateIsAccept(true);
                //나의 알림메세지 해제
                return;
            }
        }
        throw new UnableToRequestFriendFollowException();
    }

    @Transactional(readOnly = true)
    public FriendListResponse selectFriendList(String email) {
        User user = userRepository.findByEmail(email);
        List<Friend> friendList = friendRepository.findByFriendList(user);
        List<FriendInfoResponse> friendInfoResponseList = new ArrayList<>();
        User friendUser;

        for(Friend friend : friendList){
            if(friend.getRequestUser().equals(user)){
                friendUser = friend.getReceiveUser();
            }else{
                friendUser = friend.getRequestUser();
            }
            friendInfoResponseList.add(FriendInfoResponse.builder()
                    .email(friendUser.getEmail())
                    .nickname(friendUser.getNickname())
                    .profile(friendUser.getProfile()).build());
        }
        return FriendListResponse.builder().friends(friendInfoResponseList).build();
    }
}
