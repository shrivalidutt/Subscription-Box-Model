package com.subscriptionbox.models;

import java.time.LocalDate;

public class Order {
    private int orderId;
    private int userId;
    private int subscriptionId;
    private LocalDate orderDate;
    private String deliveryStatus;
    private String shippingAddress;
    private String feedback;
    private double totalAmount;

    public Order() {
        this.deliveryStatus = "In Transit";
    }

    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(int subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", subscriptionId=" + subscriptionId +
                ", orderDate=" + orderDate +
                ", deliveryStatus='" + deliveryStatus + '\'' +
                ", shippingAddress='" + shippingAddress + '\'' +
                ", feedback='" + feedback + '\'' +
                ", totalAmount=" + totalAmount +
                '}';
    }
}

