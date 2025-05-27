package com.simplesocial.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReactionRequest {
    @NotBlank(message = "Reaction type cannot be empty")
    private String type;
}