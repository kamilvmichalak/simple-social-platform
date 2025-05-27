package com.simplesocial.repository;

import com.simplesocial.entity.Like;
import com.simplesocial.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByPostIdAndUserId(Long postId, Long userId);

    Optional<Like> findByCommentIdAndUserId(Long commentId, Long userId);

    Page<Like> findByPostId(Long postId, Pageable pageable);

    Page<Like> findByCommentId(Long commentId, Pageable pageable);

    Page<Like> findByUser(User user, Pageable pageable);

    boolean existsByPostIdAndUserId(Long postId, Long userId);

    boolean existsByCommentIdAndUserId(Long commentId, Long userId);
}