package com.simplesocial.controller;

import com.simplesocial.dto.ApiResponse;
import com.simplesocial.entity.Group;
import com.simplesocial.entity.GroupMember;
import com.simplesocial.entity.User;
import com.simplesocial.service.GroupService;
import com.simplesocial.service.UserService;
import java.util.List;
import org.springframework.security.core.Authentication;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Group>>> getAllGroups() {
        return ResponseEntity.ok(ApiResponse.success(groupService.findAll()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Group>> createGroup(
            @RequestBody Group group,
            Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(groupService.createGroup(group, currentUser)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Group>> getGroup(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(groupService.findById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Group>> updateGroup(
            @PathVariable Long id,
            @RequestBody Group groupDetails) {
        return ResponseEntity.ok(ApiResponse.success(groupService.updateGroup(id, groupDetails)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteGroup(@PathVariable Long id) {
        groupService.deleteGroup(id);
        return ResponseEntity.ok(ApiResponse.success("Group deleted successfully", null));
    }

    @GetMapping("/creator/{userId}")
    public ResponseEntity<ApiResponse<Page<Group>>> getGroupsByCreator(
            @PathVariable Long userId,
            Pageable pageable) {
        User creator = userService.findById(userId);
        return ResponseEntity.ok(ApiResponse.success(groupService.findByCreator(creator, pageable)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<Group>>> searchGroups(
            @RequestParam String query,
            Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(groupService.searchPublicGroups(query, pageable)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Page<Group>>> getUserGroups(
            @PathVariable Long userId,
            Pageable pageable) {
        User user = userService.findById(userId);
        return ResponseEntity.ok(ApiResponse.success(groupService.findUserGroups(user, pageable)));
    }

    @PostMapping("/{groupId}/members/{userId}")
    public ResponseEntity<ApiResponse<GroupMember>> addMember(
            @PathVariable Long groupId,
            @PathVariable Long userId,
            @RequestBody GroupMember memberDetails) {
        Group group = groupService.findById(groupId);
        User user = userService.findById(userId);
        memberDetails.setGroup(group);
        memberDetails.setUser(user);
        return ResponseEntity.ok(ApiResponse.success(groupService.addMember(memberDetails)));
    }

    @DeleteMapping("/{groupId}/members/{memberId}")
    public ResponseEntity<ApiResponse<Void>> removeMember(
            @PathVariable Long groupId,
            @PathVariable Long memberId) {
        groupService.removeMember(memberId);
        return ResponseEntity.ok(ApiResponse.success("Member removed successfully", null));
    }

    @PutMapping("/{groupId}/members/{memberId}/role")
    public ResponseEntity<ApiResponse<GroupMember>> updateMemberRole(
            @PathVariable Long groupId,
            @PathVariable Long memberId,
            @RequestBody GroupMember memberDetails) {
        return ResponseEntity.ok(ApiResponse.success(groupService.updateMemberRole(memberId, memberDetails.getRole())));
    }
}
