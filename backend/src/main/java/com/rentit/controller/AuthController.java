package com.rentit.controller;

import com.rentit.dto.*;
import com.rentit.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origins}")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        AuthResponse response = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logout(Authentication authentication) {
        authService.logout(authentication.getName());
        return ResponseEntity.ok(new MessageResponse("Logout successful"));
    }

    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getProfile(Authentication authentication) {
        UserDTO user = authService.getProfile(authentication.getName());
        return ResponseEntity.ok(Map.of("user", user));
    }

    @PostMapping("/reset-password-request")
    public ResponseEntity<MessageResponse> resetPasswordRequest(@RequestBody Map<String, String> request) {
        MessageResponse response = authService.requestPasswordReset(request.get("email"));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponse> resetPassword(@RequestBody Map<String, String> request) {
        MessageResponse response = authService.resetPassword(
                request.get("resetToken"),
                request.get("newPassword")
        );
        return ResponseEntity.ok(response);
    }
}
