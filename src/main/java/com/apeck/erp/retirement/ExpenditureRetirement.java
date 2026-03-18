package com.apeck.erp.retirement;

import java.math.BigDecimal;
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
 * Expenditure Retirement Entity
 * Represents an expenditure retirement/accountability report
 */
@Entity
@Table(name = "expenditure_retirements", indexes = {
    @Index(name = "idx_retirements_status", columnList = "status"),
    @Index(name = "idx_retirements_requisition", columnList = "requisition_id"),
    @Index(name = "idx_retirements_submitted_by", columnList = "submitted_by")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenditureRetirement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "retirement_number", unique = true, nullable = false, length = 50)
    private String retirementNumber;

    @Column(name = "requisition_id", unique = true, nullable = false)
    private Long requisitionId;

    @Column(name = "employee_name", nullable = false, length = 255)
    private String employeeName;

    @Column(name = "employee_title", length = 100)
    private String employeeTitle;

    @Column(name = "amount_received", nullable = false, precision = 15, scale = 2)
    private BigDecimal amountReceived;

    @Column(name = "amount_expensed", nullable = false, precision = 15, scale = 2)
    private BigDecimal amountExpensed;

    // Submitted by
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submitted_by", nullable = false)
    private User submittedBy;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    // Status workflow
    @Column(nullable = false, length = 20)
    @Builder.Default
    private String status = "PENDING"; // PENDING, APPROVED, REJECTED

    // Finance approval
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "finance_approved_by")
    private User financeApprovedBy;

    @Column(name = "finance_approved_at")
    private LocalDateTime financeApprovedAt;

    @Column(name = "finance_notes", columnDefinition = "TEXT")
    private String financeNotes;

    // Timestamps
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}