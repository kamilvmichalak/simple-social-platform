package com.simplesocial.repository;

import com.simplesocial.entity.Message;
import com.simplesocial.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT m FROM Message m WHERE " +
            "(m.sender = :user1 AND m.receiver = :user2) OR " +
            "(m.sender = :user2 AND m.receiver = :user1) " +
            "ORDER BY m.createdAt DESC")
    Page<Message> findConversation(
            @Param("user1") User user1,
            @Param("user2") User user2,
            Pageable pageable);

    @Query("SELECT m FROM Message m WHERE m.receiver = :user AND m.isRead = false ORDER BY m.createdAt DESC")
    Page<Message> findUnreadMessages(@Param("user") User user, Pageable pageable);

    @Query("SELECT COUNT(m) FROM Message m WHERE m.receiver = :user AND m.isRead = false")
    long countUnreadMessages(@Param("user") User user);
}