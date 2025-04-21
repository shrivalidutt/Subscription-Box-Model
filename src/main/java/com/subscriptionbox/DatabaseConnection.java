package com.subscriptionbox;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/subscriptionboxdb3";
    private static final String USER = "JavaLogin";
    private static final String PASSWORD = "Java";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
