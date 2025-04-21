package com.subscriptionbox.models;

public class PremiumUser {
    private int premiumUserId;
    private int userId;
    private String membershipStart;
    private String membershipExpiration;
    private double discountPercentage;

    public PremiumUser() {}

    public PremiumUser(int premiumUserId, int userId, String membershipStart, String membershipExpiration, double discountPercentage) {
        this.premiumUserId = premiumUserId;
        this.userId = userId;
        this.membershipStart = membershipStart;
        this.membershipExpiration = membershipExpiration;
        this.discountPercentage = discountPercentage;
    }

    public int getPremiumUserId() {
        return premiumUserId;
    }

    public void setPremiumUserId(int premiumUserId) {
        this.premiumUserId = premiumUserId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMembershipStart() {
        return membershipStart;
    }

    public void setMembershipStart(String membershipStart) {
        this.membershipStart = membershipStart;
    }

    public String getMembershipExpiration() {
        return membershipExpiration;
    }

    public void setMembershipExpiration(String membershipExpiration) {
        this.membershipExpiration = membershipExpiration;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    @Override
    public String toString() {
        return "PremiumUser{" +
                "premiumUserId=" + premiumUserId +
                ", userId=" + userId +
                ", membershipStart='" + membershipStart + '\'' +
                ", membershipExpiration='" + membershipExpiration + '\'' +
                ", discountPercentage=" + discountPercentage +
                '}';
    }
}
