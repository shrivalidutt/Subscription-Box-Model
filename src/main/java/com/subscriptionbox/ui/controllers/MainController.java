package com.subscriptionbox.ui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainController extends BaseController {
    @FXML
    private StackPane contentArea;

    @FXML
    public void initialize() {
        // Load dashboard by default when application starts
        showDashboard();
    }

    @FXML
    private void showDashboard() {
        loadContent("DashboardView.fxml");
    }

    @FXML
    private void showUsers() {
        loadContent("UserView.fxml");
    }

    @FXML
    private void showSubscriptions() {
        loadContent("SubscriptionView.fxml");
    }

    @FXML
    private void showProducts() {
        loadContent("ProductView.fxml");
    }

    @FXML
    private void showOrders() {
        loadContent("OrderView.fxml");
    }

    @FXML
    private void handleExit() {
        Stage stage = (Stage) contentArea.getScene().getWindow();
        stage.close();
    }

    private void loadContent(String fxmlFile) {
        try {
            String fxmlPath = "/com/subscriptionbox/ui/fxml/" + fxmlFile;
            URL fxmlUrl = getClass().getResource(fxmlPath);
            if (fxmlUrl == null) {
                throw new IOException("Cannot find " + fxmlFile);
            }
            
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent content = loader.load();
            contentArea.getChildren().setAll(content);
        } catch (IOException e) {
            showError("Error loading content", "Could not load the requested view: " + e.getMessage());
        }
    }

    @Override
    protected void onClose() {
        // Cleanup if needed
    }
} 