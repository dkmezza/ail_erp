package com.apeck.erp.retirement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Retirement Response DTO
 * Returns complete retirement data to clients
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RetirementResponse {
    
    private Long id;
    private String retirementNumber;
    private Long requisitionId;
    private String requisitionNumber;
    
    private String employeeName;
    private String employeeTitle;
    
    private BigDecimal amountReceived;
    private BigDecimal amountExpensed;
    private BigDecimal amountRemaining;
    
    // Submitted by
    private Long submittedById;
    private String submittedByName;
    private LocalDateTime submittedAt;
    
    // Status and workflow
    private String status;
    
    // Approval details
    private Long financeApprovedById;
    private String financeApprovedByName;
    private LocalDateTime financeApprovedAt;
    private String financeNotes;
    
    // Line items and attachments
    private List<LineItemResponse> lineItems;
    private List<AttachmentResponse> attachments;
    
    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LineItemResponse {
        private Long id;
        private LocalDate date;
        private String description;
        private BigDecimal cost;
    }
}