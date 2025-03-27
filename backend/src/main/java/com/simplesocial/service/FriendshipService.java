package com.simplesocial.service;

import com.simplesocial.entity.Friendship;
import com.simplesocial.entity.FriendshipStatus;
import com.simplesocial.entity.User;

import java.util.List;
import java.util.Optional;

public interface FriendshipService {
    Friendship createFriendship(Friendship friendship);

    Friendship findById(Long id);

    Friendship updateFriendship(Long id, Friendship friendshipDetails);

    void deleteFriendship(Long id);

    Optional<Friendship> findByUserAndFriend(User user, User friend);

    boolean existsByUserAndFriend(User user, User friend);

    List<Friendship> findUserFriendshipsByStatus(User user, FriendshipStatus status);

    List<User> findUserFriendsByStatus(User user, FriendshipStatus status);
}