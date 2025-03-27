package com.simplesocial.service.impl;

import com.simplesocial.entity.Comment;
import com.simplesocial.entity.Post;
import com.simplesocial.entity.User;
import com.simplesocial.exception.ResourceNotFoundException;
import com.simplesocial.repository.CommentRepository;
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

    @Override
    @Transactional
    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));
    }

    @Override
    @Transactional
    public Comment updateComment(Long id, Comment commentDetails) {
        Comment comment = findById(id);

        if (commentDetails.getContent() != null) {
            comment.setContent(commentDetails.getContent());
        }

        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long id) {
        Comment comment = findById(id);
        commentRepository.delete(comment);
    }

    @Override
    public Page<Comment> findByPost(Post post, Pageable pageable) {
        return commentRepository.findByPost(post, pageable);
    }

    @Override
    public Page<Comment> findByAuthor(User author, Pageable pageable) {
        return commentRepository.findByAuthor(author, pageable);
    }
}