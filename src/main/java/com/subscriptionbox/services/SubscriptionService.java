package com.subscriptionbox.services;

import com.subscriptionbox.dao.SubscriptionDAO;
import com.subscriptionbox.models.Subscription;
import com.subscriptionbox.DatabaseConnection;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SubscriptionService extends BaseService {
    private static final Logger LOGGER = Logger.getLogger(SubscriptionService.class.getName());
    private final SubscriptionDAO subscriptionDAO = new SubscriptionDAO();

    public List<Subscription> getAllSubscriptions() {
        try {
            LOGGER.info("Fetching all subscriptions");
            return subscriptionDAO.getAllSubscriptions();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching subscriptions", e);
            throw new RuntimeException("Failed to fetch subscriptions", e);
        }
    }

    public Subscription getSubscription(int id) {
        try {
            LOGGER.info("Fetching subscription with ID: " + id);
            return subscriptionDAO.getSubscription(id);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching subscription with ID: " + id, e);
            throw new RuntimeException("Failed to fetch subscription", e);
        }
    }

    public boolean createSubscription(Subscription subscription) {
        try {
            if (subscription == null) {
                LOGGER.warning("Attempted to create null subscription");
                return false;
            }
            LOGGER.info("Creating new subscription for user: " + subscription.getUserId());
            subscriptionDAO.addSubscription(subscription);
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating subscription", e);
            throw new RuntimeException("Failed to create subscription", e);
        }
    }

    public boolean updateSubscription(Subscription subscription) {
        try {
            if (subscription == null) {
                LOGGER.warning("Attempted to update null subscription");
                return false;
            }
            if (subscription.getSubscriptionId() <= 0) {
                LOGGER.warning("Attempted to update subscription with invalid ID: " + subscription.getSubscriptionId());
                return false;
            }
            
            LOGGER.info("Updating subscription with ID: " + subscription.getSubscriptionId());
            boolean updated = subscriptionDAO.updateSubscription(subscription);
            
            if (!updated) {
                LOGGER.warning("No subscription found with ID: " + subscription.getSubscriptionId());
            }
            
            return updated;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating subscription with ID: " + subscription.getSubscriptionId(), e);
            throw new RuntimeException("Failed to update subscription", e);
        }
    }

    public boolean deleteSubscription(int id) {
        try {
            if (id <= 0) {
                LOGGER.warning("Attempted to delete subscription with invalid ID: " + id);
                return false;
            }
            
            LOGGER.info("Deleting subscription with ID: " + id);
            boolean deleted = subscriptionDAO.deleteSubscription(id);
            
            if (!deleted) {
                LOGGER.warning("No subscription found with ID: " + id);
            }
            
            return deleted;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting subscription with ID: " + id, e);
            throw new RuntimeException("Failed to delete subscription", e);
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

    public boolean verifyProductExists(int productId) {
        try {
            String query = "SELECT COUNT(*) FROM products WHERE Product_ID = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, productId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1) > 0;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error verifying product existence", e);
            return false;
        }
    }

    public boolean verifySubscriptionTypeExists(int subscriptionTypeId) {
        try {
            String query = "SELECT COUNT(*) FROM subscription_types WHERE Subscription_Type_ID = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, subscriptionTypeId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1) > 0;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error verifying subscription type existence", e);
            return false;
        }
    }
}
