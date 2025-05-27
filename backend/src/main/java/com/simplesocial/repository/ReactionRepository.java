package com.simplesocial.repository;

import com.simplesocial.entity.Reaction;
import com.simplesocial.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    Optional<Reaction> findByUserAndPostId(User user, Long postId);

    Long countByPostId(Long postId);
}