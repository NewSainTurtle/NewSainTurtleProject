package com.newsainturtle.friend.repository;

import com.newsainturtle.friend.entity.Friend;
import com.newsainturtle.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    Friend findByRequestUserAndReceiveUser(User requestUser, User receiveUser);

    @Query("SELECT f FROM Friend f WHERE (f.requestUser = ?1 OR f.receiveUser = ?1) AND f.isAccept = true")
    List<Friend> findByFriendList(User user);

    @Transactional
    @Modifying //clearAutomatically = true
    @Query("DELETE FROM Friend f WHERE (f.requestUser = ?1 AND f.receiveUser = ?2 OR f.requestUser = ?2 AND f.receiveUser = ?1) AND f.isAccept = true")
    void deleteByFriend(User user, User friendUser);

    @Query("SELECT f FROM Friend f WHERE (f.requestUser = ?1 AND f.receiveUser = ?2 OR f.requestUser = ?2 AND f.receiveUser = ?1) AND f.isAccept = true")
    Friend findByFriend(User user, User friendUser);

    @Transactional
    @Modifying
    @Query("DELETE FROM Friend f WHERE (f.requestUser = ?1 AND f.receiveUser = ?2 OR f.requestUser = ?2 AND f.receiveUser = ?1) AND f.isAccept = false")
    void deleteRejectedUser(User user, User friendUser);

}