package com.simplesocial.controller;

import com.simplesocial.dto.ApiResponse;
import com.simplesocial.entity.User;
import com.simplesocial.service.UserService;
import com.simplesocial.service.PostService;
import com.simplesocial.service.FriendshipService;
import com.simplesocial.entity.FriendshipStatus;
import org.springframework.security.core.Authentication;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PostService postService;
    private final FriendshipService friendshipService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(userService.findById(id)));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<ApiResponse<User>> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(ApiResponse.success(userService.findByUsername(username)));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<User>> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(ApiResponse.success(userService.findByEmail(email)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<User>>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(userService.findAll(pageable)));
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<?>> getMyProfile(Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());

        long postsCount = postService.countByAuthor(currentUser);
        long followersCount = friendshipService.countFollowersByStatus(currentUser, FriendshipStatus.ACCEPTED);

        var data = new java.util.HashMap<String, Object>();
        data.put("id", currentUser.getId());
        data.put("username", currentUser.getUsername());
        data.put("email", currentUser.getEmail());
        data.put("profilePicture", currentUser.getProfilePicture());
        data.put("postsCount", postsCount);
        data.put("followersCount", followersCount);

        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        return ResponseEntity.ok(ApiResponse.success(userService.updateUser(id, userDetails)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<Void>> deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deactivated successfully", null));
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<Void>> activateUser(@PathVariable Long id) {
        userService.activateUser(id);
        return ResponseEntity.ok(ApiResponse.success("User activated successfully", null));
    }
}