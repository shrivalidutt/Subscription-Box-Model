package com.subscriptionbox.models;

import java.time.LocalDate;

public class Subscription {
    private int subscriptionId;
    private int userId;
    private int productId;
    private int subscriptionTypeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate expirationDate;
    private String status;
    private String subscriptionStatus;
    private String frequency;
    private double price;

    public Subscription() {
        this.startDate = LocalDate.now();
        this.status = "Active";
        this.subscriptionStatus = "Active";
    }

    public Subscription(int subscriptionId, int userId, int productId, int subscriptionTypeId, 
                       LocalDate startDate, LocalDate endDate, LocalDate expirationDate,
                       String status, String subscriptionStatus, String frequency, double price) {
        this.subscriptionId = subscriptionId;
        this.userId = userId;
        this.productId = productId;
        this.subscriptionTypeId = subscriptionTypeId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.expirationDate = expirationDate;
        this.status = status;
        this.subscriptionStatus = subscriptionStatus;
        this.frequency = frequency;
        this.price = price;
    }

    public int getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(int subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getSubscriptionTypeId() {
        return subscriptionTypeId;
    }

    public void setSubscriptionTypeId(int subscriptionTypeId) {
        this.subscriptionTypeId = subscriptionTypeId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public void setSubscriptionStatus(String subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isActive() {
        return "Active".equalsIgnoreCase(status);
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "subscriptionId=" + subscriptionId +
                ", userId=" + userId +
                ", productId=" + productId +
                ", subscriptionTypeId=" + subscriptionTypeId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", expirationDate=" + expirationDate +
                ", status='" + status + '\'' +
                ", subscriptionStatus='" + subscriptionStatus + '\'' +
                ", frequency='" + frequency + '\'' +
                ", price=" + price +
                '}';
    }
}

