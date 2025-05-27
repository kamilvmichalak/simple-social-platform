package com.simplesocial.controller;

import com.simplesocial.dto.request.PostRequest;
import com.simplesocial.dto.response.ApiResponse;
import com.simplesocial.dto.response.PostResponse;
import com.simplesocial.entity.FriendshipStatus;
import com.simplesocial.entity.User;
import com.simplesocial.service.FriendshipService;
import com.simplesocial.service.PostService;
import com.simplesocial.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final UserService userService;
    private final FriendshipService friendshipService;

    @PostMapping
    public ResponseEntity<ApiResponse<PostResponse>> createPost(
            @Valid @RequestBody PostRequest postRequest,
            Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(postService.createPost(postRequest, currentUser)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> getPost(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(postService.findById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody PostRequest postRequest,
            Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(postService.updatePost(id, postRequest, currentUser)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @PathVariable Long id,
            Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        postService.deletePost(id, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Post deleted successfully", null));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Page<PostResponse>>> getUserPosts(
            @PathVariable Long userId,
            Pageable pageable) {
        User user = userService.findById(userId);
        return ResponseEntity.ok(ApiResponse.success(postService.findByAuthor(user, pageable)));
    }

    @GetMapping("/feed")
    public ResponseEntity<ApiResponse<Page<PostResponse>>> getFeedPosts(
            Authentication authentication,
            Pageable pageable) {
        User currentUser = userService.findByUsername(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(postService.findFriendsPosts(currentUser, pageable)));
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<ApiResponse<Page<PostResponse>>> getGroupPosts(
            @PathVariable Long groupId,
            Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(postService.findByGroupId(groupId, pageable)));
    }

    @GetMapping("/public")
    public ResponseEntity<ApiResponse<Page<PostResponse>>> getPublicPosts(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(postService.findPublicPosts(pageable)));
    }
}