package com.apeck.erp.common.exception;

/**
 * Base API Exception
 * Parent class for all custom exceptions
 */
public class ApiException extends RuntimeException {
    
    public ApiException(String message) {
        super(message);
    }
    
    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }
}