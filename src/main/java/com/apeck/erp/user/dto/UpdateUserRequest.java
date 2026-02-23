package com.apeck.erp.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Update User Request DTO
 * For updating existing users
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    
    @Size(min = 2, max = 255, message = "Name must be between 2 and 255 characters")
    private String name;
    
    @Email(message = "Email must be valid")
    private String email;
    
    @Pattern(regexp = "^\\+?[0-9]{10,20}$", message = "Phone number must be valid")
    private String phone;
    
    @Pattern(regexp = "^(field_staff|approver|finance|admin)$", 
             message = "Role must be one of: field_staff, approver, finance, admin")
    private String role;
    
    @Size(max = 100, message = "Department must not exceed 100 characters")
    private String department;
    
    private Boolean isActive;
}