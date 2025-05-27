package com.simplesocial.controller;

import com.simplesocial.dto.request.CommentRequest;
import com.simplesocial.dto.response.ApiResponse;
import com.simplesocial.dto.response.CommentResponse;
import com.simplesocial.entity.User;
import com.simplesocial.service.CommentService;
import com.simplesocial.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final UserService userService;

    @PostMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentRequest commentRequest,
            Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        return ResponseEntity
                .ok(ApiResponse.success(commentService.createComment(postId, commentRequest, currentUser)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentResponse>> getComment(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(commentService.findById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentRequest commentRequest,
            Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(commentService.updateComment(id, commentRequest, currentUser)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long id,
            Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        commentService.deleteComment(id, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Comment deleted successfully", null));
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<Page<CommentResponse>>> getPostComments(
            @PathVariable Long postId,
            Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(commentService.findByPostId(postId, pageable)));
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponse<Page<CommentResponse>>> getUserComments(
            Authentication authentication,
            Pageable pageable) {
        User currentUser = userService.findByUsername(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(commentService.findByAuthor(currentUser, pageable)));
    }
}