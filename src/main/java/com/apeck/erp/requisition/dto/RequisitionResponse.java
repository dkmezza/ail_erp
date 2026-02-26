package com.apeck.erp.requisition.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Requisition Response DTO
 * Returns requisition data to clients
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequisitionResponse {
    
    private Long id;
    private String requisitionNumber;
    private LocalDate date;
    private BigDecimal amountRequested;
    private String department;
    private String description;
    
    // Requested by
    private Long requestedById;
    private String requestedByName;
    private String requestedByEmail;
    
    // Status and workflow
    private String status;
    
    // Approval details
    private Long approvedById;
    private String approvedByName;
    private LocalDateTime approvedAt;
    private BigDecimal approvedAmount;
    
    // Disbursement details
    private Long disbursedById;
    private String disbursedByName;
    private LocalDateTime disbursedAt;
    private String paymentMethod;
    private String paymentReference;
    
    // Rejection details
    private String rejectionReason;
    
    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}