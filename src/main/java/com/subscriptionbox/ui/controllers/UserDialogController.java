package com.subscriptionbox.ui.controllers;

import com.subscriptionbox.models.User;
import com.subscriptionbox.services.UserService;
import com.subscriptionbox.services.ActivityService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.logging.Logger;
import java.util.logging.Level;

public class UserDialogController {
    private static final Logger LOGGER = Logger.getLogger(UserDialogController.class.getName());
    
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField addressField;

    private User user;
    private final UserService userService = new UserService();
    private final ActivityService activityService = new ActivityService();

    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            nameField.setText(user.getUserName());
            emailField.setText(user.getEmail());
            phoneField.setText(user.getPhone());
            addressField.setText(user.getAddress());
        }
    }

    @FXML
    private void handleSave() {
        try {
            if (user == null) {
                user = new User();
            }

            user.setUserName(nameField.getText());
            user.setEmail(emailField.getText());
            user.setPhone(phoneField.getText());
            user.setAddress(addressField.getText());

            if (user.getUserId() == 0) {
                userService.createUser(user);
                LOGGER.info("Creating new user: " + user.getUserName());
                activityService.logActivity("New user created: " + user.getUserName(), "USER");
            } else {
                userService.updateUser(user);
                LOGGER.info("Updating user: " + user.getUserName());
                activityService.logActivity("User updated: " + user.getUserName(), "USER");
            }
            closeDialog();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving user", e);
        }
    }

    @FXML
    private void handleCancel() {
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
} 