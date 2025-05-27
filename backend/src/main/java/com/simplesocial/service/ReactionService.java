package com.simplesocial.service;

import com.simplesocial.dto.request.ReactionRequest;
import com.simplesocial.dto.response.ReactionResponse;
import com.simplesocial.entity.User;

public interface ReactionService {
    ReactionResponse createReaction(Long postId, ReactionRequest request, User user);

    ReactionResponse findById(Long id);

    void deleteReaction(Long id, User user);

    ReactionResponse findByUserAndPostId(User user, Long postId);

    Long countByPostId(Long postId);
}