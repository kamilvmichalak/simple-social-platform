package com.simplesocial.service.impl;

import com.simplesocial.entity.Friendship;
import com.simplesocial.entity.FriendshipStatus;
import com.simplesocial.entity.User;
import com.simplesocial.exception.ResourceNotFoundException;
import com.simplesocial.repository.FriendshipRepository;
import com.simplesocial.service.FriendshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendshipServiceImpl implements FriendshipService {
    private final FriendshipRepository friendshipRepository;

    @Override
    @Transactional
    public Friendship createFriendship(Friendship friendship) {
        return friendshipRepository.save(friendship);
    }

    @Override
    public Friendship findById(Long id) {
        return friendshipRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Friendship not found with id: " + id));
    }

    @Override
    @Transactional
    public Friendship updateFriendship(Long id, Friendship friendshipDetails) {
        Friendship friendship = findById(id);

        if (friendshipDetails.getStatus() != null) {
            friendship.setStatus(friendshipDetails.getStatus());
        }

        return friendshipRepository.save(friendship);
    }

    @Override
    @Transactional
    public void deleteFriendship(Long id) {
        Friendship friendship = findById(id);
        friendshipRepository.delete(friendship);
    }

    @Override
    public Optional<Friendship> findByUserAndFriend(User user, User friend) {
        return friendshipRepository.findByUserAndFriend(user, friend);
    }

    @Override
    public boolean existsByUserAndFriend(User user, User friend) {
        return friendshipRepository.existsByUserAndFriend(user, friend);
    }

    @Override
    public List<Friendship> findUserFriendshipsByStatus(User user, FriendshipStatus status) {
        return friendshipRepository.findUserFriendshipsByStatus(user, status);
    }

    @Override
    public List<User> findUserFriendsByStatus(User user, FriendshipStatus status) {
        return friendshipRepository.findUserFriendsByStatus(user, status);
    }

    @Override
    public List<User> findFollowersByStatus(User user, FriendshipStatus status) {
        return friendshipRepository.findFollowersByStatus(user, status);
    }

    @Override
    public Long countFollowersByStatus(User user, FriendshipStatus status) {
        return friendshipRepository.countFollowersByStatus(user, status);
    }
}