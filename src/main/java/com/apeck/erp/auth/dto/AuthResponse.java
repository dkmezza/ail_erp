package com.apeck.erp.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Authentication Response DTO
 * Returned after successful login
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    
    private String token;
    
    @Builder.Default
    private String tokenType = "Bearer";
    
    private Long userId;
    private String name;
    private String email;
    private String role;
    private String department;
}
