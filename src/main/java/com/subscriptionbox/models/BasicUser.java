package com.subscriptionbox.models;

public class BasicUser {
    private int basicUserId;
    private int userId;
    private String subscriptionDate;

    // Constructor
    public BasicUser() {}

    public BasicUser(int basicUserId, int userId, String subscriptionDate) {
        this.basicUserId = basicUserId;
        this.userId = userId;
        this.subscriptionDate = subscriptionDate;
    }

    // Getters and Setters
    public int getBasicUserId() {
        return basicUserId;
    }

    public void setBasicUserId(int basicUserId) {
        this.basicUserId = basicUserId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSubscriptionDate() {
        return subscriptionDate;
    }

    public void setSubscriptionDate(String subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
    }

    // Optional toString method
    @Override
    public String toString() {
        return "BasicUser{" +
                "basicUserId=" + basicUserId +
                ", userId=" + userId +
                ", subscriptionDate='" + subscriptionDate + '\'' +
                '}';
    }
}
