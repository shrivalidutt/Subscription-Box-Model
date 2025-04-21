package com.subscriptionbox.ui.controllers;

import com.subscriptionbox.models.Subscription;
import com.subscriptionbox.services.SubscriptionService;
import com.subscriptionbox.services.ActivityService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.logging.Logger;
import java.util.logging.Level;

public class SubscriptionDialogController {
    private static final Logger LOGGER = Logger.getLogger(SubscriptionDialogController.class.getName());
    
    @FXML private TextField userIdField;
    @FXML private TextField productIdField;
    @FXML private TextField subscriptionTypeIdField;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private DatePicker expirationDatePicker;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private ComboBox<String> subscriptionStatusComboBox;
    @FXML private ComboBox<String> frequencyComboBox;
    @FXML private TextField priceField;

    private Subscription subscription;
    private final SubscriptionService subscriptionService = new SubscriptionService();
    private final ActivityService activityService = new ActivityService();

    @FXML
    public void initialize() {
        // Initialize combo boxes
        statusComboBox.getItems().addAll("Active", "Inactive", "Cancelled", "Expired");
        subscriptionStatusComboBox.getItems().addAll("Active", "Inactive", "Cancelled", "Expired");
        frequencyComboBox.getItems().addAll("Monthly", "Quarterly", "Annual");
        
        // Set default values
        LocalDate now = LocalDate.now();
        startDatePicker.setValue(now);
        endDatePicker.setValue(now.plusMonths(1));
        expirationDatePicker.setValue(now.plusMonths(1));
        statusComboBox.setValue("Active");
        subscriptionStatusComboBox.setValue("Active");
        frequencyComboBox.setValue("Monthly");
        priceField.setText("0.00");
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
        if (subscription != null) {
            userIdField.setText(String.valueOf(subscription.getUserId()));
            productIdField.setText(String.valueOf(subscription.getProductId()));
            subscriptionTypeIdField.setText(String.valueOf(subscription.getSubscriptionTypeId()));
            startDatePicker.setValue(subscription.getStartDate());
            endDatePicker.setValue(subscription.getEndDate());
            expirationDatePicker.setValue(subscription.getExpirationDate());
            statusComboBox.setValue(subscription.getStatus());
            subscriptionStatusComboBox.setValue(subscription.getSubscriptionStatus());
            frequencyComboBox.setValue(subscription.getFrequency());
            priceField.setText(String.format("%.2f", subscription.getPrice()));
        }
    }

    @FXML
    private void handleSave() {
        try {
            if (!validateInput()) {
                return;
            }

            if (subscription == null) {
                subscription = new Subscription();
            }

            // Parse and validate User ID
            int userId;
            try {
                userId = Integer.parseInt(userIdField.getText().trim());
                // Verify user exists
                if (!verifyUserExists(userId)) {
                    showError("Invalid Input", "User ID " + userId + " does not exist in the database.");
                    return;
                }
                subscription.setUserId(userId);
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "Invalid user ID input", e);
                showError("Invalid Input", "Please enter a valid numeric value for User ID.");
                return;
            }

            // Parse and validate Product ID
            int productId;
            try {
                productId = Integer.parseInt(productIdField.getText().trim());
                // Verify product exists
                if (!verifyProductExists(productId)) {
                    showError("Invalid Input", "Product ID " + productId + " does not exist in the database.");
                    return;
                }
                subscription.setProductId(productId);
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "Invalid product ID input", e);
                showError("Invalid Input", "Please enter a valid numeric value for Product ID.");
                return;
            }

