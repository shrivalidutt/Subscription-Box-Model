package com.subscriptionbox.ui.validation;

import com.subscriptionbox.models.Product;

public class ProductValidator implements InputValidator {
    private final Product product;
    private String errorMessage;

    public ProductValidator(Product product) {
        this.product = product;
    }

    @Override
    public boolean isValid() {
        if (InputValidator.isNullOrEmpty(product.getProductName())) {
            errorMessage = "Product name is required.";
            return false;
        }

        if (InputValidator.isNullOrEmpty(product.getCategory())) {
            errorMessage = "Category is required.";
            return false;
        }

        if (InputValidator.isNullOrEmpty(product.getProductDescription())) {
            errorMessage = "Description is required.";
            return false;
        }

        if (product.getStockQuantity() < 0) {
            errorMessage = "Stock quantity cannot be negative.";
            return false;
        }

        if (product.getPrice() <= 0) {
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