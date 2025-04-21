package com.subscriptionbox.ui.validation;

import com.subscriptionbox.models.Subscription;
import java.time.LocalDate;

public class SubscriptionValidator implements InputValidator {
    private final Subscription subscription;
    private String errorMessage;

    public SubscriptionValidator(Subscription subscription) {
        this.subscription = subscription;
    }

    @Override
    public boolean isValid() {
        if (subscription.getUserId() <= 0) {
            errorMessage = "Valid user ID is required.";
            return false;
        }

        if (subscription.getProductId() <= 0) {
            errorMessage = "Valid product ID is required.";
            return false;
        }

        if (subscription.getSubscriptionTypeId() <= 0) {
            errorMessage = "Valid subscription type ID is required.";
            return false;
        }

        if (subscription.getStartDate() == null) {
            errorMessage = "Start date is required.";
            return false;
        }

        if (subscription.getEndDate() == null) {
            errorMessage = "End date is required.";
            return false;
        }

        if (subscription.getExpirationDate() == null) {
            errorMessage = "Expiration date is required.";
            return false;
        }

        if (subscription.getStartDate().isAfter(subscription.getEndDate())) {
            errorMessage = "Start date cannot be after end date.";
            return false;
        }

        if (subscription.getEndDate().isAfter(subscription.getExpirationDate())) {
            errorMessage = "End date cannot be after expiration date.";
            return false;
        }

        if (InputValidator.isNullOrEmpty(subscription.getStatus())) {
            errorMessage = "Status is required.";
            return false;
        }

        if (InputValidator.isNullOrEmpty(subscription.getSubscriptionStatus())) {
            errorMessage = "Subscription status is required.";
            return false;
        }

        if (InputValidator.isNullOrEmpty(subscription.getFrequency())) {
            errorMessage = "Frequency is required.";
            return false;
        }

        if (subscription.getPrice() <= 0) {
            errorMessage = "Price must be greater than zero.";
            return false;
        }

        return true;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
} 