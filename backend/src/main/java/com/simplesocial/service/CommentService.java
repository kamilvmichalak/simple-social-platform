package com.simplesocial.service;

import com.simplesocial.dto.request.CommentRequest;
import com.simplesocial.dto.response.CommentResponse;
import com.simplesocial.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    CommentResponse createComment(Long postId, CommentRequest commentRequest, User currentUser);

    CommentResponse findById(Long id);

    CommentResponse updateComment(Long id, CommentRequest commentRequest, User currentUser);

    void deleteComment(Long id, User currentUser);

    Page<CommentResponse> findByPostId(Long postId, Pageable pageable);

    Page<CommentResponse> findByAuthor(User author, Pageable pageable);
}