package com.subscriptionbox.dao;

import com.subscriptionbox.models.PremiumUser;
import com.subscriptionbox.DatabaseConnection;
import java.sql.*;

public class PremiumUserDAO {

    public PremiumUser getPremiumUserByUserId(int userId) {
        String query = "SELECT * FROM premium_user WHERE User_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                PremiumUser user = new PremiumUser();
                user.setPremiumUserId(rs.getInt("Premium_UserID"));
                user.setUserId(rs.getInt("User_ID"));
                user.setMembershipStart(rs.getString("Membership_Start"));
                user.setMembershipExpiration(rs.getString("Membership_Expiration"));
                user.setDiscountPercentage(rs.getDouble("Discount_Percentage"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addPremiumUser(PremiumUser premiumUser) {
        String query = "INSERT INTO premium_user (User_ID, Membership_Start, Membership_Expiration, Discount_Percentage) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, premiumUser.getUserId());
            stmt.setString(2, premiumUser.getMembershipStart());
            stmt.setString(3, premiumUser.getMembershipExpiration());
            stmt.setDouble(4, premiumUser.getDiscountPercentage());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

