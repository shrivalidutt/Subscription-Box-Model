package com.subscriptionbox.dao;

import com.subscriptionbox.DatabaseConnection;
import com.subscriptionbox.models.Order;
import java.sql.*;
import java.util.*;
import java.time.LocalDate;
import java.util.logging.Logger;
import java.util.logging.Level;

public class OrderDAO {
    private static final Logger LOGGER = Logger.getLogger(OrderDAO.class.getName());

    public List<Order> getAllOrders() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT Order_ID, User_ID, Subscription_ID, Order_Date, " +
                      "delivery_status as DeliveryStatus, Shipping_Address, Feedback " +
                      "FROM orders";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            LOGGER.info("Executing query: " + query);
            while (rs.next()) {
                orders.add(mapResultSetToOrder(rs));
            }
            LOGGER.info("Retrieved " + orders.size() + " orders");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching orders", e);
            throw e;
        }
        return orders;
    }

    public Order getOrder(int orderId) throws SQLException {
        String query = "SELECT o.Order_ID, o.User_ID, o.Subscription_ID, o.Order_Date, " +
                      "o.delivery_status as DeliveryStatus, o.Shipping_Address, o.Feedback, " +
                      "COALESCE(SUM(op.Price * op.Quantity), 0) as Total_Amount " +
                      "FROM orders o " +
                      "LEFT JOIN order_products op ON o.Order_ID = op.Order_ID " +
                      "GROUP BY o.Order_ID, o.User_ID, o.Subscription_ID, o.Order_Date, " +
                      "o.delivery_status, o.Shipping_Address, o.Feedback";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, orderId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToOrder(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching order with ID: " + orderId, e);
            throw e;
        }
        return null;
    }

    public void addOrder(Order order) throws SQLException {
        String query = "INSERT INTO orders (User_ID, Subscription_ID, Order_Date, delivery_status, Shipping_Address, Feedback) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            setOrderParameters(pstmt, order);
            int rowsAffected = pstmt.executeUpdate();
            LOGGER.info("Rows affected by insert: " + rowsAffected);

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    order.setOrderId(generatedKeys.getInt(1));
                    LOGGER.info("Generated order ID: " + order.getOrderId());
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding order", e);
            throw e;
        }
    }

    public boolean updateOrder(Order order) throws SQLException {
        String query = "UPDATE orders SET User_ID = ?, Subscription_ID = ?, Order_Date = ?, delivery_status = ?, Shipping_Address = ?, Feedback = ? WHERE Order_ID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            setOrderParameters(pstmt, order);
            pstmt.setInt(7, order.getOrderId());

            int rowsAffected = pstmt.executeUpdate();
            LOGGER.info("Rows affected by update: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating order with ID: " + order.getOrderId(), e);
            throw e;
        }
    }

    public boolean deleteOrder(int orderId) throws SQLException {
        String query = "DELETE FROM orders WHERE Order_ID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, orderId);
            int rowsAffected = pstmt.executeUpdate();
            LOGGER.info("Rows affected by delete: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting order with ID: " + orderId, e);
            throw e;
        }
    }

    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        try {
            order.setOrderId(rs.getInt("Order_ID"));
            order.setUserId(rs.getInt("User_ID"));
            order.setSubscriptionId(rs.getInt("Subscription_ID"));
            
            java.sql.Date orderDate = rs.getDate("Order_Date");
            if (orderDate != null) {
                order.setOrderDate(orderDate.toLocalDate());
            }
            
            String deliveryStatus = rs.getString("DeliveryStatus");
            order.setDeliveryStatus(deliveryStatus != null ? deliveryStatus : "Pending");
            
            String shippingAddress = rs.getString("Shipping_Address");
            order.setShippingAddress(shippingAddress != null ? shippingAddress : "");
            
            String feedback = rs.getString("Feedback");
            order.setFeedback(feedback != null ? feedback : "");
            
            order.setTotalAmount(0.0); // Set default total amount
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error mapping ResultSet to Order. Available columns: " + getColumnNames(rs), e);
            throw e;
        }
        return order;
    }

    private String getColumnNames(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        StringBuilder columns = new StringBuilder();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            columns.append(metaData.getColumnName(i)).append(", ");
        }
        return columns.toString();
    }

    private void setOrderParameters(PreparedStatement pstmt, Order order) throws SQLException {
        try {
            pstmt.setInt(1, order.getUserId());
            pstmt.setInt(2, order.getSubscriptionId());
            
            LocalDate orderDate = order.getOrderDate();
            pstmt.setDate(3, orderDate != null ? java.sql.Date.valueOf(orderDate) : null);
            
            String deliveryStatus = order.getDeliveryStatus();
            if (deliveryStatus == null || deliveryStatus.trim().isEmpty()) {
                deliveryStatus = "In Transit";
            } else if (!isValidDeliveryStatus(deliveryStatus)) {
                LOGGER.warning("Invalid delivery status: " + deliveryStatus + ". Using default: In Transit");
                deliveryStatus = "In Transit";
            }
            pstmt.setString(4, deliveryStatus);
            
            pstmt.setString(5, order.getShippingAddress());
            pstmt.setString(6, order.getFeedback());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error setting order parameters", e);
            throw e;
        }
    }

    private boolean isValidDeliveryStatus(String status) {
        if (status == null) return false;
        String[] validStatuses = {"Delivered", "Returned", "In Transit"};
        for (String validStatus : validStatuses) {
            if (validStatus.equals(status)) {
                return true;
            }
        }
        return false;
    }
}
