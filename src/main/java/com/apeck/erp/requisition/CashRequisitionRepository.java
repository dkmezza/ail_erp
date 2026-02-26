package com.apeck.erp.requisition;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apeck.erp.user.User;

/**
 * Cash Requisition Repository
 * Data access layer for cash requisitions
 */
@Repository
public interface CashRequisitionRepository extends JpaRepository<CashRequisition, Long> {

    /**
     * Find requisitions by status
     */
    List<CashRequisition> findByStatus(String status);

    /**
     * Find requisitions by user who requested them
     */
    List<CashRequisition> findByRequestedBy(User requestedBy);

    /**
     * Find requisitions by department
     */
    List<CashRequisition> findByDepartment(String department);

    /**
     * Find requisitions within date range
     */
    List<CashRequisition> findByDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Find requisitions by status and department
     */
    List<CashRequisition> findByStatusAndDepartment(String status, String department);

    /**
     * Find requisitions by status and requested by user
     */
    List<CashRequisition> findByStatusAndRequestedBy(String status, User requestedBy);
}