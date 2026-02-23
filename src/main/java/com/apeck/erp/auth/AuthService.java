package com.apeck.erp.auth;

import com.apeck.erp.auth.dto.AuthResponse;
import com.apeck.erp.auth.dto.LoginRequest;
import com.apeck.erp.auth.dto.RegisterRequest;
import com.apeck.erp.common.exception.BadRequestException;
import com.apeck.erp.security.JwtTokenProvider;
import com.apeck.erp.security.UserPrincipal;
import com.apeck.erp.user.User;
import com.apeck.erp.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Authentication Service
 * Handles user authentication, registration, and token generation
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Authenticate user and generate JWT token
     */
    public AuthResponse login(LoginRequest loginRequest) {
        log.info("Authenticating user: {}", loginRequest.getEmail());
        
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // Generate JWT token
        String token = tokenProvider.generateToken(authentication);
        
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        
        log.info("User authenticated successfully: {}", userPrincipal.getEmail());
        
        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(userPrincipal.getId())
                .name(userPrincipal.getName())
                .email(userPrincipal.getEmail())
                .role(userPrincipal.getRole())
                .department(userPrincipal.getDepartment())
                .build();
    }

    /**
     * Register new user (admin only)
     */
    @Transactional
    public AuthResponse register(RegisterRequest registerRequest) {
        log.info("Registering new user: {}", registerRequest.getEmail());
        
        // Check if email already exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new BadRequestException("Email already registered: " + registerRequest.getEmail());
        }
        
        // Create new user
        User user = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .phone(registerRequest.getPhone())
                .role(registerRequest.getRole())
                .department(registerRequest.getDepartment())
                .passwordHash(passwordEncoder.encode(registerRequest.getPassword()))
                .isActive(true)
                .build();
        
        user = userRepository.save(user);
        
        log.info("User registered successfully: {}", user.getEmail());
        
        // Auto-login after registration
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                registerRequest.getEmail(),
                registerRequest.getPassword()
            )
        );
        
        String token = tokenProvider.generateToken(authentication);
        
        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .department(user.getDepartment())
                .build();
    }

    /**
     * Get current authenticated user
     */
    public UserPrincipal getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        return (UserPrincipal) authentication.getPrincipal();
    }
}