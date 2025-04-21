package com.subscriptionbox.services;

import com.subscriptionbox.dao.ProductDAO;
import com.subscriptionbox.models.Product;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class ProductService extends BaseService {
    private static final Logger LOGGER = Logger.getLogger(ProductService.class.getName());
    private final ProductDAO productDAO = new ProductDAO();

    public List<Product> getAllProducts() {
        try {
            LOGGER.info("Fetching all products");
            return productDAO.getAllProducts();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching products", e);
            throw new RuntimeException("Failed to fetch products", e);
        }
    }

    public boolean createProduct(Product product) {
        try {
            LOGGER.info("Creating new product: " + product.getProductName());
            productDAO.addProduct(product);
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating product", e);
            return false;
        }
    }

    public boolean updateProduct(Product product) {
        try {
            LOGGER.info("Updating product with ID: " + product.getProductId());
            productDAO.updateProduct(product);
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating product", e);
            return false;
        }
    }

    public boolean deleteProduct(int productId) {
        try {
            LOGGER.info("Deleting product with ID: " + productId);
            productDAO.deleteProduct(productId);
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting product", e);
            return false;
        }
    }
}
