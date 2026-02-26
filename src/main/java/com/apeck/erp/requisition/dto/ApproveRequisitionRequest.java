package com.apeck.erp.requisition.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Approve Requisition Request DTO
 * Used when approver approves a requisition
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApproveRequisitionRequest {
    
    @NotNull(message = "Approved amount is required")
    @DecimalMin(value = "0.01", message = "Approved amount must be greater than zero")
    @Digits(integer = 15, fraction = 2, message = "Approved amount must have maximum 15 digits and 2 decimal places")
    private BigDecimal approvedAmount;
    
    // Explicit getter (in case Lombok isn't processing)
    public BigDecimal getApprovedAmount() {
        return approvedAmount;
    }
    
    // Explicit setter (in case Lombok isn't processing)
    public void setApprovedAmount(BigDecimal approvedAmount) {
        this.approvedAmount = approvedAmount;
    }
}