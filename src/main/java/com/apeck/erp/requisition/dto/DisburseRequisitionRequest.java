package com.apeck.erp.requisition.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Disburse Requisition Request DTO
 * Used when finance disburses approved funds
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DisburseRequisitionRequest {
    
    @NotBlank(message = "Payment method is required")
    @Pattern(regexp = "^(MOBILE_MONEY|BANK|CASH|CHEQUE)$", 
             message = "Payment method must be one of: MOBILE_MONEY, BANK, CASH, CHEQUE")
    private String paymentMethod;
    
    @NotBlank(message = "Payment reference is required")
    @Size(max = 100, message = "Payment reference must not exceed 100 characters")
    private String paymentReference;
    
    // Explicit getters
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public String getPaymentReference() {
        return paymentReference;
    }
    
    // Explicit setters
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }
}