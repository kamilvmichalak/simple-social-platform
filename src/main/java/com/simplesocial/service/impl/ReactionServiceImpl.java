package com.simplesocial.service.impl;

import com.simplesocial.entity.Post;
import com.simplesocial.entity.Reaction;
import com.simplesocial.entity.User;
import com.simplesocial.exception.ResourceNotFoundException;
import com.simplesocial.repository.ReactionRepository;
import com.simplesocial.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {
    private final ReactionRepository reactionRepository;

    @Override
    @Transactional
    public Reaction createReaction(Reaction reaction) {
        return reactionRepository.save(reaction);
    }

    @Override
    public Reaction findById(Long id) {
        return reactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reaction not found with id: " + id));
    }

    @Override
    public Optional<Reaction> findByUserAndPost(User user, Post post) {
        return reactionRepository.findByUserAndPost(user, post);
    }

    @Override
    @Transactional
    public void deleteReaction(Long id) {
        Reaction reaction = findById(id);
        reactionRepository.delete(reaction);
    }

    @Override
    public boolean existsByUserAndPost(User user, Post post) {
        return reactionRepository.existsByUserAndPost(user, post);
    }

    @Override
    public long countByPost(Post post) {
        return reactionRepository.countByPost(post);
    }
}