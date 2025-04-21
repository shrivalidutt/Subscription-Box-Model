package com.subscriptionbox.dao;

import com.subscriptionbox.models.OrderProduct;
import com.subscriptionbox.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderProductDAO {
    private final DatabaseConnection dbConnection = new DatabaseConnection();

    public List<OrderProduct> getOrderProductsByOrderId(int orderId) throws SQLException {
        List<OrderProduct> orderProducts = new ArrayList<>();
        String sql = "SELECT * FROM order_products WHERE Order_ID = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, orderId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    orderProducts.add(mapResultSetToOrderProduct(rs));
                }
            }
        }
        return orderProducts;
    }

    public void addOrderProduct(OrderProduct orderProduct) throws SQLException {
        String sql = "INSERT INTO order_products (Order_ID, Product_ID, Quantity, Price) " +
                    "VALUES (?, ?, ?, ?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, orderProduct.getOrderId());
            pstmt.setInt(2, orderProduct.getProductId());
            pstmt.setInt(3, orderProduct.getQuantity());
            pstmt.setDouble(4, orderProduct.getPrice());
            
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    orderProduct.setOrderProductId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public boolean updateOrderProduct(OrderProduct orderProduct) throws SQLException {
        String sql = "UPDATE order_products SET Order_ID = ?, Product_ID = ?, " +
                    "Quantity = ?, Price = ? WHERE Order_ProductID = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, orderProduct.getOrderId());
            pstmt.setInt(2, orderProduct.getProductId());
            pstmt.setInt(3, orderProduct.getQuantity());
            pstmt.setDouble(4, orderProduct.getPrice());
            pstmt.setInt(5, orderProduct.getOrderProductId());
            
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean deleteOrderProduct(int orderProductId) throws SQLException {
        String sql = "DELETE FROM order_products WHERE Order_ProductID = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, orderProductId);
            return pstmt.executeUpdate() > 0;
        }
    }

    private OrderProduct mapResultSetToOrderProduct(ResultSet rs) throws SQLException {
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setOrderProductId(rs.getInt("Order_ProductID"));
        orderProduct.setOrderId(rs.getInt("Order_ID"));
        orderProduct.setProductId(rs.getInt("Product_ID"));
        orderProduct.setQuantity(rs.getInt("Quantity"));
        orderProduct.setPrice(rs.getDouble("Price"));
        return orderProduct;
    }
}

