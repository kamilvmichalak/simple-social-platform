package com.simplesocial.controller;

import com.simplesocial.dto.request.ReactionRequest;
import com.simplesocial.dto.response.ApiResponse;
import com.simplesocial.dto.response.ReactionResponse;
import com.simplesocial.entity.User;
import com.simplesocial.service.ReactionService;
import com.simplesocial.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reactions")
@RequiredArgsConstructor
public class ReactionController {
    private final ReactionService reactionService;
    private final UserService userService;

    @PostMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<ReactionResponse>> createReaction(
            @PathVariable Long postId,
            @Valid @RequestBody ReactionRequest reactionRequest,
            Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        return ResponseEntity
                .ok(ApiResponse.success(reactionService.createReaction(postId, reactionRequest, currentUser)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReactionResponse>> getReaction(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(reactionService.findById(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReaction(
            @PathVariable Long id,
            Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        reactionService.deleteReaction(id, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Reaction deleted successfully", null));
    }

    @GetMapping("/posts/{postId}/user")
    public ResponseEntity<ApiResponse<ReactionResponse>> getUserReactionOnPost(
            @PathVariable Long postId,
            Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(reactionService.findByUserAndPostId(currentUser, postId)));
    }

    @GetMapping("/posts/{postId}/count")
    public ResponseEntity<ApiResponse<Long>> getPostReactionCount(@PathVariable Long postId) {
        return ResponseEntity.ok(ApiResponse.success(reactionService.countByPostId(postId)));
    }
}