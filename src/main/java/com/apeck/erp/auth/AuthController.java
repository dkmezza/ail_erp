package com.apeck.erp.auth;

import com.apeck.erp.auth.dto.AuthResponse;
import com.apeck.erp.auth.dto.LoginRequest;
import com.apeck.erp.auth.dto.RegisterRequest;
import com.apeck.erp.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication Controller
 * Handles authentication endpoints (login, register, logout)
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User authentication and registration endpoints")
public class AuthController {

    private final AuthService authService;

    /**
     * Login endpoint
     * POST /api/auth/login
     */
    @PostMapping("/login")
    @Operation(
        summary = "User login", 
        description = "Authenticate user with email and password, returns JWT token"
    )
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse authResponse = authService.login(loginRequest);
        return ResponseEntity.ok(authResponse);
    }

    /**
     * Register endpoint (admin only)
     * POST /api/auth/register
     */
    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
        summary = "Register new user", 
        description = "Create new user account (admin only)"
    )
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        AuthResponse authResponse = authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
    }

    /**
     * Logout endpoint
     * POST /api/auth/logout
     */
    @PostMapping("/logout")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
        summary = "User logout", 
        description = "Logout current user (token invalidation handled client-side)"
    )
    public ResponseEntity<Void> logout() {
        // JWT is stateless, so logout is handled client-side by removing the token
        // You can optionally implement a token blacklist here if needed
        return ResponseEntity.ok().build();
    }

    /**
     * Get current user profile
     * GET /api/auth/me
     */
    @GetMapping("/me")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
        summary = "Get current user", 
        description = "Get currently authenticated user information"
    )
    public ResponseEntity<UserPrincipal> getCurrentUser() {
        UserPrincipal currentUser = authService.getCurrentUser();
        return ResponseEntity.ok(currentUser);
    }
}