package com.simplesocial.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PostResponse {
    private Long id;
    private String content;
    private String imageUrl;
    private boolean public_;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserResponse author;
    private Long likesCount;
    private Long commentsCount;
}