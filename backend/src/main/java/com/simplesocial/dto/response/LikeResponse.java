package com.simplesocial.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LikeResponse {
    private Long id;
    private UserResponse user;
    private Long postId;
    private Long commentId;
    private LocalDateTime createdAt;
}