package com.subscriptionbox.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class BaseService {
    private static final String URL = "jdbc:mysql://localhost:3306/subscription_box";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
} 