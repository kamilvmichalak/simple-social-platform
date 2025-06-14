package com.simplesocial.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommentResponse {
    private Long id;
    private String content;
    private UserResponse author;
    private Long postId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int likesCount;
}