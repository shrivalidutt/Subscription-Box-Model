package com.subscriptionbox.dao;

import com.subscriptionbox.models.Preference;
import com.subscriptionbox.DatabaseConnection;
import java.sql.*;
import java.util.*;

public class PreferenceDAO {
    public List<Preference> getPreferencesByUser(int userId) {
        List<Preference> prefs = new ArrayList<>();
        String query = "SELECT * FROM preferences WHERE User_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Preference p = new Preference();
                p.setPreferenceId(rs.getInt("Preference_ID"));
                p.setUserId(rs.getInt("User_ID"));
                p.setCategory(rs.getString("Category"));
                p.setDislikes(rs.getString("Dislikes"));
                prefs.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prefs;
    }

    public void addPreference(Preference preference) {
        String query = "INSERT INTO preferences (User_ID, Category, Dislikes) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, preference.getUserId());
            stmt.setString(2, preference.getCategory());
            stmt.setString(3, preference.getDislikes());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
