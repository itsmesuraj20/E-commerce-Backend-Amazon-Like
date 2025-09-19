package com.ecommerce.user.controller;

import com.ecommerce.user.dto.*;
import com.ecommerce.user.security.UserPrincipal;
import com.ecommerce.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@Tag(name = "User Management", description = "User profile and address management")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @Operation(summary = "Get current user profile")
    public ResponseEntity<UserResponse> getCurrentUserProfile(@AuthenticationPrincipal UserPrincipal currentUser) {
        UserResponse userResponse = userService.getUserProfile(currentUser.getId());
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("/profile")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @Operation(summary = "Update current user profile")
    public ResponseEntity<UserResponse> updateCurrentUserProfile(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody UserUpdateRequest updateRequest) {
        UserResponse userResponse = userService.updateUserProfile(currentUser.getId(), updateRequest);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/addresses")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @Operation(summary = "Get user addresses")
    public ResponseEntity<List<AddressResponse>> getUserAddresses(@AuthenticationPrincipal UserPrincipal currentUser) {
        List<AddressResponse> addresses = userService.getUserAddresses(currentUser.getId());
        return ResponseEntity.ok(addresses);
    }

    @PostMapping("/addresses")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @Operation(summary = "Add new address")
    public ResponseEntity<AddressResponse> addAddress(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody AddressRequest addressRequest) {
        AddressResponse addressResponse = userService.addAddress(currentUser.getId(), addressRequest);
        return ResponseEntity.ok(addressResponse);
    }

    @DeleteMapping("/addresses/{addressId}")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @Operation(summary = "Delete address")
    public ResponseEntity<Void> deleteAddress(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable UUID addressId) {
        userService.deleteAddress(currentUser.getId(), addressId);
        return ResponseEntity.ok().build();
    }
}