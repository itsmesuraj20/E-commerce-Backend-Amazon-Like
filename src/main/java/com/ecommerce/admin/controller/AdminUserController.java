package com.ecommerce.admin.controller;

import com.ecommerce.user.dto.UserResponse;
import com.ecommerce.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin/users")
@Tag(name = "Admin - User Management", description = "Admin operations for user management")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @Operation(summary = "Get all users (Admin only)")
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserResponse> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get user by ID (Admin only)")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID userId) {
        UserResponse user = userService.getUserProfile(userId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{userId}/activate")
    @Operation(summary = "Activate user (Admin only)")
    public ResponseEntity<UserResponse> activateUser(@PathVariable UUID userId) {
        UserResponse user = userService.activateUser(userId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{userId}/deactivate")
    @Operation(summary = "Deactivate user (Admin only)")
    public ResponseEntity<UserResponse> deactivateUser(@PathVariable UUID userId) {
        UserResponse user = userService.deactivateUser(userId);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete user (Admin only)")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
}