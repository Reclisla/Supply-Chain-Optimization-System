package com.management;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.exception.SupplyChainException;
import com.model.Order;


public class OrderManagement {

    public Order createOrder(String customerId, double totalAmount) throws SupplyChainException {
       
        String orderId = generateOrderId();
        String sql = "INSERT INTO orders (orderId, customerId, orderDate, totalAmount, status) VALUES (?, ?, NOW(), ?, 'New')";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, orderId);
            ps.setString(2, customerId);
            ps.setDouble(3, totalAmount);

            ps.executeUpdate();

            return new Order(orderId, customerId, new Date(), totalAmount, "New");

        } catch (SQLException e) {
            throw new SupplyChainException("Failed to create order: " + e.getMessage());
        }
    }

    public Order getById(String orderId) throws SupplyChainException {

        String sql = "SELECT * FROM orders WHERE orderId = ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Order(
                            rs.getString("orderId"),
                            rs.getString("customerId"),
                            rs.getTimestamp("orderDate"),
                            rs.getDouble("totalAmount"),
                            rs.getString("status")
                    );
                } else {
                    return null;
                }
            }

        } catch (SQLException e) {
            throw new SupplyChainException("Error fetching order: " + e.getMessage());
        }
    }

    public void updateStatus(String orderId, String status) throws SupplyChainException {
        if (orderId == null || orderId.trim().isEmpty())
            throw new SupplyChainException("Order ID is required.");
        if (status == null || status.trim().isEmpty())
            throw new SupplyChainException("Order status cannot be empty.");

        String allowed = "New,Confirmed,Cancelled,Completed,Shipped";
        if (!allowed.toLowerCase().contains(status.toLowerCase()))
            throw new SupplyChainException("Invalid order status. Allowed: New, Confirmed, Cancelled, Completed, Shipped.");

        String sql = "UPDATE orders SET status = ? WHERE orderId = ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setString(2, orderId);
            int rows = ps.executeUpdate();

            if (rows == 0)
                throw new SupplyChainException("No order found with ID: " + orderId);

        } catch (SQLException e) {
            throw new SupplyChainException("Error updating order: " + e.getMessage());
        }
    }

    
     //Retrieves all orders for a given customer ID.
     
    public List<Order> getByCustomer(String customerId) throws SupplyChainException {

        String sql = "SELECT * FROM orders WHERE customerId = ? ORDER BY orderDate DESC";
        List<Order> orders = new ArrayList<>();

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order(
                            rs.getString("orderId"),
                            rs.getString("customerId"),
                            rs.getTimestamp("orderDate"),
                            rs.getDouble("totalAmount"),
                            rs.getString("status")
                    );
                    orders.add(order);
                }
            }

            return orders;

        } catch (SQLException e) {
            throw new SupplyChainException("Error fetching orders for customer: " + e.getMessage());
        }
    }

    private String generateOrderId() {
        String prefix = "O";
        int nextNum = 5000;
        String sql = "SELECT orderId FROM orders ORDER BY orderId DESC LIMIT 1";

        try (Connection conn = DBConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                String lastId = rs.getString("orderId");
                nextNum = Integer.parseInt(lastId.replaceAll("\\D", "")) + 1;
            }

        } catch (SQLException e) {
            // fallback to O5000
        }

        return prefix + nextNum;
    }
    
    public List<Order> listAll() throws SupplyChainException {
        String sql = "SELECT * FROM orders ORDER BY orderDate DESC";
        List<Order> orders = new ArrayList<>();

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Order order = new Order(
                        rs.getString("orderId"),
                        rs.getString("customerId"),
                        rs.getTimestamp("orderDate"),
                        rs.getDouble("totalAmount"),
                        rs.getString("status")
                );
                orders.add(order);
            }

            return orders;

        } catch (SQLException e) {
            throw new SupplyChainException("Error fetching all orders: " + e.getMessage());
        }
    }
}
