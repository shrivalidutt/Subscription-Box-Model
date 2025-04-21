package com.subscriptionbox.services;

import com.subscriptionbox.dao.UserDAO;
import com.subscriptionbox.models.User;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class UserService extends BaseService {
    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());
    private final UserDAO userDAO = new UserDAO();

    public List<User> fetchAllUsers() {
        try {
            LOGGER.info("Fetching all users");
            return userDAO.getAllUsers();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching users", e);
            throw new RuntimeException("Failed to fetch users", e);
        }
    }

    public boolean createUser(User user) {
        try {
            LOGGER.info("Creating new user: " + user.getUserName());
            userDAO.addUser(user);
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating user", e);
            return false;
        }
    }

    public boolean updateUser(User user) {
        try {
            LOGGER.info("Updating user with ID: " + user.getUserId());
            userDAO.updateUser(user);
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating user", e);
            return false;
        }
    }

    public boolean deleteUser(int userId) {
        try {
            LOGGER.info("Deleting user with ID: " + userId);
            userDAO.deleteUser(userId);
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting user", e);
            return false;
        }
    }
}
