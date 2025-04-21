package com.subscriptionbox.ui.controllers;

import com.subscriptionbox.models.Product;
import com.subscriptionbox.services.ProductService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.stage.Stage;

public class ProductController extends BaseController {
    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Integer> productIdColumn;
    @FXML private TableColumn<Product, String> productNameColumn;
    @FXML private TableColumn<Product, String> categoryColumn;
    @FXML private TableColumn<Product, String> descriptionColumn;
    @FXML private TableColumn<Product, Integer> stockColumn;
    @FXML private TableColumn<Product, Double> priceColumn;
    @FXML private TableColumn<Product, Void> actionsColumn;

    private final ProductService productService = new ProductService();
    private final ObservableList<Product> products = FXCollections.observableArrayList();
    private Timeline autoRefreshTimeline;

    @FXML
    public void initialize() {
        setupTableColumns();
        refreshProducts();
        setupAutoRefresh();
    }

    private void setupAutoRefresh() {
        autoRefreshTimeline = new Timeline(
            new KeyFrame(Duration.seconds(5), event -> refreshProducts())
        );
        autoRefreshTimeline.setCycleCount(Timeline.INDEFINITE);
        autoRefreshTimeline.play();
    }

    private void setupTableColumns() {
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("productDescription"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        priceColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                setText(empty || price == null ? null : String.format("$%.2f", price));
            }
        });

        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            {
                editButton.setOnAction(event -> showEditProductDialog(getTableRow().getItem()));
                deleteButton.setOnAction(event -> deleteProduct(getTableRow().getItem()));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : new HBox(5, editButton, deleteButton));
            }
        });
    }

    @FXML
    private void refreshProducts() {
        products.setAll(productService.getAllProducts());
        productTable.setItems(products);
    }

    @FXML
    private void showAddProductDialog() {
        showProductDialog(null);
    }

    private void showEditProductDialog(Product product) {
        showProductDialog(product);
    }

    private void showProductDialog(Product product) {
        Stage dialog = createDialog(
            "ProductDialog.fxml",
            product == null ? "Add New Product" : "Edit Product",
            product,
            (controller, data) -> {
                ((ProductDialogController) controller).setProduct((Product) data);
            }
        );
        
        // Add listener for dialog close
        dialog.setOnHidden(event -> {
            refreshProducts(); // Immediate refresh when dialog closes
        });
        
        dialog.showAndWait();
    }

    @FXML
    private void deleteSelectedProduct() {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            deleteProduct(selectedProduct);
        } else {
            showError("Error", "Please select a product to delete");
        }
    }

    private void deleteProduct(Product product) {
        if (showConfirmation("Confirm Delete", "Are you sure you want to delete this product?")) {
            productService.deleteProduct(product.getProductId());
            refreshProducts();
        }
    }

    @Override
    public void onClose() {
        if (autoRefreshTimeline != null) {
            autoRefreshTimeline.stop();
        }
    }
} 