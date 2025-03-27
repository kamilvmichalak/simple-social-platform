package com.simplesocial.controller;

import com.simplesocial.dto.ApiResponse;
import com.simplesocial.entity.Comment;
import com.simplesocial.entity.Post;
import com.simplesocial.service.CommentService;
import com.simplesocial.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final PostService postService;

    @PostMapping
    public ResponseEntity<ApiResponse<Comment>> createComment(@RequestBody Comment comment) {
        return ResponseEntity.ok(ApiResponse.success(commentService.createComment(comment)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Comment>> getComment(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(commentService.findById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Comment>> updateComment(@PathVariable Long id,
            @RequestBody Comment commentDetails) {
        return ResponseEntity.ok(ApiResponse.success(commentService.updateComment(id, commentDetails)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok(ApiResponse.success("Comment deleted successfully", null));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<ApiResponse<Page<Comment>>> getPostComments(@PathVariable Long postId, Pageable pageable) {
        Post post = postService.findById(postId);
        return ResponseEntity.ok(ApiResponse.success(commentService.findByPost(post, pageable)));
    }
}