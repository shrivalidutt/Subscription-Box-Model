package com.subscriptionbox.dao;

import com.subscriptionbox.models.BasicUser;
import com.subscriptionbox.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BasicUserDAO {

    public BasicUser getBasicUserByUserId(int userId) {
        String query = "SELECT * FROM basic_user WHERE User_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                BasicUser user = new BasicUser();
                user.setBasicUserId(rs.getInt("Basic_UserID"));
                user.setUserId(rs.getInt("User_ID"));
                user.setSubscriptionDate(rs.getString("Subscription_Date"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addBasicUser(BasicUser basicUser) {
        String query = "INSERT INTO basic_user (User_ID, Subscription_Date) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, basicUser.getUserId());
            stmt.setString(2, basicUser.getSubscriptionDate());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

