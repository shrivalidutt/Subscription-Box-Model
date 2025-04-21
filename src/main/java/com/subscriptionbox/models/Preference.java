package com.subscriptionbox.models;

public class Preference {
    private int preferenceId;
    private int userId;
    private String category;
    private String dislikes;

    public Preference() {}

    public Preference(int preferenceId, int userId, String category, String dislikes) {
        this.preferenceId = preferenceId;
        this.userId = userId;
        this.category = category;
        this.dislikes = dislikes;
    }

    public int getPreferenceId() {
        return preferenceId;
    }

    public void setPreferenceId(int preferenceId) {
        this.preferenceId = preferenceId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDislikes() {
        return dislikes;
    }

    public void setDislikes(String dislikes) {
        this.dislikes = dislikes;
    }

    @Override
    public String toString() {
        return "Preference{" +
                "preferenceId=" + preferenceId +
                ", userId=" + userId +
                ", category='" + category + '\'' +
                ", dislikes='" + dislikes + '\'' +
                '}';
    }
}

