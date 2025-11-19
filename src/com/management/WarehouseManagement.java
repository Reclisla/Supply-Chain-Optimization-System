package com.management;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.exception.SupplyChainException;
import com.model.Warehouse;

public class WarehouseManagement {

    
     // Adds a new warehouse record to the database.
     
    public Warehouse addWarehouse(String name, String location, int capacity) throws SupplyChainException {

        String warehouseId = generateWarehouseId();
        String sql = "INSERT INTO warehouse (warehouseId, name, location, capacity) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, warehouseId);
            ps.setString(2, name);
            ps.setString(3, location);
            ps.setInt(4, capacity);

            int rows = ps.executeUpdate();
            if (rows == 0)
                throw new SupplyChainException("Failed to insert warehouse record.");

            return new Warehouse(warehouseId, name, location, capacity);

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SupplyChainException("Duplicate warehouse ID or invalid data.");
        } catch (SQLException e) {
            throw new SupplyChainException("Database error while adding warehouse: " + e.getMessage());
        }
    }

    
     //Retrieves warehouse details by ID.
     
    public Warehouse getById(String warehouseId) throws SupplyChainException {
        
        String sql = "SELECT * FROM warehouse WHERE warehouseId = ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, warehouseId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Warehouse(
                                 rs.getString("warehouseId"),
                                 rs.getString("name"),
                                 rs.getString("location"),
                                 rs.getInt("capacity")
                    );
                } else {
                    return null;
                }
            }

        } catch (SQLException e) {
            throw new SupplyChainException("Database error while retrieving warehouse: " + e.getMessage());
        }
    }

    
      //Updates warehouse information.
     
    public void updateWarehouse(String warehouseId, String name, String location, int capacity)
            throws SupplyChainException {
        if (warehouseId == null || warehouseId.trim().isEmpty())
            throw new SupplyChainException("Warehouse ID cannot be empty.");
        if (name == null || name.trim().isEmpty())
            throw new SupplyChainException("Warehouse name cannot be empty.");
        if (location == null || location.trim().isEmpty())
            throw new SupplyChainException("Warehouse location cannot be empty.");
        if (capacity <= 0)
            throw new SupplyChainException("Warehouse capacity must be positive.");

        String sql = "UPDATE warehouse SET name = ?, location = ?, capacity = ? WHERE warehouseId = ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, location);
            ps.setInt(3, capacity);
            ps.setString(4, warehouseId);

            int rows = ps.executeUpdate();
            if (rows == 0)
                throw new SupplyChainException("No warehouse found with ID: " + warehouseId);

        } catch (SQLException e) {
            throw new SupplyChainException("Database error while updating warehouse: " + e.getMessage());
        }
    }

    
     //Deletes a warehouse by ID.
     
    public boolean deleteWarehouse(String warehouseId) throws SupplyChainException {

        String sql = "DELETE FROM warehouse WHERE warehouseId = ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, warehouseId);
            int rows = ps.executeUpdate();
            
            return rows > 0;

        } catch (SQLException e) {
            throw new SupplyChainException("Database error while deleting warehouse (Check for associated inventory/data): " + e.getMessage());
        }
    }

    
     // Lists all warehouses from the database.
     
    public List<Warehouse> listAll() throws SupplyChainException {
        String sql = "SELECT * FROM warehouse ORDER BY warehouseId";
        List<Warehouse> warehouses = new ArrayList<>();

        try (Connection conn = DBConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Warehouse w = new Warehouse(
                                 rs.getString("warehouseId"),
                                 rs.getString("name"),
                                 rs.getString("location"),
                                 rs.getInt("capacity")
                );
                warehouses.add(w);
            }

            return warehouses;

        } catch (SQLException e) {
            throw new SupplyChainException("Database error while listing warehouses: " + e.getMessage());
        }
    }

    // Generates Warehouse IDs like W7001, W7002, etc.
    private String generateWarehouseId() {
        String prefix = "W";
        int nextNum = 7000;

        String sql = "SELECT warehouseId FROM warehouse ORDER BY warehouseId DESC LIMIT 1";

        try (Connection conn = DBConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                String lastId = rs.getString("warehouseId");
                nextNum = Integer.parseInt(lastId.replaceAll("\\D", "")) + 1;
            }

        } catch (SQLException e) {
            // ignore, fallback to W7001
        }

        return prefix + nextNum;
    }
}