package com.subscriptionbox.ui.controllers;

import com.subscriptionbox.models.Product;
import com.subscriptionbox.services.ProductService;
import com.subscriptionbox.services.ActivityService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.logging.Logger;
import java.util.logging.Level;

public class ProductDialogController {
    private static final Logger LOGGER = Logger.getLogger(ProductDialogController.class.getName());
    
    @FXML
    private TextField nameField;
    @FXML
    private TextField categoryField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private Spinner<Integer> stockSpinner;
    @FXML
    private TextField priceField;

    private Product product;
    private final ProductService productService = new ProductService();
    private final ActivityService activityService = new ActivityService();

    public void setProduct(Product product) {
        this.product = product;
        if (product != null) {
            nameField.setText(product.getProductName());
            categoryField.setText(product.getCategory());
            descriptionField.setText(product.getProductDescription());
            stockSpinner.getValueFactory().setValue(product.getStockQuantity());
            priceField.setText(String.valueOf(product.getPrice()));
        }
    }

    @FXML
    private void handleSave() {
        if (product == null) {
            product = new Product();
        }

        try {
            product.setProductName(nameField.getText());
            product.setCategory(categoryField.getText());
            product.setProductDescription(descriptionField.getText());
            product.setStockQuantity(stockSpinner.getValue());
            product.setPrice(Double.parseDouble(priceField.getText()));

            if (product.getProductId() == 0) {
                productService.createProduct(product);
                LOGGER.info("Creating new product: " + product.getProductName());
                activityService.logActivity("New product added: " + product.getProductName(), "PRODUCT");
            } else {
                productService.updateProduct(product);
                LOGGER.info("Updating product: " + product.getProductName());
                activityService.logActivity("Product updated: " + product.getProductName(), "PRODUCT");
            }
            closeDialog();
        } catch (NumberFormatException e) {
            showError("Invalid Input", "Please enter a valid price");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving product", e);
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

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 