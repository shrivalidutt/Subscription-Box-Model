package com.subscriptionbox.ui.controllers;

import com.subscriptionbox.models.Subscription;
import com.subscriptionbox.services.SubscriptionService;
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
import java.util.logging.Level;
import java.util.logging.Logger;

public class SubscriptionController extends BaseController {
    private static final Logger LOGGER = Logger.getLogger(SubscriptionController.class.getName());
    
    @FXML private TableView<Subscription> subscriptionTable;
    @FXML private TableColumn<Subscription, Integer> subscriptionIdColumn;
    @FXML private TableColumn<Subscription, Integer> userIdColumn;
    @FXML private TableColumn<Subscription, Integer> subscriptionTypeIdColumn;
    @FXML private TableColumn<Subscription, String> startDateColumn;
    @FXML private TableColumn<Subscription, String> endDateColumn;
    @FXML private TableColumn<Subscription, String> expirationDateColumn;
    @FXML private TableColumn<Subscription, String> statusColumn;
    @FXML private TableColumn<Subscription, String> subscriptionStatusColumn;
    @FXML private TableColumn<Subscription, String> frequencyColumn;
    @FXML private TableColumn<Subscription, Double> priceColumn;
    @FXML private TableColumn<Subscription, Void> actionsColumn;

    private final SubscriptionService subscriptionService = new SubscriptionService();
    private final ActivityService activityService = new ActivityService();
    private final ObservableList<Subscription> subscriptions = FXCollections.observableArrayList();
    private Timeline autoRefreshTimeline;

    @FXML
    public void initialize() {
        setupTableColumns();
        refreshSubscriptions();
        setupAutoRefresh();
    }

    private void setupTableColumns() {
        subscriptionIdColumn.setCellValueFactory(new PropertyValueFactory<>("subscriptionId"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        subscriptionTypeIdColumn.setCellValueFactory(new PropertyValueFactory<>("subscriptionTypeId"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        expirationDateColumn.setCellValueFactory(new PropertyValueFactory<>("expirationDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        subscriptionStatusColumn.setCellValueFactory(new PropertyValueFactory<>("subscriptionStatus"));
        frequencyColumn.setCellValueFactory(new PropertyValueFactory<>("frequency"));
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
            private final HBox container = new HBox(5, editButton, deleteButton);

            {
                editButton.setOnAction(event -> {
                    Subscription subscription = getTableRow().getItem();
                    if (subscription != null) {
                        showSubscriptionDialog(subscription);
                    }
                });
                
                deleteButton.setOnAction(event -> {
                    Subscription subscription = getTableRow().getItem();
                    if (subscription != null) {
                        deleteSubscription(subscription);
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
            new KeyFrame(Duration.seconds(5), event -> refreshSubscriptions())
        );
        autoRefreshTimeline.setCycleCount(Timeline.INDEFINITE);
        autoRefreshTimeline.play();
    }

    @FXML
    private void refreshSubscriptions() {
        try {
            subscriptions.setAll(subscriptionService.getAllSubscriptions());
            subscriptionTable.setItems(subscriptions);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to refresh subscriptions", e);
            showError("Error", "Failed to refresh subscriptions: " + e.getMessage());
        }
    }

    @FXML
    private void showAddSubscriptionDialog() {
        try {
            showSubscriptionDialog(null);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to show add subscription dialog", e);
            showError("Error", "Failed to open subscription dialog: " + e.getMessage());
        }
    }

    private void showSubscriptionDialog(Subscription subscription) {
        Stage dialog = createDialog(
            "SubscriptionDialog.fxml",
            subscription == null ? "Add New Subscription" : "Edit Subscription",
            subscription,
            (controller, data) -> {
                ((SubscriptionDialogController) controller).setSubscription((Subscription) data);
            }
        );
        
        // Add listener for dialog close
        dialog.setOnHidden(event -> {
            refreshSubscriptions(); // Immediate refresh when dialog closes
            if (subscription == null) {
                activityService.logActivity("New subscription created", "SUBSCRIPTION");
            } else {
                activityService.logActivity("Subscription updated: #" + subscription.getSubscriptionId(), "SUBSCRIPTION");
            }
        });
        
        dialog.showAndWait();
    }

    private void deleteSubscription(Subscription subscription) {
        if (subscription == null) {
            LOGGER.warning("Attempted to delete null subscription");
            return;
        }
        
        if (showConfirmation("Confirm Deactivation", 
            "Are you sure you want to deactivate this subscription?\n" +
            "This will mark the subscription as deleted but preserve order history.\n" +
            "The subscription will no longer appear in the list.")) {
            try {
                if (subscriptionService.deleteSubscription(subscription.getSubscriptionId())) {
                    refreshSubscriptions();
                    activityService.logActivity("Subscription deactivated: #" + subscription.getSubscriptionId(), "SUBSCRIPTION");
                } else {
                    showError("Error", "Failed to deactivate subscription. Please try again.");
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to delete subscription", e);
                showError("Error", "Failed to deactivate subscription: " + e.getMessage());
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