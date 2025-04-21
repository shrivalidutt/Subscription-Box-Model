package com.subscriptionbox.dao;

import com.subscriptionbox.DatabaseConnection;
import com.subscriptionbox.models.Subscription;
import java.sql.*;
import java.util.*;
import java.time.LocalDate;
import java.util.logging.Logger;
import java.util.logging.Level;

public class SubscriptionDAO {
    private static final Logger LOGGER = Logger.getLogger(SubscriptionDAO.class.getName());

    public List<Subscription> getAllSubscriptions() throws SQLException {
        List<Subscription> subscriptions = new ArrayList<>();
        String query = "SELECT * FROM subscriptions WHERE Status != 'Deleted' AND Subscription_Status != 'Deleted'";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                subscriptions.add(mapResultSetToSubscription(rs));
            }
        }
        return subscriptions;
    }

    public Subscription getSubscription(int subscriptionId) throws SQLException {
        String query = "SELECT * FROM subscriptions WHERE Subscription_ID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, subscriptionId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSubscription(rs);
                }
            }
        }
        return null;
    }

    public void addSubscription(Subscription subscription) throws SQLException {
        String query = "INSERT INTO subscriptions (User_ID, Product_ID, Subscription_Type_ID, Start_Date, End_Date, Expiration_Date, Status, Subscription_Status, Frequency, Price) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            setSubscriptionParameters(pstmt, subscription);
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    subscription.setSubscriptionId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public boolean updateSubscription(Subscription subscription) throws SQLException {
        String query = "UPDATE subscriptions SET User_ID = ?, Product_ID = ?, Subscription_Type_ID = ?, Start_Date = ?, End_Date = ?, Expiration_Date = ?, Status = ?, Subscription_Status = ?, Frequency = ?, Price = ? WHERE Subscription_ID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            setSubscriptionParameters(pstmt, subscription);
            pstmt.setInt(11, subscription.getSubscriptionId());

            int rowsAffected = pstmt.executeUpdate();
            LOGGER.info("Rows affected by update: " + rowsAffected);
            return rowsAffected > 0;
        }
    }

    public boolean deleteSubscription(int subscriptionId) throws SQLException {
        String query = "UPDATE subscriptions SET Status = 'Deleted', Subscription_Status = 'Deleted' WHERE Subscription_ID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, subscriptionId);
            int rowsAffected = pstmt.executeUpdate();
            LOGGER.info("Rows affected by soft delete: " + rowsAffected);
            return rowsAffected > 0;
        }
    }

    private Subscription mapResultSetToSubscription(ResultSet rs) throws SQLException {
        Subscription subscription = new Subscription();
        subscription.setSubscriptionId(rs.getInt("Subscription_ID"));
        subscription.setUserId(rs.getInt("User_ID"));
        subscription.setProductId(rs.getInt("Product_ID"));
        subscription.setSubscriptionTypeId(rs.getInt("Subscription_Type_ID"));
        
        java.sql.Date startDate = rs.getDate("Start_Date");
        if (startDate != null) {
            subscription.setStartDate(startDate.toLocalDate());
        }
        
        java.sql.Date endDate = rs.getDate("End_Date");
        if (endDate != null) {
            subscription.setEndDate(endDate.toLocalDate());
        }
        
        java.sql.Date expirationDate = rs.getDate("Expiration_Date");
        if (expirationDate != null) {
            subscription.setExpirationDate(expirationDate.toLocalDate());
        }
        
        subscription.setStatus(rs.getString("Status"));
        subscription.setSubscriptionStatus(rs.getString("Subscription_Status"));
        subscription.setFrequency(rs.getString("Frequency"));
        subscription.setPrice(rs.getDouble("Price"));
        
        return subscription;
    }

    private void setSubscriptionParameters(PreparedStatement pstmt, Subscription subscription) throws SQLException {
        pstmt.setInt(1, subscription.getUserId());
        pstmt.setInt(2, subscription.getProductId());
        pstmt.setInt(3, subscription.getSubscriptionTypeId());
        
        LocalDate startDate = subscription.getStartDate();
        pstmt.setDate(4, startDate != null ? java.sql.Date.valueOf(startDate) : null);
        
        LocalDate endDate = subscription.getEndDate();
        pstmt.setDate(5, endDate != null ? java.sql.Date.valueOf(endDate) : null);
        
        LocalDate expirationDate = subscription.getExpirationDate();
        pstmt.setDate(6, expirationDate != null ? java.sql.Date.valueOf(expirationDate) : null);
        
        pstmt.setString(7, subscription.getStatus());
        pstmt.setString(8, subscription.getSubscriptionStatus());
        pstmt.setString(9, subscription.getFrequency());
        pstmt.setDouble(10, subscription.getPrice());
    }
}

