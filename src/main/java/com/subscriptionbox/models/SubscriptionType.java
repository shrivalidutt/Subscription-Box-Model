package com.subscriptionbox.models;

public class SubscriptionType {
    private int subscriptionTypeId;
    private String typeName;
    private double price;
    private int duration;

    public SubscriptionType() {}

    public SubscriptionType(int subscriptionTypeId, String typeName, double price, int duration) {
        this.subscriptionTypeId = subscriptionTypeId;
        this.typeName = typeName;
        this.price = price;
        this.duration = duration;
    }

    public int getSubscriptionTypeId() {
        return subscriptionTypeId;
    }

    public void setSubscriptionTypeId(int subscriptionTypeId) {
        this.subscriptionTypeId = subscriptionTypeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "SubscriptionType{" +
                "subscriptionTypeId=" + subscriptionTypeId +
                ", typeName='" + typeName + '\'' +
                ", price=" + price +
                ", duration=" + duration +
                '}';
    }
}
