package com.subscriptionbox.ui.controllers;

import com.subscriptionbox.models.User;
import com.subscriptionbox.models.Product;
import com.subscriptionbox.models.Subscription;
import com.subscriptionbox.models.Order;
import com.subscriptionbox.models.Activity;
import com.subscriptionbox.services.UserService;
import com.subscriptionbox.services.ProductService;
import com.subscriptionbox.services.SubscriptionService;
import com.subscriptionbox.services.OrderService;
import com.subscriptionbox.services.ActivityService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DashboardController extends BaseController {
    private static final Logger LOGGER = Logger.getLogger(DashboardController.class.getName());

    @FXML
    private Text totalUsersText;
    @FXML
    private Text totalProductsText;
    @FXML
    private Text activeSubscriptionsText;
    @FXML
    private Text totalOrdersText;
    @FXML
    private ListView<String> recentActivityList;

    private final UserService userService = new UserService();
    private final ProductService productService = new ProductService();
    private final SubscriptionService subscriptionService = new SubscriptionService();
    private final OrderService orderService = new OrderService();
    private final ActivityService activityService = new ActivityService();
    private final ObservableList<String> recentActivities = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        updateDashboardMetrics();
        loadRecentActivities();
        setupAutoRefresh();
    }

    private void setupAutoRefresh() {
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(5), event -> {
                updateDashboardMetrics();
                loadRecentActivities();
            })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void updateDashboardMetrics() {
        try {
            // Update user count
            List<User> users = userService.fetchAllUsers();
            totalUsersText.setText(String.valueOf(users.size()));

            // Update product count
            List<Product> products = productService.getAllProducts();
            totalProductsText.setText(String.valueOf(products.size()));

            // Update active subscriptions count
            List<Subscription> subscriptions = subscriptionService.getAllSubscriptions();
            long activeCount = subscriptions.stream()
                    .filter(Subscription::isActive)
                    .count();
            activeSubscriptionsText.setText(String.valueOf(activeCount));

            // Update total orders count
            List<Order> orders = orderService.getAllOrders();
            totalOrdersText.setText(String.valueOf(orders.size()));
        } catch (Exception e) {
            showError("Error", "Failed to update dashboard metrics: " + e.getMessage());
        }
    }

    private void loadRecentActivities() {
        LOGGER.info("Starting to load recent activities");
        recentActivities.clear();
        try {
            List<Activity> activities = activityService.getRecentActivities(10);
            LOGGER.info("Received " + activities.size() + " activities from service");
            
            if (activities.isEmpty()) {
                LOGGER.info("No activities found in database");
                recentActivityList.setItems(FXCollections.observableArrayList("No recent activities"));
                return;
            }
            
            for (Activity activity : activities) {
                LOGGER.info("Adding activity to list: " + activity.toString());
                recentActivities.add(activity.toString());
            }
            LOGGER.info("Setting " + recentActivities.size() + " activities to ListView");
            recentActivityList.setItems(recentActivities);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load recent activities", e);
            showError("Error", "Failed to load recent activities: " + e.getMessage());
            recentActivityList.setItems(FXCollections.observableArrayList("Error loading activities"));
        }
    }

    @FXML
    private void handleAddUser() {
        showDialog("UserDialog.fxml", "Add New User", null, (controller, data) -> {
            ((UserDialogController) controller).setUser(null);
        });
        updateDashboardMetrics();
        loadRecentActivities();
    }

    @FXML
    private void handleAddProduct() {
        showDialog("ProductDialog.fxml", "Add New Product", null, (controller, data) -> {
            ((ProductDialogController) controller).setProduct(null);
        });
        updateDashboardMetrics();
        loadRecentActivities();
    }

    @FXML
    private void handleCreateSubscription() {
        showDialog("SubscriptionDialog.fxml", "Create New Subscription", null, (controller, data) -> {
            ((SubscriptionDialogController) controller).setSubscription(null);
        });
        updateDashboardMetrics();
        loadRecentActivities();
    }

    @Override
    protected void onClose() {
        // Cleanup if needed
    }
} 