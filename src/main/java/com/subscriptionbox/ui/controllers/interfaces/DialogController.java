package com.subscriptionbox.ui.controllers.interfaces;

public interface DialogController {
    void handleSave();
    void handleCancel();
    boolean validateInput();
    void showError(String title, String message);
    void closeDialog();
} 