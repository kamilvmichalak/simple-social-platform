package com.simplesocial.service.impl;

import com.simplesocial.dto.request.MessageRequest;
import com.simplesocial.dto.response.MessageResponse;
import com.simplesocial.dto.response.UserResponse;
import com.simplesocial.entity.Message;
import com.simplesocial.entity.User;
import com.simplesocial.exception.ResourceNotFoundException;
import com.simplesocial.repository.MessageRepository;
import com.simplesocial.service.MessageService;
import com.simplesocial.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final UserService userService;

    @Override
    @Transactional
    public MessageResponse createMessage(MessageRequest request, User sender) {
        User receiver = userService.findById(request.getRecipientId());
        Message message = new Message();
        message.setContent(request.getContent());
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setReadable(false);

        return mapToResponse(messageRepository.save(message));
    }

    @Override
    public MessageResponse findById(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + id));
        return mapToResponse(message);
    }

    @Override
    @Transactional
    public MessageResponse updateMessage(Long id, MessageRequest request, User currentUser) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + id));

        if (!message.getSender().equals(currentUser)) {
            throw new ResourceNotFoundException("Not authorized to update this message");
        }

        if (request.getContent() != null) {
            message.setContent(request.getContent());
        }

        Message updated = messageRepository.save(message);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void deleteMessage(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + id));
        messageRepository.delete(message);
    }

    @Override
    public Page<MessageResponse> findConversation(User user1, User user2, Pageable pageable) {
        return messageRepository.findConversation(user1, user2, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<MessageResponse> findUnreadMessages(User user, Pageable pageable) {
        return messageRepository.findUnreadMessages(user, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public long countUnreadMessages(User user) {
        return messageRepository.countUnreadMessages(user);
    }

    private MessageResponse mapToResponse(Message message) {
        MessageResponse response = new MessageResponse();
        response.setId(message.getId());
        response.setContent(message.getContent());
        response.setSender(UserResponse.fromUser(message.getSender()));
        response.setRecipient(UserResponse.fromUser(message.getReceiver()));
        response.setCreatedAt(message.getCreatedAt());
        response.setRead(message.isReadable());
        return response;
    }
}