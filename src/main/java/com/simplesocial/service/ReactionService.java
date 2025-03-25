package com.simplesocial.service;

import com.simplesocial.entity.Post;
import com.simplesocial.entity.Reaction;
import com.simplesocial.entity.User;

import java.util.Optional;

public interface ReactionService {
    Reaction createReaction(Reaction reaction);

    Reaction findById(Long id);

    Optional<Reaction> findByUserAndPost(User user, Post post);

    void deleteReaction(Long id);

    boolean existsByUserAndPost(User user, Post post);

    long countByPost(Post post);
}