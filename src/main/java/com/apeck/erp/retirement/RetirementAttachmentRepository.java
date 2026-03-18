package com.apeck.erp.retirement;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Retirement Attachment Repository
 * Data access layer for retirement attachments/receipts
 */
@Repository
public interface RetirementAttachmentRepository extends JpaRepository<RetirementAttachment, Long> {

    /**
     * Find all attachments for a retirement
     */
    List<RetirementAttachment> findByRetirementId(Long retirementId);
}