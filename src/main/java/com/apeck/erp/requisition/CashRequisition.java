package com.apeck.erp.requisition;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.apeck.erp.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Cash Requisition Entity
 * Represents a cash requisition request in the system
 */
@Entity
@Table(name = "cash_requisitions", indexes = {
    @Index(name = "idx_requisitions_status", columnList = "status"),
    @Index(name = "idx_requisitions_requested_by", columnList = "requested_by"),
    @Index(name = "idx_requisitions_date", columnList = "date"),
    @Index(name = "idx_requisitions_department", columnList = "department")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashRequisition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "requisition_number", unique = true, nullable = false, length = 50)
    private String requisitionNumber;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "amount_requested", nullable = false, precision = 15, scale = 2)
    private BigDecimal amountRequested;

    @Column(length = 100)
    private String department;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    // Requested by (many-to-one relationship with User)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requested_by", nullable = false)
    private User requestedBy;

    // Status workflow
    @Column(nullable = false, length = 20)
    @Builder.Default
    private String status = "PENDING"; // PENDING, APPROVED, REJECTED, DISBURSED

    // Approval details
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "approved_amount", precision = 15, scale = 2)
    private BigDecimal approvedAmount;

    // Disbursement details
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disbursed_by")
    private User disbursedBy;

    @Column(name = "disbursed_at")
    private LocalDateTime disbursedAt;

    @Column(name = "payment_method", length = 20)
    private String paymentMethod; // MOBILE_MONEY, BANK, CASH, CHEQUE

    @Column(name = "payment_reference", length = 100)
    private String paymentReference;

    // Rejection reason
    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    // Timestamps
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}