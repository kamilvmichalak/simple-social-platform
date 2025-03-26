package com.simplesocial.controller;

import com.simplesocial.dto.ApiResponse;
import com.simplesocial.entity.FriendshipStatus;
import com.simplesocial.entity.Post;
import com.simplesocial.entity.User;
import com.simplesocial.service.FriendshipService;
import com.simplesocial.service.PostService;
import com.simplesocial.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final UserService userService;
    private final FriendshipService friendshipService;

    @PostMapping
    public ResponseEntity<ApiResponse<Post>> createPost(@RequestBody Post post) {
        return ResponseEntity.ok(ApiResponse.success(postService.createPost(post)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Post>> getPost(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(postService.findById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Post>> updatePost(@PathVariable Long id, @RequestBody Post postDetails) {
        return ResponseEntity.ok(ApiResponse.success(postService.updatePost(id, postDetails)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok(ApiResponse.success("Post deleted successfully", null));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Page<Post>>> getUserPosts(@PathVariable Long userId, Pageable pageable) {
        User user = userService.findById(userId);
        return ResponseEntity.ok(ApiResponse.success(postService.findByAuthor(user, pageable)));
    }

    @GetMapping("/feed/{userId}")
    public ResponseEntity<ApiResponse<Page<Post>>> getFeedPosts(@PathVariable Long userId, Pageable pageable) {
        User user = userService.findById(userId);
        List<User> friends = friendshipService.findUserFriendsByStatus(user, FriendshipStatus.ACCEPTED);
        return ResponseEntity.ok(ApiResponse.success(postService.findFriendsPosts(friends, pageable)));
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<ApiResponse<Page<Post>>> getGroupPosts(@PathVariable Long groupId, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(postService.findByGroupId(groupId, pageable)));
    }

    @GetMapping("/public")
    public ResponseEntity<ApiResponse<Page<Post>>> getPublicPosts(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(postService.findPublicPosts(pageable)));
    }
}