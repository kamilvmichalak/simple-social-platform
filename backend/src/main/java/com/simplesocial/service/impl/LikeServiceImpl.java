package com.simplesocial.service.impl;

import com.simplesocial.dto.response.LikeResponse;
import com.simplesocial.dto.response.UserResponse;
import com.simplesocial.entity.Comment;
import com.simplesocial.entity.Like;
import com.simplesocial.entity.Post;
import com.simplesocial.entity.User;
import com.simplesocial.exception.ResourceNotFoundException;
import com.simplesocial.repository.CommentRepository;
import com.simplesocial.repository.LikeRepository;
import com.simplesocial.repository.PostRepository;
import com.simplesocial.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public LikeResponse likePost(Long postId, User currentUser) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        if (hasLikedPost(postId, currentUser)) {
            throw new IllegalStateException("User has already liked this post");
        }

        Like like = new Like();
        like.setUser(currentUser);
        like.setPost(post);

        Like savedLike = likeRepository.save(like);
        return mapToResponse(savedLike);
    }

    @Override
    @Transactional
    public LikeResponse likeComment(Long commentId, User currentUser) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));

        if (hasLikedComment(commentId, currentUser)) {
            throw new IllegalStateException("User has already liked this comment");
        }

        Like like = new Like();
        like.setUser(currentUser);
        like.setComment(comment);

        Like savedLike = likeRepository.save(like);
        return mapToResponse(savedLike);
    }

    @Override
    @Transactional
    public void unlikePost(Long postId, User currentUser) {
        Like like = likeRepository.findByPostIdAndUserId(postId, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Like not found for post: " + postId));
        likeRepository.delete(like);
    }

    @Override
    @Transactional
    public void unlikeComment(Long commentId, User currentUser) {
        Like like = likeRepository.findByCommentIdAndUserId(commentId, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Like not found for comment: " + commentId));
        likeRepository.delete(like);
    }

    @Override
    public Page<LikeResponse> findByPostId(Long postId, Pageable pageable) {
        return likeRepository.findByPostId(postId, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<LikeResponse> findByCommentId(Long commentId, Pageable pageable) {
        return likeRepository.findByCommentId(commentId, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<LikeResponse> findByUser(User user, Pageable pageable) {
        return likeRepository.findByUser(user, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public boolean hasLikedPost(Long postId, User user) {
        return likeRepository.existsByPostIdAndUserId(postId, user.getId());
    }

    @Override
    public boolean hasLikedComment(Long commentId, User user) {
        return likeRepository.existsByCommentIdAndUserId(commentId, user.getId());
    }

    private LikeResponse mapToResponse(Like like) {
        LikeResponse response = new LikeResponse();
        response.setId(like.getId());

        UserResponse userResponse = new UserResponse();
        userResponse.setId(like.getUser().getId());
        userResponse.setUsername(like.getUser().getUsername());
        userResponse.setEmail(like.getUser().getEmail());
        response.setUser(userResponse);

        if (like.getPost() != null) {
            response.setPostId(like.getPost().getId());
        }
        if (like.getComment() != null) {
            response.setCommentId(like.getComment().getId());
        }
        response.setCreatedAt(like.getCreatedAt());
        return response;
    }
}