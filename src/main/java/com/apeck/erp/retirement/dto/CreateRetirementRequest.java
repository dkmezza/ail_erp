package com.apeck.erp.retirement.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Create Retirement Request DTO
 * Used when field staff submits an expenditure retirement
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRetirementRequest {
    
    @NotNull(message = "Requisition ID is required")
    private Long requisitionId;
    
    @NotBlank(message = "Employee name is required")
    @Size(max = 255, message = "Employee name must not exceed 255 characters")
    private String employeeName;
    
    @Size(max = 100, message = "Employee title must not exceed 100 characters")
    private String employeeTitle;
    
    @NotNull(message = "Amount received is required")
    @DecimalMin(value = "0.01", message = "Amount received must be greater than zero")
    @Digits(integer = 15, fraction = 2)
    private BigDecimal amountReceived;
    
    @NotNull(message = "Line items are required")
    @Size(min = 1, message = "At least one line item is required")
    @Valid
    private List<RetirementLineItemRequest> lineItems;
    
    // Getters for Lombok compatibility
    public Long getRequisitionId() {
        return requisitionId;
    }
    
    public String getEmployeeName() {
        return employeeName;
    }
    
    public String getEmployeeTitle() {
        return employeeTitle;
    }
    
    public BigDecimal getAmountReceived() {
        return amountReceived;
    }
    
    public List<RetirementLineItemRequest> getLineItems() {
        return lineItems;
    }
}
