package com.apeck.erp.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.email.from}")
    private String fromEmail;

    @Value("${app.email.name}")
    private String fromName;

    @Async
    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromName + " <" + fromEmail + ">");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
            log.info("Email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to: {}. Error: {}", to, e.getMessage());
        }
    }

    public String buildRequisitionApprovedEmail(String requisitionNumber, Double approvedAmount) {
        return String.format("""
            Dear Employee,
            
            Your cash requisition %s has been approved.
            
            Approved Amount: TZS %,.2f
            
            The funds will be disbursed shortly.
            
            Best regards,
            APECK Finance Team
            """, requisitionNumber, approvedAmount);
    }

    public String buildRequisitionRejectedEmail(String requisitionNumber, String reason) {
        return String.format("""
            Dear Employee,
            
            Your cash requisition %s has been rejected.
            
            Reason: %s
            
            Please contact the finance department for more information.
            
            Best regards,
            APECK Finance Team
            """, requisitionNumber, reason);
    }

    public String buildRequisitionDisbursedEmail(String requisitionNumber, Double amount, String paymentMethod) {
        return String.format("""
            Dear Employee,
            
            Your cash requisition %s has been disbursed.
            
            Amount: TZS %,.2f
            Payment Method: %s
            
            Please submit your expenditure retirement within 7 days.
            
            Best regards,
            APECK Finance Team
            """, requisitionNumber, amount, paymentMethod);
    }

    public String buildRetirementApprovedEmail(String retirementNumber) {
        return String.format("""
            Dear Employee,
            
            Your expenditure retirement %s has been approved.
            
            All receipts and documentation have been verified.
            
            Best regards,
            APECK Finance Team
            """, retirementNumber);
    }

    public String buildRetirementRejectedEmail(String retirementNumber, String reason) {
        return String.format("""
            Dear Employee,
            
            Your expenditure retirement %s has been rejected.
            
            Reason: %s
            
            Please resubmit with the necessary corrections.
            
            Best regards,
            APECK Finance Team
            """, retirementNumber, reason);
    }
}