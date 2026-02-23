package com.apeck.erp.common.exception;

/**
 * Unauthorized Exception
 * Thrown when user is not authenticated
 */
public class UnauthorizedException extends RuntimeException {
    
    public UnauthorizedException(String message) {
        super(message);
    }
    
    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}