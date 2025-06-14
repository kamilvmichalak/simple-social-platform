package com.simplesocial.controller;

import com.simplesocial.dto.ApiResponse;
import com.simplesocial.entity.Friendship;
import com.simplesocial.entity.FriendshipStatus;
import com.simplesocial.entity.User;
import com.simplesocial.service.FriendshipService;
import com.simplesocial.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/friendships")
@RequiredArgsConstructor
public class FriendshipController {
    private final FriendshipService friendshipService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<Friendship>> createFriendship(@RequestBody Friendship friendship) {
        return ResponseEntity.ok(ApiResponse.success(friendshipService.createFriendship(friendship)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Friendship>> getFriendship(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(friendshipService.findById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Friendship>> updateFriendship(
            @PathVariable Long id,
            @RequestBody Friendship friendshipDetails) {
        return ResponseEntity.ok(ApiResponse.success(friendshipService.updateFriendship(id, friendshipDetails)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFriendship(@PathVariable Long id) {
        friendshipService.deleteFriendship(id);
        return ResponseEntity.ok(ApiResponse.success("Friendship deleted successfully", null));
    }

    @GetMapping("/user/{userId}/friend/{friendId}")
    public ResponseEntity<ApiResponse<Friendship>> getFriendshipBetweenUsers(
            @PathVariable Long userId,
            @PathVariable Long friendId) {
        User user = userService.findById(userId);
        User friend = userService.findById(friendId);
        Optional<Friendship> friendship = friendshipService.findByUserAndFriend(user, friend);
        return ResponseEntity.ok(ApiResponse.success(friendship.orElse(null)));
    }

    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<ApiResponse<List<Friendship>>> getUserFriendshipsByStatus(
            @PathVariable Long userId,
            @PathVariable FriendshipStatus status) {
        User user = userService.findById(userId);
        return ResponseEntity.ok(ApiResponse.success(friendshipService.findUserFriendshipsByStatus(user, status)));
    }

    @PostMapping("/user/{friendId}/follow")
    public ResponseEntity<ApiResponse<Friendship>> followUser(
            @PathVariable Long friendId,
            Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        User friend = userService.findById(friendId);

        if (friend == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("User not found"));
        }

        // Check if already following
        Optional<Friendship> existingFriendship = friendshipService.findByUserAndFriend(currentUser, friend);
        if (existingFriendship.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success(existingFriendship.get()));
        }

        // Create new friendship with ACCEPTED status
        Friendship friendship = new Friendship();
        friendship.setUser(currentUser);
        friendship.setFriend(friend);
        friendship.setStatus(FriendshipStatus.ACCEPTED); // Direct follow, no need for acceptance

        try {
            Friendship savedFriendship = friendshipService.createFriendship(friendship);
            return ResponseEntity.ok(ApiResponse.success(savedFriendship));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to follow user: " + e.getMessage()));
        }
    }

    @DeleteMapping("/user/{friendId}/unfollow")
    public ResponseEntity<ApiResponse<Void>> unfollowUser(
            @PathVariable Long friendId,
            Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        User friend = userService.findById(friendId);

        if (friend == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("User not found"));
        }

        Optional<Friendship> friendship = friendshipService.findByUserAndFriend(currentUser, friend);
        if (friendship.isPresent()) {
            friendshipService.deleteFriendship(friendship.get().getId());
            return ResponseEntity.ok(ApiResponse.success("Unfollowed successfully", null));
        }

        return ResponseEntity.ok(ApiResponse.success("Already not following", null));
    }

    @GetMapping("/user/me/friends")
    public ResponseEntity<ApiResponse<List<User>>> getMyFriends(Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(
                friendshipService.findUserFriendsByStatus(currentUser, FriendshipStatus.ACCEPTED)));
    }

    @GetMapping("/user/{userId}/friends")
    public ResponseEntity<ApiResponse<List<User>>> getUserFriends(@PathVariable Long userId) {
        User user = userService.findById(userId);
        return ResponseEntity.ok(ApiResponse.success(
                friendshipService.findUserFriendsByStatus(user, FriendshipStatus.ACCEPTED)));
    }

    @GetMapping("/user/me/followers")
    public ResponseEntity<ApiResponse<List<User>>> getMyFollowers(Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(
                friendshipService.findFollowersByStatus(currentUser, FriendshipStatus.ACCEPTED)));
    }

    @GetMapping("/user/{userId}/followers/count")
    public ResponseEntity<ApiResponse<Long>> countFollowers(@PathVariable Long userId) {
        User user = userService.findById(userId);
        return ResponseEntity.ok(ApiResponse.success(
                friendshipService.countFollowersByStatus(user, FriendshipStatus.ACCEPTED)));
    }
}