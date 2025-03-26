package com.simplesocial.service;

import com.simplesocial.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    User registerUser(User user);

    User findByUsername(String username);

    User findByEmail(String email);

    User findById(Long id);

    User updateUser(Long id, User userDetails);

    void deleteUser(Long id);

    Page<User> searchUsers(String query, Pageable pageable);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}