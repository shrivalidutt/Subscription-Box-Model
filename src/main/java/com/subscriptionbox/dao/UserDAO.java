package com.subscriptionbox.dao;

import com.subscriptionbox.DatabaseConnection;
import com.subscriptionbox.models.User;
import java.sql.*;
import java.util.*;

public class UserDAO {
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("User_ID"));
                user.setUserName(rs.getString("User_Name"));
                user.setEmail(rs.getString("Email"));
                user.setPhone(rs.getString("Phone"));
                user.setAddress(rs.getString("Address"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void addUser(User user) {
        String query = "INSERT INTO users (User_Name, Email, Phone, Address) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, user.getUserName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPhone());
            pstmt.setString(4, user.getAddress());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUser(User user) {
        String query = "UPDATE users SET User_Name = ?, Email = ?, Phone = ?, Address = ? WHERE User_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, user.getUserName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPhone());
            pstmt.setString(4, user.getAddress());
            pstmt.setInt(5, user.getUserId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(int userId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Start transaction
            conn.setAutoCommit(false);

            try {
                // First check if user exists
                String checkUserQuery = "SELECT COUNT(*) FROM users WHERE User_ID = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(checkUserQuery)) {
                    pstmt.setInt(1, userId);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next() && rs.getInt(1) == 0) {
                            throw new SQLException("User not found");
                        }
                    }
                }

                // Delete from orders table first (if any)
                String deleteOrdersQuery = "DELETE FROM orders WHERE User_ID = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(deleteOrdersQuery)) {
                    pstmt.setInt(1, userId);
                    pstmt.executeUpdate();
                }

                // Delete from subscriptions table (if any)
                String deleteSubscriptionsQuery = "DELETE FROM subscriptions WHERE User_ID = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(deleteSubscriptionsQuery)) {
                    pstmt.setInt(1, userId);
                    pstmt.executeUpdate();
                }

                // Delete from basic_user table (if any)
                String deleteBasicUserQuery = "DELETE FROM basic_user WHERE User_ID = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(deleteBasicUserQuery)) {
                    pstmt.setInt(1, userId);
                    pstmt.executeUpdate();
                }

                // Delete from premium_user table (if any)
                String deletePremiumUserQuery = "DELETE FROM premium_user WHERE User_ID = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(deletePremiumUserQuery)) {
                    pstmt.setInt(1, userId);
                    pstmt.executeUpdate();
                }

                // Finally delete from users table
                String deleteUserQuery = "DELETE FROM users WHERE User_ID = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(deleteUserQuery)) {
                    pstmt.setInt(1, userId);
                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected == 0) {
                        throw new SQLException("Failed to delete user");
                    }
                }

                // Commit transaction
                conn.commit();
            } catch (SQLException e) {
                // Rollback transaction on error
                conn.rollback();
                throw e;
            } finally {
                // Reset auto-commit
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete user: " + e.getMessage());
        }
    }
}
