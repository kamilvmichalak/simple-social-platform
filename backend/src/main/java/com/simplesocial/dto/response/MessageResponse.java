package com.simplesocial.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MessageResponse {
    private Long id;
    private String content;
    private UserResponse sender;
    private UserResponse recipient;
    private LocalDateTime createdAt;
    private boolean read;
}