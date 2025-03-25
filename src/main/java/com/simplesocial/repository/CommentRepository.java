package com.simplesocial.repository;

import com.simplesocial.entity.Comment;
import com.simplesocial.entity.Post;
import com.simplesocial.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByPost(Post post, Pageable pageable);

    Page<Comment> findByAuthor(User author, Pageable pageable);
}