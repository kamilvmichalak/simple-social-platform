package com.simplesocial.service;

import com.simplesocial.dto.response.LikeResponse;
import com.simplesocial.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LikeService {
    LikeResponse likePost(Long postId, User currentUser);

    LikeResponse likeComment(Long commentId, User currentUser);

    void unlikePost(Long postId, User currentUser);

    void unlikeComment(Long commentId, User currentUser);

    Page<LikeResponse> findByPostId(Long postId, Pageable pageable);

    Page<LikeResponse> findByCommentId(Long commentId, Pageable pageable);

    Page<LikeResponse> findByUser(User user, Pageable pageable);

    boolean hasLikedPost(Long postId, User user);

    boolean hasLikedComment(Long commentId, User user);
}