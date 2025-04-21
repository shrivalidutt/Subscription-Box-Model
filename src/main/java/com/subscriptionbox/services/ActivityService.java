package com.subscriptionbox.services;

import com.subscriptionbox.dao.ActivityDAO;
import com.subscriptionbox.models.Activity;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class ActivityService {
    private static final Logger LOGGER = Logger.getLogger(ActivityService.class.getName());
    private final ActivityDAO activityDAO = new ActivityDAO();

    public void logActivity(String description, String type) {
        try {
            LOGGER.info("Attempting to log activity: " + description + " of type: " + type);
            Activity activity = new Activity(description, type);
            activityDAO.addActivity(activity);
            LOGGER.info("Successfully logged activity: " + description);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to log activity: " + description, e);
        }
    }

    public List<Activity> getRecentActivities(int limit) {
        try {
            LOGGER.info("Fetching recent activities with limit: " + limit);
            List<Activity> activities = activityDAO.getRecentActivities(limit);
            LOGGER.info("Retrieved " + activities.size() + " activities");
            for (Activity activity : activities) {
                LOGGER.info("Activity: " + activity.toString());
            }
            return activities;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to fetch recent activities", e);
            return List.of();
        }
    }
} 