package com.simplesocial.repository;

import com.simplesocial.entity.Post;
import com.simplesocial.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByAuthor(User author, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.author IN :friends AND p.isPublic = true ORDER BY p.createdAt DESC")
    Page<Post> findFriendsPosts(@Param("friends") Iterable<User> friends, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.group.id = :groupId ORDER BY p.createdAt DESC")
    Page<Post> findByGroupId(@Param("groupId") Long groupId, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.isPublic = true ORDER BY p.createdAt DESC")
    Page<Post> findPublicPosts(Pageable pageable);
}