package com.apeck.erp.user.exception;

import com.apeck.erp.common.exception.ResourceNotFoundException;

/**
 * User Not Found Exception
 * Thrown when a user is not found
 */
public class UserNotFoundException extends ResourceNotFoundException {
    
    public UserNotFoundException(Long id) {
        super("User not found with id: " + id);
    }
    
    public UserNotFoundException(String email) {
        super("User not found with email: " + email);
    }
}