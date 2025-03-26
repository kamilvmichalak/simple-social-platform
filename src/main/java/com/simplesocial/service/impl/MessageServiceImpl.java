package com.simplesocial.service.impl;

import com.simplesocial.entity.Message;
import com.simplesocial.entity.User;
import com.simplesocial.exception.ResourceNotFoundException;
import com.simplesocial.repository.MessageRepository;
import com.simplesocial.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;

    @Override
    @Transactional
    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public Message findById(Long id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + id));
    }

    @Override
    @Transactional
    public Message updateMessage(Long id, Message messageDetails) {
        Message message = findById(id);

        if (messageDetails.getContent() != null) {
            message.setContent(messageDetails.getContent());
        }
        if (messageDetails.getIsRead() != message.getIsRead()) {
            message.setIsRead(messageDetails.getIsRead());
        }

        return messageRepository.save(message);
    }

    @Override
    @Transactional
    public void deleteMessage(Long id) {
        Message message = findById(id);
        messageRepository.delete(message);
    }

    @Override
    public Page<Message> findConversation(User user1, User user2, Pageable pageable) {
        return messageRepository.findConversation(user1, user2, pageable);
    }

    @Override
    public Page<Message> findUnreadMessages(User user, Pageable pageable) {
        return messageRepository.findUnreadMessages(user, pageable);
    }

    @Override
    public long countUnreadMessages(User user) {
        return messageRepository.countUnreadMessages(user);
    }
}