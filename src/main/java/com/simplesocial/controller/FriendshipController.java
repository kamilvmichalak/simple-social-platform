package com.simplesocial.controller;

import com.simplesocial.dto.ApiResponse;
import com.simplesocial.entity.Friendship;
import com.simplesocial.entity.FriendshipStatus;
import com.simplesocial.entity.User;
import com.simplesocial.service.FriendshipService;
import com.simplesocial.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/user/{userId}/friends")
    public ResponseEntity<ApiResponse<List<User>>> getUserFriends(@PathVariable Long userId) {
        User user = userService.findById(userId);
        return ResponseEntity
                .ok(ApiResponse.success(friendshipService.findUserFriendsByStatus(user, FriendshipStatus.ACCEPTED)));
    }
}