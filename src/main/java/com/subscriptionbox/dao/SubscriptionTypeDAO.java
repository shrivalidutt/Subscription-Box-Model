package com.subscriptionbox.dao;

import com.subscriptionbox.models.SubscriptionType;
import com.subscriptionbox.DatabaseConnection;
import java.sql.*;
import java.util.*;

public class SubscriptionTypeDAO {
    public List<SubscriptionType> getAllSubscriptionTypes() {
        List<SubscriptionType> types = new ArrayList<>();
        String query = "SELECT * FROM subscription_type";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                SubscriptionType type = new SubscriptionType();
                type.setSubscriptionTypeId(rs.getInt("Subscription_TypeID"));
                type.setTypeName(rs.getString("Type_Name"));
                type.setPrice(rs.getDouble("Price"));
                type.setDuration(rs.getInt("Duration"));
                types.add(type);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return types;
    }

    // Add a new subscription type
    public void addSubscriptionType(SubscriptionType subscriptionType) {
        String query = "INSERT INTO subscription_type (Type_Name, Price, Duration) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, subscriptionType.getTypeName());
            stmt.setDouble(2, subscriptionType.getPrice());
            stmt.setInt(3, subscriptionType.getDuration());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
