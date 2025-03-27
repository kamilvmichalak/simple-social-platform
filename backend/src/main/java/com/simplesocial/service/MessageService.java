package com.simplesocial.service;

import com.simplesocial.entity.Message;
import com.simplesocial.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageService {
    Message createMessage(Message message);

    Message findById(Long id);

    Message updateMessage(Long id, Message messageDetails);

    void deleteMessage(Long id);

    Page<Message> findConversation(User user1, User user2, Pageable pageable);

    Page<Message> findUnreadMessages(User user, Pageable pageable);

    long countUnreadMessages(User user);
}