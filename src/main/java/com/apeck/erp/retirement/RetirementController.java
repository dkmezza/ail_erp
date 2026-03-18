package com.apeck.erp.retirement;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.apeck.erp.retirement.dto.CreateRetirementRequest;
import com.apeck.erp.retirement.dto.RetirementResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Retirement Controller
 * Handles expenditure retirement REST endpoints
 */
@RestController
@RequestMapping("/api/retirements")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Retirements", description = "Expenditure retirement management endpoints")
public class RetirementController {

    private final RetirementService retirementService;

    /**
     * Create retirement WITHOUT files (for testing)
     * POST /api/retirements/simple
     */
    @PostMapping("/simple")
    @PreAuthorize("hasAnyRole('FIELD_STAFF', 'ADMIN')")
    @Operation(
        summary = "Create retirement without files",
        description = "Submit an expenditure retirement with line items (no file upload)"
    )
    public ResponseEntity<RetirementResponse> createRetirementSimple(
            @Valid @RequestBody CreateRetirementRequest request) {
        
        RetirementResponse response = retirementService.createRetirement(request, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Create retirement with receipts
     * POST /api/retirements
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('FIELD_STAFF', 'ADMIN')")
    @Operation(
        summary = "Create retirement",
        description = "Submit an expenditure retirement with line items and receipt attachments"
    )
    public ResponseEntity<RetirementResponse> createRetirement(
            @Valid @RequestPart("retirement") CreateRetirementRequest request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        
        RetirementResponse response = retirementService.createRetirement(request, files);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get all retirements
     * GET /api/retirements
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('FINANCE', 'ADMIN')")
    @Operation(
        summary = "Get all retirements",
        description = "Retrieve all retirements (finance or admin only)"
    )
    public ResponseEntity<List<RetirementResponse>> getAllRetirements() {
        List<RetirementResponse> retirements = retirementService.getAllRetirements();
        return ResponseEntity.ok(retirements);
    }

    /**
     * Get retirement by ID
     * GET /api/retirements/{id}
     */
    @GetMapping("/{id}")
    @Operation(
        summary = "Get retirement by ID",
        description = "Retrieve a specific retirement by ID"
    )
    public ResponseEntity<RetirementResponse> getRetirementById(@PathVariable Long id) {
        RetirementResponse response = retirementService.getRetirementById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Get my retirements
     * GET /api/retirements/my
     */
    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('FIELD_STAFF', 'ADMIN')")
    @Operation(
        summary = "Get my retirements",
        description = "Retrieve retirements submitted by current user"
    )
    public ResponseEntity<List<RetirementResponse>> getMyRetirements() {
        List<RetirementResponse> retirements = retirementService.getMyRetirements();
        return ResponseEntity.ok(retirements);
    }

    /**
     * Get retirements by status
     * GET /api/retirements/status/{status}
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('FINANCE', 'ADMIN')")
    @Operation(
        summary = "Get retirements by status",
        description = "Retrieve retirements filtered by status (PENDING, APPROVED, REJECTED)"
    )
    public ResponseEntity<List<RetirementResponse>> getRetirementsByStatus(@PathVariable String status) {
        List<RetirementResponse> retirements = retirementService.getRetirementsByStatus(status);
        return ResponseEntity.ok(retirements);
    }

    /**
     * Approve retirement
     * PUT /api/retirements/{id}/approve
     */
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('FINANCE', 'ADMIN')")
    @Operation(
        summary = "Approve retirement",
        description = "Approve a pending retirement (finance or admin only)"
    )
    public ResponseEntity<RetirementResponse> approveRetirement(
            @PathVariable Long id,
            @RequestParam(required = false) String financeNotes) {
        
        RetirementResponse response = retirementService.approveRetirement(id, financeNotes);
        return ResponseEntity.ok(response);
    }

    /**
     * Reject retirement
     * PUT /api/retirements/{id}/reject
     */
    @PutMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('FINANCE', 'ADMIN')")
    @Operation(
        summary = "Reject retirement",
        description = "Reject a pending retirement (finance or admin only)"
    )
    public ResponseEntity<RetirementResponse> rejectRetirement(
            @PathVariable Long id,
            @RequestParam(required = false) String financeNotes) {
        
        RetirementResponse response = retirementService.rejectRetirement(id, financeNotes);
        return ResponseEntity.ok(response);
    }
}