            // Parse and validate Subscription Type ID
            int subscriptionTypeId;
            try {
                subscriptionTypeId = Integer.parseInt(subscriptionTypeIdField.getText().trim());
                // Verify subscription type exists
                if (!verifySubscriptionTypeExists(subscriptionTypeId)) {
                    showError("Invalid Input", "Subscription Type ID " + subscriptionTypeId + " does not exist in the database.");
                    return;
                }
                subscription.setSubscriptionTypeId(subscriptionTypeId);
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "Invalid subscription type ID input", e);
                showError("Invalid Input", "Please enter a valid numeric value for Subscription Type ID.");
                return;
            }

            try {
                subscription.setPrice(Double.parseDouble(priceField.getText().trim()));
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "Invalid price input", e);
                showError("Invalid Input", "Please enter a valid numeric value for Price.");
                return;
            }

            // Set dates
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            LocalDate expirationDate = expirationDatePicker.getValue();

            if (startDate == null || endDate == null || expirationDate == null) {
                showError("Invalid Input", "Please select all required dates.");
                return;
            }

            subscription.setStartDate(startDate);
            subscription.setEndDate(endDate);
            subscription.setExpirationDate(expirationDate);
            subscription.setStatus(statusComboBox.getValue());
            subscription.setSubscriptionStatus(subscriptionStatusComboBox.getValue());
            subscription.setFrequency(frequencyComboBox.getValue());

            boolean success;
            if (subscription.getSubscriptionId() == 0) {
                subscriptionService.createSubscription(subscription);
                LOGGER.info("Creating new subscription for user: " + subscription.getUserId());
                activityService.logActivity("New subscription created for user: " + subscription.getUserId(), "SUBSCRIPTION");
                success = true;
            } else {
                subscriptionService.updateSubscription(subscription);
                LOGGER.info("Updating subscription: " + subscription.getSubscriptionId());
                activityService.logActivity("Subscription updated: #" + subscription.getSubscriptionId(), "SUBSCRIPTION");
                success = true;
            }

            if (success) {
                closeDialog();
            } else {
                showError("Error", "Failed to save subscription. Please check the logs for details.");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving subscription", e);
            showError("Error", "An error occurred while saving: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        closeDialog();
    }

    private boolean validateInput() {
        if (userIdField.getText().trim().isEmpty()) {
            showError("Validation Error", "Please enter a User ID.");
            return false;
        }
        if (productIdField.getText().trim().isEmpty()) {
            showError("Validation Error", "Please enter a Product ID.");
            return false;
        }
        if (subscriptionTypeIdField.getText().trim().isEmpty()) {
            showError("Validation Error", "Please enter a Subscription Type ID.");
            return false;
        }
        if (startDatePicker.getValue() == null) {
            showError("Validation Error", "Please select a Start Date.");
            return false;
        }
        if (endDatePicker.getValue() == null) {
            showError("Validation Error", "Please select an End Date.");
            return false;
        }
        if (expirationDatePicker.getValue() == null) {
            showError("Validation Error", "Please select an Expiration Date.");
            return false;
        }
        if (statusComboBox.getValue() == null) {
            showError("Validation Error", "Please select a Status.");
            return false;
        }
        if (subscriptionStatusComboBox.getValue() == null) {
            showError("Validation Error", "Please select a Subscription Status.");
            return false;
        }
        if (frequencyComboBox.getValue() == null) {
            showError("Validation Error", "Please select a Frequency.");
            return false;
        }
        if (priceField.getText().trim().isEmpty()) {
            showError("Validation Error", "Please enter a Price.");
            return false;
        }
        return true;
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeDialog() {
        Stage stage = (Stage) userIdField.getScene().getWindow();
        stage.close();
    }

    private boolean verifyUserExists(int userId) {
        try {
            return subscriptionService.verifyUserExists(userId);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error verifying user existence", e);
            return false;
        }
    }

    private boolean verifyProductExists(int productId) {
        try {
            return subscriptionService.verifyProductExists(productId);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error verifying product existence", e);
            return false;
        }
    }

    private boolean verifySubscriptionTypeExists(int subscriptionTypeId) {
        try {
            return subscriptionService.verifySubscriptionTypeExists(subscriptionTypeId);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error verifying subscription type existence", e);
            return false;
        }
    }
} 