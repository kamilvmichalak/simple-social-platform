package com.simplesocial.service;

import com.simplesocial.dto.request.RegisterRequest;
import com.simplesocial.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    User registerUser(RegisterRequest registerRequest);

    User findById(Long id);

    User findByUsername(String username);

    User findByEmail(String email);

    User updateUser(Long id, User userDetails);

    void deleteUser(Long id);

    void activateUser(Long id);

    void deactivateUser(Long id);

    Page<User> findAll(Pageable pageable);

    Page<User> searchUsers(String query, Pageable pageable);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}