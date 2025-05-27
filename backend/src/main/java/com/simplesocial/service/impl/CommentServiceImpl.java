package com.simplesocial.service.impl;

import com.simplesocial.dto.request.CommentRequest;
import com.simplesocial.dto.response.CommentResponse;
import com.simplesocial.dto.response.UserResponse;
import com.simplesocial.entity.Comment;
import com.simplesocial.entity.Post;
import com.simplesocial.entity.User;
import com.simplesocial.exception.ResourceNotFoundException;
import com.simplesocial.exception.UnauthorizedException;
import com.simplesocial.repository.CommentRepository;
import com.simplesocial.repository.PostRepository;
import com.simplesocial.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Override
    @Transactional
    public CommentResponse createComment(Long postId, CommentRequest commentRequest, User currentUser) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        Comment comment = new Comment();
        comment.setContent(commentRequest.getContent());
        comment.setAuthor(currentUser);
        comment.setPost(post);

        Comment savedComment = commentRepository.save(comment);
        return mapToResponse(savedComment);
    }

    @Override
    public CommentResponse findById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));
        return mapToResponse(comment);
    }

    @Override
    @Transactional
    public CommentResponse updateComment(Long id, CommentRequest commentRequest, User currentUser) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));

        if (!comment.getAuthor().equals(currentUser)) {
            throw new UnauthorizedException("You are not authorized to update this comment");
        }

        if (commentRequest.getContent() != null) {
            comment.setContent(commentRequest.getContent());
        }

        Comment updatedComment = commentRepository.save(comment);
        return mapToResponse(updatedComment);
    }

    @Override
    @Transactional
    public void deleteComment(Long id, User currentUser) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));

        if (!comment.getAuthor().equals(currentUser)) {
            throw new UnauthorizedException("You are not authorized to delete this comment");
        }

        commentRepository.delete(comment);
    }

    @Override
    public Page<CommentResponse> findByPostId(Long postId, Pageable pageable) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));
        return commentRepository.findByPost(post, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<CommentResponse> findByAuthor(User author, Pageable pageable) {
        return commentRepository.findByAuthor(author, pageable)
                .map(this::mapToResponse);
    }

    private CommentResponse mapToResponse(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setContent(comment.getContent());

        UserResponse authorResponse = new UserResponse();
        authorResponse.setId(comment.getAuthor().getId());
        authorResponse.setUsername(comment.getAuthor().getUsername());
        authorResponse.setEmail(comment.getAuthor().getEmail());
        response.setAuthor(authorResponse);

        response.setPostId(comment.getPost().getId());
        response.setCreatedAt(comment.getCreatedAt());
        response.setUpdatedAt(comment.getUpdatedAt());
        response.setLikesCount(0); // TODO: Implement likes functionality
        return response;
    }
}