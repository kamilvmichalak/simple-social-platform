package com.simplesocial.service;

import com.simplesocial.entity.Comment;
import com.simplesocial.entity.Post;
import com.simplesocial.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    Comment createComment(Comment comment);

    Comment findById(Long id);

    Comment updateComment(Long id, Comment commentDetails);

    void deleteComment(Long id);

    Page<Comment> findByPost(Post post, Pageable pageable);

    Page<Comment> findByAuthor(User author, Pageable pageable);
}