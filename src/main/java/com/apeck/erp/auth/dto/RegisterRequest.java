package com.apeck.erp.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Register Request DTO
 * For creating new user accounts (admin only)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 255, message = "Name must be between 2 and 255 characters")
    private String name;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;
    
    @Pattern(regexp = "^\\+?[0-9]{10,20}$", message = "Phone number must be valid")
    private String phone;
    
    @NotBlank(message = "Role is required")
    @Pattern(regexp = "^(field_staff|approver|finance|admin)$", 
             message = "Role must be one of: field_staff, approver, finance, admin")
    private String role;
    
    @Size(max = 100, message = "Department must not exceed 100 characters")
    private String department;
    
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
}
