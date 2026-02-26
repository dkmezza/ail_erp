package com.apeck.erp.requisition;

import com.apeck.erp.common.exception.BadRequestException;
import com.apeck.erp.common.exception.ResourceNotFoundException;
import com.apeck.erp.common.util.NumberGenerator;
import com.apeck.erp.requisition.dto.*;
import com.apeck.erp.security.UserPrincipal;
import com.apeck.erp.user.User;
import com.apeck.erp.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Requisition Service
 * Handles cash requisition business logic
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RequisitionService {

    private final CashRequisitionRepository requisitionRepository;
    private final UserRepository userRepository;

    /**
     * Create new cash requisition
     */
    @Transactional
    public RequisitionResponse createRequisition(CreateRequisitionRequest request) {
        User currentUser = getCurrentUser();
        
        log.info("Creating requisition for user: {}", currentUser.getEmail());
        
        // Generate requisition number
        Long sequence = requisitionRepository.count() + 1;
        String requisitionNumber = NumberGenerator.generateRequisitionNumber(sequence);
        
        CashRequisition requisition = CashRequisition.builder()
                .requisitionNumber(requisitionNumber)
                .date(request.getDate())
                .amountRequested(request.getAmountRequested())
                .department(request.getDepartment())
                .description(request.getDescription())
                .requestedBy(currentUser)
                .status("PENDING")
                .build();
        
        requisition = requisitionRepository.save(requisition);
        
        log.info("Requisition created: {}", requisitionNumber);
        
        return mapToResponse(requisition);
    }

    /**
     * Get all requisitions with pagination
     */
    @Transactional(readOnly = true)
    public Page<RequisitionResponse> getAllRequisitions(Pageable pageable) {
        return requisitionRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    /**
     * Get requisition by ID
     */
    @Transactional(readOnly = true)
    public RequisitionResponse getRequisitionById(Long id) {
        CashRequisition requisition = findRequisitionById(id);
        return mapToResponse(requisition);
    }

    /**
     * Get requisitions by status
     */
    @Transactional(readOnly = true)
    public List<RequisitionResponse> getRequisitionsByStatus(String status) {
        return requisitionRepository.findByStatus(status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get requisitions for current user
     */
    @Transactional(readOnly = true)
    public List<RequisitionResponse> getMyRequisitions() {
        User currentUser = getCurrentUser();
        return requisitionRepository.findByRequestedBy(currentUser).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get requisitions by department
     */
    @Transactional(readOnly = true)
    public List<RequisitionResponse> getRequisitionsByDepartment(String department) {
        return requisitionRepository.findByDepartment(department).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Approve requisition
     */
    @Transactional
    public RequisitionResponse approveRequisition(Long id, ApproveRequisitionRequest request) {
        User currentUser = getCurrentUser();
        CashRequisition requisition = findRequisitionById(id);
        
        log.info("Approving requisition {} by {}", requisition.getRequisitionNumber(), currentUser.getEmail());
        
        // Validate status
        if (!"PENDING".equals(requisition.getStatus())) {
            throw new BadRequestException("Only pending requisitions can be approved");
        }
        
        // Validate approved amount doesn't exceed requested amount
        if (request.getApprovedAmount().compareTo(requisition.getAmountRequested()) > 0) {
            throw new BadRequestException("Approved amount cannot exceed requested amount");
        }
        
        requisition.setStatus("APPROVED");
        requisition.setApprovedBy(currentUser);
        requisition.setApprovedAt(LocalDateTime.now());
        requisition.setApprovedAmount(request.getApprovedAmount());
        
        requisition = requisitionRepository.save(requisition);
        
        log.info("Requisition approved: {}", requisition.getRequisitionNumber());
        
        return mapToResponse(requisition);
    }

    /**
     * Reject requisition
     */
    @Transactional
    public RequisitionResponse rejectRequisition(Long id, RejectRequisitionRequest request) {
        User currentUser = getCurrentUser();
        CashRequisition requisition = findRequisitionById(id);
        
        log.info("Rejecting requisition {} by {}", requisition.getRequisitionNumber(), currentUser.getEmail());
        
        // Validate status
        if (!"PENDING".equals(requisition.getStatus())) {
            throw new BadRequestException("Only pending requisitions can be rejected");
        }
        
        requisition.setStatus("REJECTED");
        requisition.setApprovedBy(currentUser);
        requisition.setApprovedAt(LocalDateTime.now());
        requisition.setRejectionReason(request.getRejectionReason());
        
        requisition = requisitionRepository.save(requisition);
        
        log.info("Requisition rejected: {}", requisition.getRequisitionNumber());
        
        return mapToResponse(requisition);
    }

    /**
     * Disburse approved requisition
     */
    @Transactional
    public RequisitionResponse disburseRequisition(Long id, DisburseRequisitionRequest request) {
        User currentUser = getCurrentUser();
        CashRequisition requisition = findRequisitionById(id);
        
        log.info("Disbursing requisition {} by {}", requisition.getRequisitionNumber(), currentUser.getEmail());
        
        // Validate status
        if (!"APPROVED".equals(requisition.getStatus())) {
            throw new BadRequestException("Only approved requisitions can be disbursed");
        }
        
        requisition.setStatus("DISBURSED");
        requisition.setDisbursedBy(currentUser);
        requisition.setDisbursedAt(LocalDateTime.now());
        requisition.setPaymentMethod(request.getPaymentMethod());
        requisition.setPaymentReference(request.getPaymentReference());
        
        requisition = requisitionRepository.save(requisition);
        
        log.info("Requisition disbursed: {}", requisition.getRequisitionNumber());
        
        return mapToResponse(requisition);
    }

    /**
     * Helper: Find requisition by ID or throw exception
     */
    private CashRequisition findRequisitionById(Long id) {
        return requisitionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Requisition", "id", id));
    }

    /**
     * Helper: Get current authenticated user
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }

    /**
     * Helper: Map entity to response DTO
     */
    private RequisitionResponse mapToResponse(CashRequisition requisition) {
        RequisitionResponse.RequisitionResponseBuilder builder = RequisitionResponse.builder()
                .id(requisition.getId())
                .requisitionNumber(requisition.getRequisitionNumber())
                .date(requisition.getDate())
                .amountRequested(requisition.getAmountRequested())
                .department(requisition.getDepartment())
                .description(requisition.getDescription())
                .status(requisition.getStatus())
                .createdAt(requisition.getCreatedAt())
                .updatedAt(requisition.getUpdatedAt());
        
        // Requested by
        if (requisition.getRequestedBy() != null) {
            builder.requestedById(requisition.getRequestedBy().getId())
                   .requestedByName(requisition.getRequestedBy().getName())
                   .requestedByEmail(requisition.getRequestedBy().getEmail());
        }
        
        // Approval details
        if (requisition.getApprovedBy() != null) {
            builder.approvedById(requisition.getApprovedBy().getId())
                   .approvedByName(requisition.getApprovedBy().getName())
                   .approvedAt(requisition.getApprovedAt())
                   .approvedAmount(requisition.getApprovedAmount());
        }
        
        // Disbursement details
        if (requisition.getDisbursedBy() != null) {
            builder.disbursedById(requisition.getDisbursedBy().getId())
                   .disbursedByName(requisition.getDisbursedBy().getName())
                   .disbursedAt(requisition.getDisbursedAt())
                   .paymentMethod(requisition.getPaymentMethod())
                   .paymentReference(requisition.getPaymentReference());
        }
        
        // Rejection reason
        builder.rejectionReason(requisition.getRejectionReason());
        
        return builder.build();
    }
}
