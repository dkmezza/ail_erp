package com.apeck.erp.notification;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apeck.erp.notification.dto.NotificationResponse;
import com.apeck.erp.user.User;
import com.apeck.erp.user.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Transactional
    public void createRequisitionApprovedNotification(Long userId, String requisitionNumber, BigDecimal approvedAmount, Long requisitionId) {
        try {
            // Create in-app notification
            Notification notification = Notification.builder()
                    .userId(userId)
                    .title("Requisition Approved")
                    .message(String.format("Your requisition %s has been approved for TZS %,.2f", requisitionNumber, approvedAmount))
                    .type(NotificationType.REQUISITION_APPROVED)
                    .relatedEntityId(requisitionId)
                    .isRead(false)
                    .build();
            
            notificationRepository.save(notification);
            log.info("Created REQUISITION_APPROVED notification for user: {}", userId);

            // Send email
            User user = userRepository.findById(userId).orElse(null);
            if (user != null && user.getEmail() != null) {
                String emailBody = emailService.buildRequisitionApprovedEmail(requisitionNumber, approvedAmount.doubleValue());
                emailService.sendEmail(user.getEmail(), "Requisition Approved - " + requisitionNumber, emailBody);
            } else {
                log.warn("User {} not found or has no email, skipping email notification", userId);
            }
        } catch (Exception e) {
            log.error("Failed to create REQUISITION_APPROVED notification for user {}: {}", userId, e.getMessage(), e);
            // Don't throw - we don't want to fail the requisition approval if notification fails
        }
    }

    @Transactional
    public void createRequisitionRejectedNotification(Long userId, String requisitionNumber, String reason, Long requisitionId) {
        try {
            // Create in-app notification
            Notification notification = Notification.builder()
                    .userId(userId)
                    .title("Requisition Rejected")
                    .message(String.format("Your requisition %s has been rejected. Reason: %s", requisitionNumber, reason))
                    .type(NotificationType.REQUISITION_REJECTED)
                    .relatedEntityId(requisitionId)
                    .isRead(false)
                    .build();
            
            notificationRepository.save(notification);
            log.info("Created REQUISITION_REJECTED notification for user: {}", userId);

            // Send email
            User user = userRepository.findById(userId).orElse(null);
            if (user != null && user.getEmail() != null) {
                String emailBody = emailService.buildRequisitionRejectedEmail(requisitionNumber, reason);
                emailService.sendEmail(user.getEmail(), "Requisition Rejected - " + requisitionNumber, emailBody);
            }
        } catch (Exception e) {
            log.error("Failed to create REQUISITION_REJECTED notification for user {}: {}", userId, e.getMessage(), e);
        }
    }

    @Transactional
    public void createRequisitionDisbursedNotification(Long userId, String requisitionNumber, BigDecimal amount, String paymentMethod, Long requisitionId) {
        try {
            // Create in-app notification
            Notification notification = Notification.builder()
                    .userId(userId)
                    .title("Funds Disbursed")
                    .message(String.format("TZS %,.2f has been disbursed for requisition %s via %s", amount, requisitionNumber, paymentMethod))
                    .type(NotificationType.REQUISITION_DISBURSED)
                    .relatedEntityId(requisitionId)
                    .isRead(false)
                    .build();
            
            notificationRepository.save(notification);
            log.info("Created REQUISITION_DISBURSED notification for user: {}", userId);

            // Send email
            User user = userRepository.findById(userId).orElse(null);
            if (user != null && user.getEmail() != null) {
                String emailBody = emailService.buildRequisitionDisbursedEmail(requisitionNumber, amount.doubleValue(), paymentMethod);
                emailService.sendEmail(user.getEmail(), "Funds Disbursed - " + requisitionNumber, emailBody);
            }
        } catch (Exception e) {
            log.error("Failed to create REQUISITION_DISBURSED notification for user {}: {}", userId, e.getMessage(), e);
        }
    }

    @Transactional
    public void createRetirementApprovedNotification(Long userId, String retirementNumber, Long retirementId) {
        try {
            // Create in-app notification
            Notification notification = Notification.builder()
                    .userId(userId)
                    .title("Retirement Approved")
                    .message(String.format("Your expenditure retirement %s has been approved", retirementNumber))
                    .type(NotificationType.RETIREMENT_APPROVED)
                    .relatedEntityId(retirementId)
                    .isRead(false)  // ✅ ADD THIS LINE
                    .build();
            
            notificationRepository.save(notification);
            log.info("Created RETIREMENT_APPROVED notification for user: {}", userId);

            // Send email
            User user = userRepository.findById(userId).orElse(null);
            if (user != null && user.getEmail() != null) {
                String emailBody = emailService.buildRetirementApprovedEmail(retirementNumber);
                emailService.sendEmail(user.getEmail(), "Retirement Approved - " + retirementNumber, emailBody);
            }
        } catch (Exception e) {
            log.error("Failed to create RETIREMENT_APPROVED notification for user {}: {}", userId, e.getMessage(), e);
            // Don't throw - we don't want to fail the retirement approval if notification fails
        }
    }

    @Transactional
    public void createRetirementRejectedNotification(Long userId, String retirementNumber, String reason, Long retirementId) {
        try {
            // Create in-app notification
            Notification notification = Notification.builder()
                    .userId(userId)
                    .title("Retirement Rejected")
                    .message(String.format("Your expenditure retirement %s has been rejected. Reason: %s", retirementNumber, reason))
                    .type(NotificationType.RETIREMENT_REJECTED)
                    .relatedEntityId(retirementId)
                    .isRead(false)  // ✅ ADD THIS LINE
                    .build();
            
            notificationRepository.save(notification);
            log.info("Created RETIREMENT_REJECTED notification for user: {}", userId);

            // Send email
            User user = userRepository.findById(userId).orElse(null);
            if (user != null && user.getEmail() != null) {
                String emailBody = emailService.buildRetirementRejectedEmail(retirementNumber, reason);
                emailService.sendEmail(user.getEmail(), "Retirement Rejected - " + retirementNumber, emailBody);
            }
        } catch (Exception e) {
            log.error("Failed to create RETIREMENT_REJECTED notification for user {}: {}", userId, e.getMessage(), e);
            // Don't throw - we don't want to fail the retirement rejection if notification fails
        }
    }


    public List<NotificationResponse> getUserNotifications(Long userId) {
        log.info("Fetching notifications for user: {}", userId);
        
        try {
            List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
            log.info("Found {} notifications for user {}", notifications.size(), userId);
            
            return notifications.stream()
                    .map(notification -> {
                        log.debug("Mapping notification ID: {}, Title: {}, isRead: {}", 
                            notification.getId(), 
                            notification.getTitle(), 
                            notification.getIsRead());
                        return toResponse(notification);
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching notifications for user {}: {}", userId, e.getMessage(), e);
            throw e;
        }
    }

    public List<NotificationResponse> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    @Transactional
    public void markAsRead(Long notificationId, Long userId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        
        if (!notification.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized to mark this notification as read");
        }
        
        notification.setIsRead(true);
        notificationRepository.save(notification);
        log.info("Marked notification {} as read for user {}", notificationId, userId);
    }

    @Transactional
    public void markAllAsRead(Long userId) {
        List<Notification> unreadNotifications = notificationRepository
                .findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);
        
        unreadNotifications.forEach(n -> n.setIsRead(true));
        notificationRepository.saveAll(unreadNotifications);
        log.info("Marked all notifications as read for user {}", userId);
    }

    private NotificationResponse toResponse(Notification notification) {
    return NotificationResponse.builder()
            .id(notification.getId())
            .title(notification.getTitle())
            .message(notification.getMessage())
            .type(notification.getType())
            .isRead(notification.getIsRead() != null ? notification.getIsRead() : false)  // ✅ Handle null
            .createdAt(notification.getCreatedAt())
            .relatedEntityId(notification.getRelatedEntityId())
            .build();
}
}
