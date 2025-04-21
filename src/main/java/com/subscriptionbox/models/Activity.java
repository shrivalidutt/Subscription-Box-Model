package com.subscriptionbox.models;

import java.time.LocalDateTime;

public class Activity {
    private int activityId;
    private String description;
    private LocalDateTime timestamp;
    private String type; // "USER", "ORDER", "SUBSCRIPTION", "PRODUCT"

    public Activity(String description, String type) {
        this.description = description;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", timestamp.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), description);
    }
} 