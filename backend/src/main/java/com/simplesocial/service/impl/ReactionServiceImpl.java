package com.simplesocial.service.impl;

import com.simplesocial.dto.request.ReactionRequest;
import com.simplesocial.dto.response.ReactionResponse;
import com.simplesocial.dto.response.UserResponse;
import com.simplesocial.entity.Post;
import com.simplesocial.entity.Reaction;
import com.simplesocial.entity.ReactionType;
import com.simplesocial.entity.User;
import com.simplesocial.exception.ResourceNotFoundException;
import com.simplesocial.repository.PostRepository;
import com.simplesocial.repository.ReactionRepository;
import com.simplesocial.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {
    private final ReactionRepository reactionRepository;
    private final PostRepository postRepository;

    @Override
    @Transactional
    public ReactionResponse createReaction(Long postId, ReactionRequest request, User user) {
        Reaction reaction = new Reaction();
        reaction.setType(ReactionType.valueOf(request.getType().toUpperCase()));
        reaction.setUser(user);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        reaction.setPost(post);
        return mapToResponse(reactionRepository.save(reaction));
    }

    @Override
    public ReactionResponse findById(Long id) {
        return mapToResponse(reactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reaction not found")));
    }

    @Override
    @Transactional
    public void deleteReaction(Long id, User user) {
        Reaction reaction = reactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reaction not found"));
        if (!reaction.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Not authorized to delete this reaction");
        }
        reactionRepository.delete(reaction);
    }

    @Override
    public ReactionResponse findByUserAndPostId(User user, Long postId) {
        return reactionRepository.findByUserAndPostId(user, postId)
                .map(this::mapToResponse)
                .orElse(null);
    }

    @Override
    public Long countByPostId(Long postId) {
        return reactionRepository.countByPostId(postId);
    }

    private ReactionResponse mapToResponse(Reaction reaction) {
        ReactionResponse response = new ReactionResponse();
        response.setId(reaction.getId());
        response.setType(reaction.getType().name());
        response.setUser(UserResponse.fromUser(reaction.getUser()));
        response.setPostId(reaction.getPost().getId());
        response.setCreatedAt(reaction.getCreatedAt());
        return response;
    }
}