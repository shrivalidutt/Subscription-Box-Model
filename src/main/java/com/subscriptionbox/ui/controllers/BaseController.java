package com.subscriptionbox.ui.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public abstract class BaseController {
    
    protected Stage createDialog(String fxmlName, String title, Object data, DialogCallback callback) {
        try {
            String fxmlPath = "/com/subscriptionbox/ui/fxml/" + fxmlName;
            URL fxmlUrl = getClass().getResource(fxmlPath);
            if (fxmlUrl == null) {
                throw new IOException("Cannot find FXML file: " + fxmlPath);
            }
            
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            VBox dialogPane = loader.load();
            
            if (callback != null && loader.getController() != null) {
                callback.initializeController(loader.getController(), data);
            }

            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle(title);
            dialog.setScene(new Scene(dialogPane));
            return dialog;
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error", "Could not load dialog: " + e.getMessage());
            return null;
        }
    }

    protected void showDialog(String fxmlName, String title, Object data, DialogCallback callback) {
        Stage dialog = createDialog(fxmlName, title, data, callback);
        if (dialog != null) {
            dialog.showAndWait();
        }
    }

    protected boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    protected void showError(String title, String message) {
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

    /**
     * Called when the controller's view is being closed.
     * Subclasses should override this method to perform cleanup.
     */
    protected void onClose() {
        // Base implementation does nothing
    }

    @FunctionalInterface
    protected interface DialogCallback {
        void initializeController(Object controller, Object data);
    }
} 