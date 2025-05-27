package com.simplesocial.dto.response;

import com.simplesocial.entity.ERole;
import lombok.Data;

import java.util.Set;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private Set<ERole> roles;
}