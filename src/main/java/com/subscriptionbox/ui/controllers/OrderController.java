package com.subscriptionbox.ui.controllers;

import com.subscriptionbox.models.Order;
import com.subscriptionbox.services.OrderService;
import com.subscriptionbox.services.ActivityService;
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
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderController extends BaseController {
    private static final Logger LOGGER = Logger.getLogger(OrderController.class.getName());
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    @FXML private TableView<Order> orderTable;
    @FXML private TableColumn<Order, Integer> orderIdColumn;
    @FXML private TableColumn<Order, Integer> userIdColumn;
    @FXML private TableColumn<Order, Integer> subscriptionIdColumn;
    @FXML private TableColumn<Order, String> orderDateColumn;
    @FXML private TableColumn<Order, String> deliveryStatusColumn;
    @FXML private TableColumn<Order, Double> totalAmountColumn;
    @FXML private TableColumn<Order, String> shippingAddressColumn;
    @FXML private TableColumn<Order, String> feedbackColumn;
    @FXML private TableColumn<Order, Void> actionsColumn;

    private final OrderService orderService = new OrderService();
    private final ActivityService activityService = new ActivityService();
    private final ObservableList<Order> orders = FXCollections.observableArrayList();
    private Timeline autoRefreshTimeline;

    @FXML
    public void initialize() {
        try {
            setupTableColumns();
            refreshOrders();
            setupAutoRefresh();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error initializing OrderController", e);
            showError("Initialization Error", "Failed to initialize the orders view: " + e.getMessage());
        }
    }

    private void setupTableColumns() {
        // Basic columns
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        subscriptionIdColumn.setCellValueFactory(new PropertyValueFactory<>("subscriptionId"));
        
        // Date column with formatting
        orderDateColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getOrderDate() != null) {
                return javafx.beans.binding.Bindings.createStringBinding(
                    () -> cellData.getValue().getOrderDate().format(DATE_FORMATTER)
                );
            }
            return javafx.beans.binding.Bindings.createStringBinding(() -> "");
        });

        // Status columns
        deliveryStatusColumn.setCellValueFactory(new PropertyValueFactory<>("deliveryStatus"));
        
        // Total amount column with currency formatting
        totalAmountColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        totalAmountColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", price));
                }
            }
        });

        // Text columns with wrapping
        shippingAddressColumn.setCellValueFactory(new PropertyValueFactory<>("shippingAddress"));
        shippingAddressColumn.setCellFactory(column -> {
            TableCell<Order, String> cell = new TableCell<>() {
                private final Label label = new Label();
                {
                    label.setWrapText(true);
                    setGraphic(label);
                }
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        label.setText(null);
                    } else {
                        label.setText(item);
                    }
                }
            };
            return cell;
        });

        feedbackColumn.setCellValueFactory(new PropertyValueFactory<>("feedback"));
        feedbackColumn.setCellFactory(column -> {
            TableCell<Order, String> cell = new TableCell<>() {
                private final Label label = new Label();
                {
                    label.setWrapText(true);
                    setGraphic(label);
                }
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        label.setText(null);
                    } else {
                        label.setText(item);
                    }
                }
            };
            return cell;
        });

        // Actions column
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final HBox container = new HBox(5, editButton, deleteButton);

            {
                editButton.setOnAction(event -> {
                    Order order = getTableRow().getItem();
                    if (order != null) {
                        showOrderDialog(order);
                    }
                });
                
                deleteButton.setOnAction(event -> {
                    Order order = getTableRow().getItem();
                    if (order != null) {
                        deleteOrder(order);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : container);
            }
        });
    }

    private void setupAutoRefresh() {
        if (autoRefreshTimeline != null) {
            autoRefreshTimeline.stop();
        }
        autoRefreshTimeline = new Timeline(
            new KeyFrame(Duration.seconds(5), event -> refreshOrders())
        );
        autoRefreshTimeline.setCycleCount(Timeline.INDEFINITE);
        autoRefreshTimeline.play();
    }

    @FXML
    private void refreshOrders() {
        try {
            orders.setAll(orderService.getAllOrders());
            orderTable.setItems(orders);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to refresh orders", e);
            showError("Error", "Failed to refresh orders: " + e.getMessage());
        }
    }

    @FXML
    private void showAddOrderDialog() {
        try {
            showOrderDialog(null);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to show add order dialog", e);
            showError("Error", "Failed to open order dialog: " + e.getMessage());
        }
    }

    private void showOrderDialog(Order order) {
        Stage dialog = createDialog(
            "OrderDialog.fxml",
            order == null ? "Add New Order" : "Edit Order",
            order,
            (controller, data) -> {
                ((OrderDialogController) controller).setOrder((Order) data);
            }
        );
        
        // Add listener for dialog close
        dialog.setOnHidden(event -> {
            refreshOrders(); // Immediate refresh when dialog closes
            if (order == null) {
                activityService.logActivity("New order created", "ORDER");
            } else {
                activityService.logActivity("Order updated: #" + order.getOrderId(), "ORDER");
            }
        });
        
        dialog.showAndWait();
    }

    private void deleteOrder(Order order) {
        if (order == null) {
            showError("Error", "No order selected for deletion");
            return;
        }

        if (showConfirmation("Confirm Delete", 
            "Are you sure you want to delete this order?\n" +
            "This action cannot be undone.\n" +
            "Order ID: " + order.getOrderId())) {
            try {
                if (orderService.deleteOrder(order.getOrderId())) {
                    refreshOrders();
                    showSuccess("Success", "Order deleted successfully");
                    activityService.logActivity("Order deleted: #" + order.getOrderId(), "ORDER");
                } else {
                    showError("Error", "Failed to delete order. Please try again.");
                }
            } catch (Exception e) {
                showError("Error", "Failed to delete order: " + e.getMessage());
            }
        }
    }

    @Override
    public void onClose() {
        if (autoRefreshTimeline != null) {
            autoRefreshTimeline.stop();
        }
    }
} 