package com.apeck.erp.common.util;

import java.math.BigDecimal;
import java.util.regex.Pattern;

import com.apeck.erp.common.exception.BadRequestException;

/**
 * Validation Utility
 * Common validation methods
 */
public class ValidationUtil {
    
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^\\+?[0-9]{10,20}$");
    
    /**
     * Validate email format
     */
    public static void validateEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new BadRequestException("Invalid email format: " + email);
        }
    }
    
    /**
     * Validate phone number
     */
    public static void validatePhone(String phone) {
        if (phone != null && !PHONE_PATTERN.matcher(phone).matches()) {
            throw new BadRequestException("Invalid phone number: " + phone);
        }
    }
    
    /**
     * Validate amount is positive
     */
    public static void validatePositiveAmount(BigDecimal amount, String fieldName) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException(fieldName + " must be greater than zero");
        }
    }
    
    /**
     * Validate string is not blank
     */
    public static void validateNotBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new BadRequestException(fieldName + " is required");
        }
    }
}