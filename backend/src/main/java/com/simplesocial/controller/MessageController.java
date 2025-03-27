package com.simplesocial.controller;

import com.simplesocial.dto.ApiResponse;
import com.simplesocial.entity.Message;
import com.simplesocial.entity.User;
import com.simplesocial.service.MessageService;
import com.simplesocial.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<Message>> createMessage(@RequestBody Message message) {
        return ResponseEntity.ok(ApiResponse.success(messageService.createMessage(message)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Message>> getMessage(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(messageService.findById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Message>> updateMessage(
            @PathVariable Long id,
            @RequestBody Message messageDetails) {
        return ResponseEntity.ok(ApiResponse.success(messageService.updateMessage(id, messageDetails)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return ResponseEntity.ok(ApiResponse.success("Message deleted successfully", null));
    }

    @GetMapping("/conversation/{user1Id}/{user2Id}")
    public ResponseEntity<ApiResponse<Page<Message>>> getConversation(
            @PathVariable Long user1Id,
            @PathVariable Long user2Id,
            Pageable pageable) {
        User user1 = userService.findById(user1Id);
        User user2 = userService.findById(user2Id);
        return ResponseEntity.ok(ApiResponse.success(messageService.findConversation(user1, user2, pageable)));
    }

    @GetMapping("/unread/{userId}")
    public ResponseEntity<ApiResponse<Page<Message>>> getUnreadMessages(
            @PathVariable Long userId,
            Pageable pageable) {
        User user = userService.findById(userId);
        return ResponseEntity.ok(ApiResponse.success(messageService.findUnreadMessages(user, pageable)));
    }

    @GetMapping("/unread/{userId}/count")
    public ResponseEntity<ApiResponse<Long>> getUnreadMessagesCount(@PathVariable Long userId) {
        User user = userService.findById(userId);
        return ResponseEntity.ok(ApiResponse.success(messageService.countUnreadMessages(user)));
    }
}