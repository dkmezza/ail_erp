package com.apeck.erp.retirement;

import com.apeck.erp.common.exception.BadRequestException;
import com.apeck.erp.common.exception.ResourceNotFoundException;
import com.apeck.erp.common.util.NumberGenerator;
import com.apeck.erp.requisition.CashRequisition;
import com.apeck.erp.requisition.CashRequisitionRepository;
import com.apeck.erp.retirement.dto.*;
import com.apeck.erp.security.UserPrincipal;
import com.apeck.erp.user.User;
import com.apeck.erp.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Retirement Service
 * Handles expenditure retirement business logic
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RetirementService {

    private final ExpenditureRetirementRepository retirementRepository;
    private final RetirementLineItemRepository lineItemRepository;
    private final RetirementAttachmentRepository attachmentRepository;
    private final CashRequisitionRepository requisitionRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    /**
     * Create new retirement
     */
    @Transactional
    public RetirementResponse createRetirement(CreateRetirementRequest request, List<MultipartFile> files) {
        User currentUser = getCurrentUser();
        
        log.info("Creating retirement for requisition: {}", request.getRequisitionId());
        
        // Validate requisition exists and is disbursed
        CashRequisition requisition = requisitionRepository.findById(request.getRequisitionId())
                .orElseThrow(() -> new ResourceNotFoundException("Requisition", "id", request.getRequisitionId()));
        
        if (!"DISBURSED".equals(requisition.getStatus())) {
            throw new BadRequestException("Only disbursed requisitions can have retirements submitted");
        }
        
        // Check if retirement already exists for this requisition
        if (retirementRepository.findByRequisitionId(request.getRequisitionId()).isPresent()) {
            throw new BadRequestException("Retirement already exists for this requisition");
        }
        
        // Calculate total expensed amount
        BigDecimal amountExpensed = request.getLineItems().stream()
                .map(RetirementLineItemRequest::getCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Generate retirement number
        Long sequence = retirementRepository.count() + 1;
        String retirementNumber = NumberGenerator.generateRetirementNumber(sequence);
        
        // Create retirement
        ExpenditureRetirement retirement = ExpenditureRetirement.builder()
                .retirementNumber(retirementNumber)
                .requisitionId(requisition.getId())
                .employeeName(request.getEmployeeName())
                .employeeTitle(request.getEmployeeTitle())
                .amountReceived(request.getAmountReceived())
                .amountExpensed(amountExpensed)
                .submittedBy(currentUser)
                .status("PENDING")
                .build();
        
        retirement = retirementRepository.save(retirement);
        
        // Create line items
        for (RetirementLineItemRequest itemRequest : request.getLineItems()) {
            RetirementLineItem lineItem = RetirementLineItem.builder()
                    .retirementId(retirement.getId())
                    .date(itemRequest.getDate())
                    .description(itemRequest.getDescription())
                    .cost(itemRequest.getCost())
                    .build();
            
            lineItemRepository.save(lineItem);
        }
        
        // Handle file uploads
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                String filename = fileStorageService.storeFile(file);
                
                RetirementAttachment attachment = RetirementAttachment.builder()
                        .retirementId(retirement.getId())
                        .fileName(file.getOriginalFilename())
                        .filePath(filename)
                        .fileType(file.getContentType())
                        .fileSize(file.getSize())
                        .build();
                
                attachmentRepository.save(attachment);
            }
        }
        
        log.info("Retirement created: {}", retirementNumber);
        
        return mapToResponse(retirement);
    }

    /**
     * Get all retirements
     */
    @Transactional(readOnly = true)
    public List<RetirementResponse> getAllRetirements() {
        return retirementRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get retirement by ID
     */
    @Transactional(readOnly = true)
    public RetirementResponse getRetirementById(Long id) {
        ExpenditureRetirement retirement = findRetirementById(id);
        return mapToResponse(retirement);
    }

    /**
     * Get retirements by status
     */
    @Transactional(readOnly = true)
    public List<RetirementResponse> getRetirementsByStatus(String status) {
        return retirementRepository.findByStatus(status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get my retirements
     */
    @Transactional(readOnly = true)
    public List<RetirementResponse> getMyRetirements() {
        User currentUser = getCurrentUser();
        return retirementRepository.findBySubmittedBy(currentUser).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Approve retirement
     */
    @Transactional
    public RetirementResponse approveRetirement(Long id, String financeNotes) {
        User currentUser = getCurrentUser();
        ExpenditureRetirement retirement = findRetirementById(id);
        
        log.info("Approving retirement {} by {}", retirement.getRetirementNumber(), currentUser.getEmail());
        
        if (!"PENDING".equals(retirement.getStatus())) {
            throw new BadRequestException("Only pending retirements can be approved");
        }
        
        retirement.setStatus("APPROVED");
        retirement.setFinanceApprovedBy(currentUser);
        retirement.setFinanceApprovedAt(LocalDateTime.now());
        retirement.setFinanceNotes(financeNotes);
        
        retirement = retirementRepository.save(retirement);
        
        log.info("Retirement approved: {}", retirement.getRetirementNumber());
        
        return mapToResponse(retirement);
    }

    /**
     * Reject retirement
     */
    @Transactional
    public RetirementResponse rejectRetirement(Long id, String financeNotes) {
        User currentUser = getCurrentUser();
        ExpenditureRetirement retirement = findRetirementById(id);
        
        log.info("Rejecting retirement {} by {}", retirement.getRetirementNumber(), currentUser.getEmail());
        
        if (!"PENDING".equals(retirement.getStatus())) {
            throw new BadRequestException("Only pending retirements can be rejected");
        }
        
        retirement.setStatus("REJECTED");
        retirement.setFinanceApprovedBy(currentUser);
        retirement.setFinanceApprovedAt(LocalDateTime.now());
        retirement.setFinanceNotes(financeNotes);
        
        retirement = retirementRepository.save(retirement);
        
        log.info("Retirement rejected: {}", retirement.getRetirementNumber());
        
        return mapToResponse(retirement);
    }

    /**
     * Helper: Find retirement by ID
     */
    private ExpenditureRetirement findRetirementById(Long id) {
        return retirementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Retirement", "id", id));
    }

    /**
     * Helper: Get current user
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }

    /**
     * Helper: Map to response DTO
     */
    private RetirementResponse mapToResponse(ExpenditureRetirement retirement) {
        // Get requisition
        CashRequisition requisition = requisitionRepository.findById(retirement.getRequisitionId())
                .orElse(null);
        
        // Get line items
        List<RetirementLineItem> lineItems = lineItemRepository.findByRetirementId(retirement.getId());
        List<RetirementResponse.LineItemResponse> lineItemResponses = lineItems.stream()
                .map(item -> RetirementResponse.LineItemResponse.builder()
                        .id(item.getId())
                        .date(item.getDate())
                        .description(item.getDescription())
                        .cost(item.getCost())
                        .build())
                .collect(Collectors.toList());
        
        // Get attachments
        List<RetirementAttachment> attachments = attachmentRepository.findByRetirementId(retirement.getId());
        List<AttachmentResponse> attachmentResponses = attachments.stream()
                .map(att -> AttachmentResponse.builder()
                        .id(att.getId())
                        .fileName(att.getFileName())
                        .fileType(att.getFileType())
                        .fileSize(att.getFileSize())
                        .downloadUrl("/api/retirements/" + retirement.getId() + "/attachments/" + att.getId())
                        .build())
                .collect(Collectors.toList());
        
        RetirementResponse.RetirementResponseBuilder builder = RetirementResponse.builder()
                .id(retirement.getId())
                .retirementNumber(retirement.getRetirementNumber())
                .requisitionId(retirement.getRequisitionId())
                .employeeName(retirement.getEmployeeName())
                .employeeTitle(retirement.getEmployeeTitle())
                .amountReceived(retirement.getAmountReceived())
                .amountExpensed(retirement.getAmountExpensed())
                .amountRemaining(retirement.getAmountReceived().subtract(retirement.getAmountExpensed()))
                .status(retirement.getStatus())
                .lineItems(lineItemResponses)
                .attachments(attachmentResponses)
                .createdAt(retirement.getCreatedAt())
                .updatedAt(retirement.getUpdatedAt());
        
        if (requisition != null) {
            builder.requisitionNumber(requisition.getRequisitionNumber());
        }
        
        if (retirement.getSubmittedBy() != null) {
            builder.submittedById(retirement.getSubmittedBy().getId())
                   .submittedByName(retirement.getSubmittedBy().getName())
                   .submittedAt(retirement.getSubmittedAt());
        }
        
        if (retirement.getFinanceApprovedBy() != null) {
            builder.financeApprovedById(retirement.getFinanceApprovedBy().getId())
                   .financeApprovedByName(retirement.getFinanceApprovedBy().getName())
                   .financeApprovedAt(retirement.getFinanceApprovedAt())
                   .financeNotes(retirement.getFinanceNotes());
        }
        
        return builder.build();
    }
}