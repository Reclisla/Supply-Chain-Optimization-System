package com.management;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.exception.SupplyChainException;
import com.model.Inventory;

public class InventoryManagement {

    public Inventory addStock(String productId, int quantity, String warehouseId) throws SupplyChainException {
        
        Inventory existing = null;
        try {
            existing = getByProductAndWarehouse(productId, warehouseId);
        } catch (SupplyChainException ignore) {
        }

        Timestamp now = new Timestamp(new Date().getTime());

        try (Connection conn = DBConnectionManager.getConnection()) {
            if (existing != null) {
                String updateSql =
                        "UPDATE inventory SET quantityInStock = quantityInStock + ?, lastUpdated = ? WHERE productId = ? AND warehouseId = ?";
                try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                    ps.setInt(1, quantity);
                    ps.setTimestamp(2, now);
                    ps.setString(3, productId);
                    ps.setString(4, warehouseId);
                    ps.executeUpdate();
                }
                existing.setQuantityInStock(existing.getQuantityInStock() + quantity);
                existing.setLastUpdated(new Date());
                return existing;
            } else {
                String newInventoryId = generateInventoryId();
                String insertSql =
                        "INSERT INTO inventory (inventoryId, productId, quantityInStock, lastUpdated, warehouseId) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                    ps.setString(1, newInventoryId);
                    ps.setString(2, productId);
                    ps.setInt(3, quantity);
                    ps.setTimestamp(4, now);
                    ps.setString(5, warehouseId);
                    ps.executeUpdate();
                }
                return new Inventory(newInventoryId, productId, quantity, new Date(), warehouseId);
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SupplyChainException("Invalid Product or Warehouse ID.");
        } catch (SQLException e) {
            throw new SupplyChainException("Database error while adding inventory: " + e.getMessage());
        }
    }

    public void deductStock(String productId, int quantity, String warehouseId) throws SupplyChainException {
        Inventory inv = getByProductAndWarehouse(productId, warehouseId);
        if (inv == null)
            throw new SupplyChainException(
                    "No inventory found for product " + productId + " in warehouse " + warehouseId);

        if (inv.getQuantityInStock() < quantity)
            throw new SupplyChainException("Insufficient stock. Available: " + inv.getQuantityInStock()
                    + ", Requested: " + quantity);
        
        if (quantity <= 0)
            throw new SupplyChainException("Quantity must be greater than zero for deduction.");

        String sql =
                "UPDATE inventory SET quantityInStock = quantityInStock - ?, lastUpdated = ? WHERE productId = ? AND warehouseId = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setTimestamp(2, new Timestamp(new Date().getTime()));
            ps.setString(3, productId);
            ps.setString(4, warehouseId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SupplyChainException("Error updating stock: " + e.getMessage());
        }
    }

    public void updateQuantity(String productId, String warehouseId, int newQuantity)
            throws SupplyChainException {
       
        String sql =
                "UPDATE inventory SET quantityInStock = ?, lastUpdated = ? WHERE productId = ? AND warehouseId = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newQuantity);
            ps.setTimestamp(2, new Timestamp(new Date().getTime()));
            ps.setString(3, productId);
            ps.setString(4, warehouseId);
            int rows = ps.executeUpdate();
            if (rows == 0)
                throw new SupplyChainException(
                        "No inventory found for Product ID: " + productId + " in Warehouse: " + warehouseId);
        } catch (SQLException e) {
            throw new SupplyChainException("Database error while updating quantity: " + e.getMessage());
        }
    }

    public Inventory getByProductAndWarehouse(String productId, String warehouseId)
            throws SupplyChainException {
       

        String sql = "SELECT * FROM inventory WHERE productId = ? AND warehouseId = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, productId);
            ps.setString(2, warehouseId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Inventory(
                            rs.getString("inventoryId"),
                            rs.getString("productId"),
                            rs.getInt("quantityInStock"),
                            rs.getTimestamp("lastUpdated"),
                            rs.getString("warehouseId"));
                } else {
                    throw new SupplyChainException(
                            "No inventory found for product " + productId + " in warehouse " + warehouseId);
                }
            }
        } catch (SQLException e) {
            throw new SupplyChainException("Database error while retrieving inventory: " + e.getMessage());
        }
    }

    
    public List<Inventory> getByProductId(String productId) throws SupplyChainException {
        
        List<Inventory> inventoryList = new ArrayList<>();
        String sql = "SELECT * FROM inventory WHERE productId = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    inventoryList.add(new Inventory(
                            rs.getString("inventoryId"),
                            rs.getString("productId"),
                            rs.getInt("quantityInStock"),
                            rs.getTimestamp("lastUpdated"),
                            rs.getString("warehouseId")));
                }
                if (inventoryList.isEmpty()) {
                    throw new SupplyChainException("No inventory found for product ID: " + productId);
                }
            }
        } catch (SQLException e) {
            throw new SupplyChainException("Database error while retrieving inventory by product ID: " + e.getMessage());
        }
        return inventoryList;
    }


    public Inventory getById(String inventoryId) throws SupplyChainException {

        String sql = "SELECT * FROM inventory WHERE inventoryId = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, inventoryId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Inventory(
                            rs.getString("inventoryId"),
                            rs.getString("productId"),
                            rs.getInt("quantityInStock"),
                            rs.getTimestamp("lastUpdated"),
                            rs.getString("warehouseId"));
                } else {
                    throw new SupplyChainException("No inventory found with ID " + inventoryId);
                }
            }
        } catch (SQLException e) {
            throw new SupplyChainException("Database error while retrieving inventory by ID: " + e.getMessage());
        }
    }

    //update inventory for objects
    public void updateInventory(Inventory inv) throws SupplyChainException {
        updateInventory(inv.getInventoryId(), inv.getQuantityInStock(), inv.getWarehouseId());
    }

    //update inventory for variables
    public void updateInventory(String inventoryId, int newQuantity, String newWarehouseId) throws SupplyChainException {
        if (inventoryId == null || inventoryId.trim().isEmpty())
            throw new SupplyChainException("Inventory ID cannot be empty.");
        if (newQuantity < 0)
            throw new SupplyChainException("Quantity cannot be negative.");
        if (newWarehouseId == null || newWarehouseId.trim().isEmpty())
            throw new SupplyChainException("Warehouse ID cannot be empty.");

        String sql = "UPDATE inventory SET quantityInStock = ?, warehouseId = ?, lastUpdated = ? WHERE inventoryId = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newQuantity);
            ps.setString(2, newWarehouseId);
            ps.setTimestamp(3, new Timestamp(new Date().getTime()));
            ps.setString(4, inventoryId);
            int rows = ps.executeUpdate();
            if (rows == 0)
                throw new SupplyChainException("No inventory found with ID " + inventoryId);
        } catch (SQLException e) {
            throw new SupplyChainException("Database error while updating inventory: " + e.getMessage());
        }
    }


    public boolean deleteInventory(String inventoryId) throws SupplyChainException {
        
        String sql = "DELETE FROM inventory WHERE inventoryId = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, inventoryId);
            int rows = ps.executeUpdate();
            return rows > 0; 

        } catch (SQLException e) {
            throw new SupplyChainException("Database error while deleting inventory: " + e.getMessage());
        }
    }


    public List<Inventory> listAll() throws SupplyChainException {
        List<Inventory> list = new ArrayList<>();
        String sql = "SELECT * FROM inventory";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Inventory(
                        rs.getString("inventoryId"),
                        rs.getString("productId"),
                        rs.getInt("quantityInStock"),
                        rs.getTimestamp("lastUpdated"),
                        rs.getString("warehouseId")));
            }
        } catch (SQLException e) {
            throw new SupplyChainException("Error fetching inventory list: " + e.getMessage());
        }
        return list;
    }

    
     //Retrieves all inventory records where quantity is below a given threshold.
     
    public List<Inventory> findInventoryBelowQuantity(int threshold) throws SupplyChainException {
        if (threshold < 0)
            throw new SupplyChainException("Threshold cannot be negative.");

        List<Inventory> lowStockList = new ArrayList<>();
        // Use < and not <= in case threshold is 0, which would show all 0 stock items.
        String sql = "SELECT * FROM inventory WHERE quantityInStock < ?"; 
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, threshold);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lowStockList.add(new Inventory(
                            rs.getString("inventoryId"),
                            rs.getString("productId"),
                            rs.getInt("quantityInStock"),
                            rs.getTimestamp("lastUpdated"),
                            rs.getString("warehouseId")));
                }
            }
        } catch (SQLException e) {
            throw new SupplyChainException("Error fetching low stock inventory: " + e.getMessage());
        }
        return lowStockList;
    }

    private String generateInventoryId() {
        String prefix = "I";
        int nextNum = 3000;
        String sql = "SELECT inventoryId FROM inventory ORDER BY inventoryId DESC LIMIT 1";
        try (Connection conn = DBConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                String lastId = rs.getString("inventoryId");
                // Safety check for non-numeric part
                if (lastId != null && lastId.startsWith(prefix)) {
                    try {
                        nextNum = Integer.parseInt(lastId.substring(prefix.length())) + 1;
                    } catch (NumberFormatException ignored) {
                        // If parsing fails, fall back to default start number 3000
                    }
                }
            }
        } catch (SQLException ignored) {
        }
        return prefix + nextNum;
    }
}