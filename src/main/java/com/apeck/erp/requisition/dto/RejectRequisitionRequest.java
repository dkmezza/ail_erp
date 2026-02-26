package com.apeck.erp.requisition.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Reject Requisition Request DTO
 * Used when approver rejects a requisition
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RejectRequisitionRequest {
    
    @NotBlank(message = "Rejection reason is required")
    @Size(min = 10, max = 1000, message = "Rejection reason must be between 10 and 1000 characters")
    private String rejectionReason;
    
    // Explicit getter
    public String getRejectionReason() {
        return rejectionReason;
    }
    
    // Explicit setter
    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
}