package com.apeck.erp.retirement.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Retirement Line Item Request DTO
 * Represents a single expense line item in a retirement
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RetirementLineItemRequest {
    
    @NotNull(message = "Date is required")
    private LocalDate date;
    
    @NotBlank(message = "Description is required")
    @Size(min = 5, max = 1000, message = "Description must be between 5 and 1000 characters")
    private String description;
    
    @NotNull(message = "Cost is required")
    @DecimalMin(value = "0.01", message = "Cost must be greater than zero")
    @Digits(integer = 15, fraction = 2)
    private BigDecimal cost;
    
    // Explicit getters
    public LocalDate getDate() {
        return date;
    }
    
    public String getDescription() {
        return description;
    }
    
    public BigDecimal getCost() {
        return cost;
    }
}