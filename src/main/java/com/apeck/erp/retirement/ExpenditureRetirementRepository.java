package com.apeck.erp.retirement;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apeck.erp.user.User;

/**
 * Expenditure Retirement Repository
 * Data access layer for expenditure retirements
 */
@Repository
public interface ExpenditureRetirementRepository extends JpaRepository<ExpenditureRetirement, Long> {

    /**
     * Find retirements by status
     */
    List<ExpenditureRetirement> findByStatus(String status);

    /**
     * Find retirements by submitted user
     */
    List<ExpenditureRetirement> findBySubmittedBy(User submittedBy);

    /**
     * Find retirement by requisition ID
     */
    Optional<ExpenditureRetirement> findByRequisitionId(Long requisitionId);
}