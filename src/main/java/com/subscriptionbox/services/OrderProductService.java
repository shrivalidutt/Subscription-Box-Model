package com.subscriptionbox.services;

import com.subscriptionbox.dao.OrderProductDAO;
import com.subscriptionbox.models.OrderProduct;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class OrderProductService {
    private static final Logger LOGGER = Logger.getLogger(OrderProductService.class.getName());
    private OrderProductDAO orderProductDAO = new OrderProductDAO();

    public List<OrderProduct> getOrderProductsByOrderId(int orderId) {
        try {
            return orderProductDAO.getOrderProductsByOrderId(orderId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching order products", e);
            throw new RuntimeException("Failed to fetch order products", e);
        }
    }

    public void addOrderProduct(OrderProduct orderProduct) {
        try {
            orderProductDAO.addOrderProduct(orderProduct);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding order product", e);
            throw new RuntimeException("Failed to add order product", e);
        }
    }
}
