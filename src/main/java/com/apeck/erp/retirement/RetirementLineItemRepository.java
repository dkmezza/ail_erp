package com.apeck.erp.retirement;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Retirement Line Item Repository
 * Data access layer for retirement line items
 */
@Repository
public interface RetirementLineItemRepository extends JpaRepository<RetirementLineItem, Long> {

    /**
     * Find all line items for a retirement
     */
    List<RetirementLineItem> findByRetirementId(Long retirementId);
}