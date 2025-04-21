package com.subscriptionbox.ui.controllers;

import com.subscriptionbox.models.Order;
import com.subscriptionbox.services.OrderService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.logging.Logger;
import java.util.logging.Level;

public class OrderDialogController extends BaseController {
    private static final Logger LOGGER = Logger.getLogger(OrderDialogController.class.getName());
    
    @FXML private TextField userIdField;
    @FXML private TextField subscriptionIdField;
    @FXML private DatePicker orderDatePicker;
    @FXML private ComboBox<String> deliveryStatusComboBox;
    @FXML private TextArea shippingAddressField;
    @FXML private TextArea feedbackField;
    @FXML private TextField totalAmountField;

    private Order order;
    private final OrderService orderService = new OrderService();

    @FXML
    public void initialize() {
        // Initialize delivery status options with exact ENUM values from database
        deliveryStatusComboBox.getItems().addAll(
            "Delivered",
            "Returned",
            "In Transit"
        );
        
        // Set default values
        orderDatePicker.setValue(LocalDate.now());
        deliveryStatusComboBox.setValue("In Transit");
        totalAmountField.setText("0.00");
        
        // Add numeric validation for total amount
        totalAmountField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*\\.?\\d*")) {
                totalAmountField.setText(oldValue);
            }
        });
    }

    public void setOrder(Order order) {
        this.order = order;
        if (order != null) {
            userIdField.setText(String.valueOf(order.getUserId()));
            subscriptionIdField.setText(String.valueOf(order.getSubscriptionId()));
            orderDatePicker.setValue(order.getOrderDate());
            deliveryStatusComboBox.setValue(order.getDeliveryStatus());
            shippingAddressField.setText(order.getShippingAddress());
            feedbackField.setText(order.getFeedback());
            totalAmountField.setText(String.format("%.2f", order.getTotalAmount()));
        }
    }

    @FXML
    private void handleSave() {
        try {
            if (!validateInput()) {
                return;
            }

            Order updatedOrder = new Order();
            if (order != null) {
                updatedOrder.setOrderId(order.getOrderId());
            }

            // Parse and validate User ID
            try {
                int userId = Integer.parseInt(userIdField.getText().trim());
                if (!orderService.verifyUserExists(userId)) {
                    showError("Invalid Input", "User ID " + userId + " does not exist in the database.");
                    return;
                }
                updatedOrder.setUserId(userId);
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "Invalid user ID input", e);
                showError("Invalid Input", "Please enter a valid numeric value for User ID.");
                return;
            }

            // Parse and validate Subscription ID
            try {
                int subscriptionId = Integer.parseInt(subscriptionIdField.getText().trim());
                if (!orderService.verifySubscriptionExists(subscriptionId)) {
                    showError("Invalid Input", "Subscription ID " + subscriptionId + " does not exist in the database.");
                    return;
                }
                updatedOrder.setSubscriptionId(subscriptionId);
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "Invalid subscription ID input", e);
                showError("Invalid Input", "Please enter a valid numeric value for Subscription ID.");
                return;
            }

            // Set order date
            LocalDate orderDate = orderDatePicker.getValue();
            if (orderDate == null) {
                orderDate = LocalDate.now();
            }
            updatedOrder.setOrderDate(orderDate);

            // Set delivery status
            String deliveryStatus = deliveryStatusComboBox.getValue();
            if (deliveryStatus == null || deliveryStatus.trim().isEmpty()) {
                deliveryStatus = "In Transit";
            }
            updatedOrder.setDeliveryStatus(deliveryStatus);

            // Set shipping address
            String shippingAddress = shippingAddressField.getText();
            if (shippingAddress == null || shippingAddress.trim().isEmpty()) {
                showError("Invalid Input", "Please enter a shipping address.");
                return;
            }
            updatedOrder.setShippingAddress(shippingAddress.trim());

            // Set feedback (optional)
            String feedback = feedbackField.getText();
            updatedOrder.setFeedback(feedback != null ? feedback.trim() : "");

            // Set total amount
            try {
                double totalAmount = Double.parseDouble(totalAmountField.getText().trim());
                if (totalAmount < 0) {
                    totalAmount = 0.0;
                }
                updatedOrder.setTotalAmount(totalAmount);
            } catch (NumberFormatException e) {
                updatedOrder.setTotalAmount(0.0);
            }

            boolean success;
            if (order == null) {
                LOGGER.info("Creating new order: " + updatedOrder);
                success = orderService.createOrder(updatedOrder);
            } else {
                LOGGER.info("Updating order: " + updatedOrder);
                success = orderService.updateOrder(updatedOrder);
            }

            if (success) {
                closeDialog();
            } else {
                showError("Error", "Failed to save order. Please check the logs for details.");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving order", e);
            showError("Error", "An error occurred while saving: " + e.getMessage());
        }
    }

    private boolean validateInput() {
        if (userIdField.getText().trim().isEmpty()) {
            showError("Validation Error", "Please enter a User ID.");
            return false;
        }
        if (subscriptionIdField.getText().trim().isEmpty()) {
            showError("Validation Error", "Please enter a Subscription ID.");
            return false;
        }
        if (orderDatePicker.getValue() == null) {
            showError("Validation Error", "Please select an Order Date.");
            return false;
        }
        if (deliveryStatusComboBox.getValue() == null) {
            showError("Validation Error", "Please select a Delivery Status.");
            return false;
        }
        if (shippingAddressField.getText().trim().isEmpty()) {
            showError("Validation Error", "Please enter a Shipping Address.");
            return false;
        }
        if (totalAmountField.getText().trim().isEmpty()) {
            showError("Validation Error", "Please enter a Total Amount.");
            return false;
        }
        return true;
    }

    @FXML
    private void handleCancel() {
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) userIdField.getScene().getWindow();
        stage.close();
    }
} 