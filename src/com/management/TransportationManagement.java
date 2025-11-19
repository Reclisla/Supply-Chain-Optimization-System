package com.management;

import com.model.Order;
import com.model.Transportation;
import com.exception.SupplyChainException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TransportationManagement {

    private OrderManagement orderMgmt = new OrderManagement();
    private InventoryManagement inventoryMgmt = new InventoryManagement();

    //  Create a new shipment (auto-updates order + interacts with transportation table)
    public Transportation createShipment(String orderId, String carrierId, String status)
            throws SupplyChainException {

        //  Validate order existence
        Order order = orderMgmt.getById(orderId);
        if (order == null)
            throw new SupplyChainException("No order found with ID: " + orderId);

        //  Allow only New or Confirmed orders to be shipped
        String currentStatus = order.getStatus();
        if (!currentStatus.equalsIgnoreCase("New") && !currentStatus.equalsIgnoreCase("Confirmed")) {
            throw new SupplyChainException(
                    "Shipment can only be created for orders with status 'New' or 'Confirmed'. Current: " + currentStatus);
        }

        //  Auto-generate unique carrier ID if not given
        if (carrierId == null || carrierId.trim().isEmpty()) {
            carrierId = generateCarrierId();
        }

        //  Default status
        if (status == null || status.trim().isEmpty())
            status = "Shipped";

        //  Generate Shipment ID
        String shipmentId = generateShipmentId();
        Timestamp now = new Timestamp(new Date().getTime());

        String sql = "INSERT INTO transportation (shipmentId, orderId, carrierId, status, shippedDate) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, shipmentId);
            ps.setString(2, orderId);
            ps.setString(3, carrierId);
            ps.setString(4, status);
            ps.setTimestamp(5, now);

            int rows = ps.executeUpdate();
            if (rows == 0)
                throw new SupplyChainException("Failed to insert shipment record into database.");

            //  Update order status to "Shipped"
            orderMgmt.updateStatus(orderId, "Shipped");

            System.out.println("Shipment created successfully for Order ID: " + orderId);
            return new Transportation(shipmentId, orderId, carrierId, status, new Date());

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SupplyChainException("Invalid Order ID — cannot create shipment.");
        } catch (SQLException e) {
            throw new SupplyChainException("Database error while creating shipment: " + e.getMessage());
        }
    }

    //  Get shipment by shipmentId
    public Transportation getById(String shipmentId) throws SupplyChainException {
        if (shipmentId == null || shipmentId.trim().isEmpty())
            throw new SupplyChainException("Shipment ID cannot be empty.");

        String sql = "SELECT * FROM transportation WHERE shipmentId = ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, shipmentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Transportation(
                        rs.getString("shipmentId"),
                        rs.getString("orderId"),
                        rs.getString("carrierId"),
                        rs.getString("status"),
                        rs.getTimestamp("shippedDate")
                );
            }
        } catch (SQLException e) {
            throw new SupplyChainException("Failed to fetch shipment: " + e.getMessage());
        }
        return null;
    }

    //  Get shipment by orderId
    public Transportation getByOrderId(String orderId) throws SupplyChainException {
        if (orderId == null || orderId.trim().isEmpty())
            throw new SupplyChainException("Order ID cannot be empty.");

        String sql = "SELECT * FROM transportation WHERE orderId = ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Transportation(
                        rs.getString("shipmentId"),
                        rs.getString("orderId"),
                        rs.getString("carrierId"),
                        rs.getString("status"),
                        rs.getTimestamp("shippedDate")
                );
            }
        } catch (SQLException e) {
            throw new SupplyChainException("Failed to fetch shipment by order: " + e.getMessage());
        }
        return null;
    }

    //  Update shipment status (auto updates order too)
    public boolean updateStatus(String shipmentId, String newStatus) throws SupplyChainException {
        if (shipmentId == null || shipmentId.trim().isEmpty())
            throw new SupplyChainException("Shipment ID cannot be empty.");
        if (newStatus == null || newStatus.trim().isEmpty())
            throw new SupplyChainException("Shipment status cannot be empty.");

        String sql = "UPDATE transportation SET status = ? WHERE shipmentId = ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            ps.setString(2, shipmentId);
            int rows = ps.executeUpdate();

            if (rows > 0 && newStatus.equalsIgnoreCase("Delivered")) {
                Transportation t = getById(shipmentId);
                if (t != null)
                    orderMgmt.updateStatus(t.getOrderId(), "Completed");
            }

            return rows > 0;

        } catch (SQLException e) {
            throw new SupplyChainException("Failed to update shipment status: " + e.getMessage());
        }
    }

    // List all shipments (for admin)
    public List<Transportation> listAll() throws SupplyChainException {
        List<Transportation> list = new ArrayList<>();
        String sql = "SELECT * FROM transportation ORDER BY shipmentId DESC";

        try (Connection conn = DBConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Transportation(
                        rs.getString("shipmentId"),
                        rs.getString("orderId"),
                        rs.getString("carrierId"),
                        rs.getString("status"),
                        rs.getTimestamp("shippedDate")
                ));
            }
        } catch (SQLException e) {
            throw new SupplyChainException("Failed to list shipments: " + e.getMessage());
        }
        return list;
    }

    //  List shipments by customer
    public List<Transportation> listByCustomer(String customerId) throws SupplyChainException {
        List<Transportation> list = new ArrayList<>();
        String sql = "SELECT t.* FROM transportation t JOIN orders o ON t.orderId = o.orderId WHERE o.customerId = ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, customerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Transportation(
                        rs.getString("shipmentId"),
                        rs.getString("orderId"),
                        rs.getString("carrierId"),
                        rs.getString("status"),
                        rs.getTimestamp("shippedDate")
                ));
            }
        } catch (SQLException e) {
            throw new SupplyChainException("Failed to list shipments for customer: " + e.getMessage());
        }
        return list;
    }

    // Helper for generating shipment IDs (T1000, T1001, ...)
    private String generateShipmentId() {
        String prefix = "T";
        int nextNum = 1000;

        String sql = "SELECT shipmentId FROM transportation ORDER BY shipmentId DESC LIMIT 1";
        try (Connection conn = DBConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                String lastId = rs.getString("shipmentId");
                nextNum = Integer.parseInt(lastId.replaceAll("\\D", "")) + 1;
            }
        } catch (Exception e) {
            // ignore fallback
        }
        return prefix + nextNum;
    }

 // Generates unique carrier IDs like CR1, CR2, CR3...
    private String generateCarrierId() {
        String prefix = "CR";
        int nextNum = 1;

        String sql = "SELECT carrierId FROM transportation ORDER BY LENGTH(carrierId) DESC, carrierId DESC LIMIT 1";

        try (Connection conn = DBConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                String lastId = rs.getString("carrierId");
                if (lastId != null && lastId.matches("CR\\d+")) {
                    nextNum = Integer.parseInt(lastId.substring(2)) + 1;
                }
            }
        } catch (SQLException e) {
            System.err.println("Carrier ID generation failed, defaulting to CR1: " + e.getMessage());
        }

        return prefix + nextNum;
    }

}
