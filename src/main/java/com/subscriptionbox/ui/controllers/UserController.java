package com.subscriptionbox.ui.controllers;

import com.subscriptionbox.models.User;
import com.subscriptionbox.services.UserService;
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
import javafx.stage.WindowEvent;

public class UserController extends BaseController {
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, Integer> userIdColumn;
    @FXML private TableColumn<User, String> userNameColumn;
    @FXML private TableColumn<User, String> emailColumn;
    @FXML private TableColumn<User, String> phoneColumn;
    @FXML private TableColumn<User, String> addressColumn;
    @FXML private TableColumn<User, Void> actionsColumn;

    private final UserService userService = new UserService();
    private final ActivityService activityService = new ActivityService();
    private final ObservableList<User> users = FXCollections.observableArrayList();
    private Timeline autoRefreshTimeline;

    @FXML
    public void initialize() {
        setupTableColumns();
        refreshUsers();
        setupAutoRefresh();
    }

    private void setupAutoRefresh() {
        autoRefreshTimeline = new Timeline(
            new KeyFrame(Duration.seconds(5), event -> refreshUsers())
        );
        autoRefreshTimeline.setCycleCount(Timeline.INDEFINITE);
        autoRefreshTimeline.play();
    }

    private void setupTableColumns() {
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            {
                editButton.setOnAction(event -> showEditUserDialog(getTableRow().getItem()));
                deleteButton.setOnAction(event -> deleteUser(getTableRow().getItem()));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : new HBox(5, editButton, deleteButton));
            }
        });
    }

    @FXML
    private void refreshUsers() {
        users.setAll(userService.fetchAllUsers());
        userTable.setItems(users);
    }

    @FXML
    private void showAddUserDialog() {
        showUserDialog(null);
    }

    private void showEditUserDialog(User user) {
        showUserDialog(user);
    }

    private void showUserDialog(User user) {
        Stage dialog = createDialog(
            "UserDialog.fxml",
            user == null ? "Add New User" : "Edit User",
            user,
            (controller, data) -> {
                ((UserDialogController) controller).setUser((User) data);
            }
        );
        
        // Add listener for dialog close
        dialog.setOnHidden(event -> {
            refreshUsers(); // Immediate refresh when dialog closes
            if (user == null) {
                activityService.logActivity("New user added", "USER");
            } else {
                activityService.logActivity("User updated: " + user.getUserName(), "USER");
            }
        });
        
        dialog.showAndWait();
    }

    @FXML
    private void deleteSelectedUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            deleteUser(selectedUser);
        } else {
            showError("Error", "Please select a user to delete");
        }
    }

    private void deleteUser(User user) {
        if (user == null) {
            showError("Error", "No user selected for deletion");
            return;
        }

        if (showConfirmation("Confirm Delete", 
            "Are you sure you want to delete this user?\n" +
            "This will also delete all associated orders and subscriptions.\n" +
            "This action cannot be undone.\n" +
            "User ID: " + user.getUserId() + "\n" +
            "Name: " + user.getUserName())) {
            try {
                if (userService.deleteUser(user.getUserId())) {
                    refreshUsers();
                    showSuccess("Success", "User deleted successfully");
                    activityService.logActivity("User deleted: " + user.getUserName(), "USER");
                } else {
                    showError("Error", "Failed to delete user. Please try again.");
                }
            } catch (Exception e) {
                showError("Error", "Failed to delete user: " + e.getMessage());
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