package com.subscriptionbox.services;

import com.subscriptionbox.dao.OrderDAO;
import com.subscriptionbox.models.Order;
import com.subscriptionbox.DatabaseConnection;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderService extends BaseService {
    private static final Logger LOGGER = Logger.getLogger(OrderService.class.getName());
    private OrderDAO orderDAO = new OrderDAO();

    public List<Order> getAllOrders() {
        try {
            LOGGER.info("Fetching all orders");
            return orderDAO.getAllOrders();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching orders", e);
            throw new RuntimeException("Failed to fetch orders", e);
        }
    }

    public Order getOrder(int id) {
        try {
            LOGGER.info("Fetching order with ID: " + id);
            return orderDAO.getOrder(id);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching order with ID: " + id, e);
            throw new RuntimeException("Failed to fetch order", e);
        }
    }

    public boolean createOrder(Order order) {
        try {
            LOGGER.info("Creating new order for user: " + order.getUserId());
            orderDAO.addOrder(order);
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating order", e);
            return false;
        }
    }

    public boolean updateOrder(Order order) {
        try {
            LOGGER.info("Updating order with ID: " + order.getOrderId());
            orderDAO.updateOrder(order);
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating order with ID: " + order.getOrderId(), e);
            return false;
        }
    }

    public boolean deleteOrder(int id) {
        try {
            LOGGER.info("Deleting order with ID: " + id);
            orderDAO.deleteOrder(id);
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting order with ID: " + id, e);
            return false;
        }
    }

    public boolean verifyUserExists(int userId) {
        try {
            String query = "SELECT COUNT(*) FROM users WHERE User_ID = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, userId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1) > 0;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error verifying user existence", e);
            return false;
        }
    }

    public boolean verifySubscriptionExists(int subscriptionId) {
        try {
            String query = "SELECT COUNT(*) FROM subscriptions WHERE Subscription_ID = ? AND Status != 'Deleted'";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, subscriptionId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1) > 0;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error verifying subscription existence", e);
            return false;
        }
    }
}

