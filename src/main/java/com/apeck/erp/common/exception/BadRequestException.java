package com.apeck.erp.common.exception;

/**
 * Bad Request Exception
 * Thrown when request data is invalid
 */
public class BadRequestException extends RuntimeException {
    
    public BadRequestException(String message) {
        super(message);
    }
    
    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}