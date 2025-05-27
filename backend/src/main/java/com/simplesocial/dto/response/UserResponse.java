package com.simplesocial.dto.response;

import com.simplesocial.entity.ERole;
import com.simplesocial.entity.User;
import lombok.Data;

import java.util.Set;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private Set<ERole> roles;
    private String profilePicture;

    public static UserResponse fromUser(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setProfilePicture(user.getProfilePicture());
        return response;
    }
}