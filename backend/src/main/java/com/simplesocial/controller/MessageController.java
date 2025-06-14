package com.simplesocial.controller;

import com.simplesocial.dto.response.ApiResponse;
import com.simplesocial.dto.request.MessageRequest;
import com.simplesocial.dto.response.MessageResponse;
import com.simplesocial.entity.User;
import com.simplesocial.service.MessageService;
import com.simplesocial.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<MessageResponse>> sendMessage(
            @RequestBody MessageRequest messageRequest,
            Authentication authentication) {
        User sender = userService.findByUsername(authentication.getName());
        return ResponseEntity.ok(
                ApiResponse.success(messageService.createMessage(messageRequest, sender)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MessageResponse>> getMessage(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(messageService.findById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MessageResponse>> updateMessage(
            @PathVariable Long id,
            @RequestBody MessageRequest messageRequest,
            Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        return ResponseEntity.ok(
                ApiResponse.success(messageService.updateMessage(id, messageRequest, currentUser)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return ResponseEntity.ok(ApiResponse.success("Message deleted successfully", null));
    }

    @GetMapping("/conversation/{userId}")
    public ResponseEntity<ApiResponse<Page<MessageResponse>>> getConversation(
            @PathVariable Long userId,
            Authentication authentication,
            Pageable pageable) {
        User currentUser = userService.findByUsername(authentication.getName());
        User other = userService.findById(userId);
        return ResponseEntity.ok(
                ApiResponse.success(messageService.findConversation(currentUser, other, pageable)));
    }

    @GetMapping("/unread/{userId}")
    public ResponseEntity<ApiResponse<Page<MessageResponse>>> getUnreadMessages(
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