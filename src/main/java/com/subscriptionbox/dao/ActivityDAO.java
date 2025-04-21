package com.subscriptionbox.dao;

import com.subscriptionbox.DatabaseConnection;
import com.subscriptionbox.models.Activity;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class ActivityDAO {
    private static final Logger LOGGER = Logger.getLogger(ActivityDAO.class.getName());

    public void addActivity(Activity activity) throws SQLException {
        String query = "INSERT INTO activities (description, timestamp, type) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            LOGGER.info("Executing query: " + query);
            LOGGER.info("Parameters: description=" + activity.getDescription() + 
                       ", timestamp=" + activity.getTimestamp() + 
                       ", type=" + activity.getType());
            
            pstmt.setString(1, activity.getDescription());
            pstmt.setTimestamp(2, Timestamp.valueOf(activity.getTimestamp()));
            pstmt.setString(3, activity.getType());
            
            int rowsAffected = pstmt.executeUpdate();
            LOGGER.info("Added activity: " + activity.getDescription() + ", rows affected: " + rowsAffected);
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    activity.setActivityId(generatedKeys.getInt(1));
                    LOGGER.info("Generated activity ID: " + activity.getActivityId());
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding activity", e);
            throw e;
        }
    }

    public List<Activity> getRecentActivities(int limit) throws SQLException {
        List<Activity> activities = new ArrayList<>();
        String query = "SELECT * FROM activities ORDER BY timestamp DESC LIMIT ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            LOGGER.info("Executing query: " + query);
            LOGGER.info("Parameter: limit=" + limit);
            
            pstmt.setInt(1, limit);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Activity activity = new Activity(
                        rs.getString("description"),
                        rs.getString("type")
                    );
                    activity.setActivityId(rs.getInt("activity_id"));
                    activity.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
                    activities.add(activity);
                    LOGGER.info("Retrieved activity: " + activity.toString());
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching recent activities", e);
            throw e;
        }
        return activities;
    }
} 