package com.simplesocial.service;

import com.simplesocial.dto.request.MessageRequest;
import com.simplesocial.dto.response.MessageResponse;
import com.simplesocial.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageService {
    MessageResponse createMessage(MessageRequest request, User sender);

    MessageResponse findById(Long id);

    MessageResponse updateMessage(Long id, MessageRequest request, User currentUser);

    void deleteMessage(Long id);

    Page<MessageResponse> findConversation(User user1, User user2, Pageable pageable);

    Page<MessageResponse> findUnreadMessages(User user, Pageable pageable);

    long countUnreadMessages(User user);
}