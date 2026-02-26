package com.apeck.erp.requisition.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Create Requisition Request DTO
 * Used when field staff creates a new cash requisition
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRequisitionRequest {
    
    @NotNull(message = "Date is required")
    private LocalDate date;
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    @Digits(integer = 15, fraction = 2, message = "Amount must have maximum 15 digits and 2 decimal places")
    private BigDecimal amountRequested;
    
    @Size(max = 100, message = "Department must not exceed 100 characters")
    private String department;
    
    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 5000, message = "Description must be between 10 and 5000 characters")
    private String description;
}
