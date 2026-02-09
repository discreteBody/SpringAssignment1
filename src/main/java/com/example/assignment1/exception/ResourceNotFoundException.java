package com.example.assignment1.exception;

// ======================== DO NOT MODIFY THIS FILE ========================

/**
 * Thrown when a requested resource (Restaurant, MenuItem, Menu) is not found.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
