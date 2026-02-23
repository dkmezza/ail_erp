package com.apeck.erp.common.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Number Generator Utility
 * Generates unique numbers for requisitions, retirements, etc.
 */
public class NumberGenerator {
    
    private static final DateTimeFormatter YEAR_FORMATTER = DateTimeFormatter.ofPattern("yyyy");
    
    /**
     * Generate requisition number: REQ-2024-0001
     */
    public static String generateRequisitionNumber(Long sequence) {
        String year = LocalDate.now().format(YEAR_FORMATTER);
        return String.format("REQ-%s-%04d", year, sequence);
    }
    
    /**
     * Generate retirement number: RET-2024-0001
     */
    public static String generateRetirementNumber(Long sequence) {
        String year = LocalDate.now().format(YEAR_FORMATTER);
        return String.format("RET-%s-%04d", year, sequence);
    }
    
    /**
     * Generate custom number with prefix
     */
    public static String generateNumber(String prefix, Long sequence) {
        String year = LocalDate.now().format(YEAR_FORMATTER);
        return String.format("%s-%s-%04d", prefix, year, sequence);
    }
}