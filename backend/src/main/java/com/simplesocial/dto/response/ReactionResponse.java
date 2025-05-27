package com.simplesocial.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReactionResponse {
    private Long id;
    private String type;
    private UserResponse user;
    private Long postId;
    private LocalDateTime createdAt;
}