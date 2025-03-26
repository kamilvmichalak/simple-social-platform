package com.simplesocial.controller;

import com.simplesocial.dto.ApiResponse;
import com.simplesocial.entity.Post;
import com.simplesocial.entity.Reaction;
import com.simplesocial.entity.User;
import com.simplesocial.service.PostService;
import com.simplesocial.service.ReactionService;
import com.simplesocial.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/reactions")
@RequiredArgsConstructor
public class ReactionController {
    private final ReactionService reactionService;
    private final PostService postService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<Reaction>> createReaction(@RequestBody Reaction reaction) {
        return ResponseEntity.ok(ApiResponse.success(reactionService.createReaction(reaction)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Reaction>> getReaction(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(reactionService.findById(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReaction(@PathVariable Long id) {
        reactionService.deleteReaction(id);
        return ResponseEntity.ok(ApiResponse.success("Reaction deleted successfully", null));
    }

    @GetMapping("/user/{userId}/post/{postId}")
    public ResponseEntity<ApiResponse<Reaction>> getUserReactionOnPost(
            @PathVariable Long userId,
            @PathVariable Long postId) {
        User user = userService.findById(userId);
        Post post = postService.findById(postId);
        Optional<Reaction> reaction = reactionService.findByUserAndPost(user, post);
        return ResponseEntity.ok(ApiResponse.success(reaction.orElse(null)));
    }

    @GetMapping("/post/{postId}/count")
    public ResponseEntity<ApiResponse<Long>> getPostReactionCount(@PathVariable Long postId) {
        Post post = postService.findById(postId);
        return ResponseEntity.ok(ApiResponse.success(reactionService.countByPost(post)));
    }
}