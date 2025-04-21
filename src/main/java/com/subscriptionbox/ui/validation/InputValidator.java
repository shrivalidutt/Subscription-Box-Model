package com.subscriptionbox.ui.validation;

public interface InputValidator {
    boolean isValid();
    String getErrorMessage();
    
    static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
    
    static boolean isValidInteger(String value) {
        try {
            Integer.parseInt(value.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    static boolean isValidDouble(String value) {
        try {
            Double.parseDouble(value.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
} 