package com.subscriptionbox.ui.controllers.base;

import com.subscriptionbox.ui.controllers.interfaces.DialogController;
import com.subscriptionbox.services.ActivityService;
import javafx.scene.control.Alert;
import javafx.scene.control.Control;
import javafx.stage.Stage;
import java.util.logging.Logger;
import java.util.logging.Level;

public abstract class BaseDialogController implements DialogController {
    protected final Logger LOGGER = Logger.getLogger(getClass().getName());
    protected final ActivityService activityService = new ActivityService();

    @Override
    public void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    protected void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void closeDialog() {
        Stage stage = (Stage) getDialogControl().getScene().getWindow();
        stage.close();
    }

    protected abstract Control getDialogControl();

    protected boolean validateTextField(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            showError("Validation Error", fieldName + " is required.");
            return false;
        }
        return true;
    }

    protected boolean validateNumericField(String value, String fieldName) {
        if (!validateTextField(value, fieldName)) {
            return false;
        }
        try {
            Double.parseDouble(value.trim());
            return true;
        } catch (NumberFormatException e) {
            showError("Validation Error", fieldName + " must be a valid number.");
            return false;
        }
    }

    protected boolean validateIntegerField(String value, String fieldName) {
        if (!validateTextField(value, fieldName)) {
            return false;
        }
        try {
            Integer.parseInt(value.trim());
            return true;
        } catch (NumberFormatException e) {
            showError("Validation Error", fieldName + " must be a valid integer.");
            return false;
        }
    }

    protected void logActivity(String description, String type) {
        try {
            LOGGER.info("Logging activity: " + description);
            activityService.logActivity(description, type);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to log activity: " + description, e);
        }
    }
} 