package com.apeck.erp.requisition;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apeck.erp.requisition.dto.ApproveRequisitionRequest;
import com.apeck.erp.requisition.dto.CreateRequisitionRequest;
import com.apeck.erp.requisition.dto.DisburseRequisitionRequest;
import com.apeck.erp.requisition.dto.RejectRequisitionRequest;
import com.apeck.erp.requisition.dto.RequisitionResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Requisition Controller
 * Handles cash requisition REST endpoints
 */
@RestController
@RequestMapping("/api/requisitions")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Requisitions", description = "Cash requisition management endpoints")
public class RequisitionController {

    private final RequisitionService requisitionService;

    /**
     * Create new requisition
     * POST /api/requisitions
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('FIELD_STAFF', 'ADMIN')")
    @Operation(
        summary = "Create requisition",
        description = "Create a new cash requisition (field staff or admin only)"
    )
    public ResponseEntity<RequisitionResponse> createRequisition(
            @Valid @RequestBody CreateRequisitionRequest request) {
        RequisitionResponse response = requisitionService.createRequisition(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get all requisitions with pagination
     * GET /api/requisitions
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('APPROVER', 'FINANCE', 'ADMIN')")
    @Operation(
        summary = "Get all requisitions",
        description = "Retrieve all requisitions with pagination (approver, finance, or admin)"
    )
    public ResponseEntity<Page<RequisitionResponse>> getAllRequisitions(Pageable pageable) {
        Page<RequisitionResponse> requisitions = requisitionService.getAllRequisitions(pageable);
        return ResponseEntity.ok(requisitions);
    }

    /**
     * Get requisition by ID
     * GET /api/requisitions/{id}
     */
    @GetMapping("/{id}")
    @Operation(
        summary = "Get requisition by ID",
        description = "Retrieve a specific requisition by ID"
    )
    public ResponseEntity<RequisitionResponse> getRequisitionById(@PathVariable Long id) {
        RequisitionResponse response = requisitionService.getRequisitionById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Get my requisitions
     * GET /api/requisitions/my
     */
    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('FIELD_STAFF', 'ADMIN')")
    @Operation(
        summary = "Get my requisitions",
        description = "Retrieve requisitions created by current user"
    )
    public ResponseEntity<List<RequisitionResponse>> getMyRequisitions() {
        List<RequisitionResponse> requisitions = requisitionService.getMyRequisitions();
        return ResponseEntity.ok(requisitions);
    }

    /**
     * Get requisitions by status
     * GET /api/requisitions/status/{status}
     */
    @GetMapping("/status/{status}")
    @Operation(
        summary = "Get requisitions by status",
        description = "Retrieve requisitions filtered by status (PENDING, APPROVED, REJECTED, DISBURSED)"
    )
    public ResponseEntity<List<RequisitionResponse>> getRequisitionsByStatus(
            @PathVariable String status) {
        List<RequisitionResponse> requisitions = requisitionService.getRequisitionsByStatus(status);
        return ResponseEntity.ok(requisitions);
    }

    /**
     * Get requisitions by department
     * GET /api/requisitions/department/{department}
     */
    @GetMapping("/department/{department}")
    @PreAuthorize("hasAnyRole('APPROVER', 'FINANCE', 'ADMIN')")
    @Operation(
        summary = "Get requisitions by department",
        description = "Retrieve requisitions filtered by department"
    )
    public ResponseEntity<List<RequisitionResponse>> getRequisitionsByDepartment(
            @PathVariable String department) {
        List<RequisitionResponse> requisitions = requisitionService.getRequisitionsByDepartment(department);
        return ResponseEntity.ok(requisitions);
    }

    /**
     * Approve requisition
     * PUT /api/requisitions/{id}/approve
     */
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('APPROVER', 'ADMIN')")
    @Operation(
        summary = "Approve requisition",
        description = "Approve a pending requisition (approver or admin only)"
    )
    public ResponseEntity<RequisitionResponse> approveRequisition(
            @PathVariable Long id,
            @Valid @RequestBody ApproveRequisitionRequest request) {
        RequisitionResponse response = requisitionService.approveRequisition(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Reject requisition
     * PUT /api/requisitions/{id}/reject
     */
    @PutMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('APPROVER', 'ADMIN')")
    @Operation(
        summary = "Reject requisition",
        description = "Reject a pending requisition (approver or admin only)"
    )
    public ResponseEntity<RequisitionResponse> rejectRequisition(
            @PathVariable Long id,
            @Valid @RequestBody RejectRequisitionRequest request) {
        RequisitionResponse response = requisitionService.rejectRequisition(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Disburse approved requisition
     * PUT /api/requisitions/{id}/disburse
     */
    @PutMapping("/{id}/disburse")
    @PreAuthorize("hasAnyRole('FINANCE', 'ADMIN')")
    @Operation(
        summary = "Disburse requisition",
        description = "Disburse funds for an approved requisition (finance or admin only)"
    )
    public ResponseEntity<RequisitionResponse> disburseRequisition(
            @PathVariable Long id,
            @Valid @RequestBody DisburseRequisitionRequest request) {
        RequisitionResponse response = requisitionService.disburseRequisition(id, request);
        return ResponseEntity.ok(response);
    }
}