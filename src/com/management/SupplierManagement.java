package com.management;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.exception.SupplyChainException;
import com.model.Supplier;


public class SupplierManagement {

    
     // Creates a new supplier record in the database.
     
    public Supplier createSupplier(String name, String contactPerson, String email, String phone)
            throws SupplyChainException {

        String supplierId = generateSupplierId();

        String sql = "INSERT INTO supplier (supplierId, name, contactPerson, email, phone) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, supplierId);
            ps.setString(2, name);
            ps.setString(3, contactPerson);
            ps.setString(4, email);
            ps.setString(5, phone);

            int rows = ps.executeUpdate();
            if (rows == 0)
                throw new SupplyChainException("Failed to add supplier record.");

            return new Supplier(supplierId, name, contactPerson, email, phone);

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SupplyChainException("Duplicate supplier ID or email already exists.");
        } catch (SQLException e) {
            throw new SupplyChainException("Database error while creating supplier: " + e.getMessage());
        }
    }
    
     //Updates an existing supplier record in the database.
    public void updateSupplier(Supplier s) throws SupplyChainException {
        
        String sql = "UPDATE supplier SET name = ?, contactPerson = ?, email = ?, phone = ? WHERE supplierId = ?";
        
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, s.getName());
            ps.setString(2, s.getContactPerson());
            ps.setString(3, s.getEmail());
            ps.setString(4, s.getPhone());
            ps.setString(5, s.getSupplierId());
            
            int rows = ps.executeUpdate();
            if (rows == 0)
                throw new SupplyChainException("No supplier found with ID: " + s.getSupplierId() + " to update.");
                
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SupplyChainException("Update failed: Email already exists for another supplier.");
        } catch (SQLException e) {
            throw new SupplyChainException("Database error while updating supplier: " + e.getMessage());
        }
    }

    
     //Deletes a supplier record by ID.
     
    public boolean deleteSupplier(String supplierId) throws SupplyChainException {
      
        String sql = "DELETE FROM supplier WHERE supplierId = ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, supplierId);
            int rows = ps.executeUpdate();
            
            return rows > 0;

        } catch (SQLException e) {
            throw new SupplyChainException("Database error while deleting supplier: " + e.getMessage());
        }
    }

    
     //Retrieves a supplier record by ID.
     
    public Supplier getById(String supplierId) throws SupplyChainException {

        String sql = "SELECT * FROM supplier WHERE supplierId = ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, supplierId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Supplier(
                                        rs.getString("supplierId"),
                                        rs.getString("name"),
                                        rs.getString("contactPerson"),
                                        rs.getString("email"),
                                        rs.getString("phone")
                    );
                } else {
                    return null; 
                }
            }

        } catch (SQLException e) {
            throw new SupplyChainException("Database error while retrieving supplier: " + e.getMessage());
        }
    }

    
     // Lists all suppliers from the database.
     
    public List<Supplier> listAll() throws SupplyChainException {
        String sql = "SELECT * FROM supplier ORDER BY supplierId";
        List<Supplier> list = new ArrayList<>();

        try (Connection conn = DBConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Supplier s = new Supplier(
                                         rs.getString("supplierId"),
                                         rs.getString("name"),
                                         rs.getString("contactPerson"),
                                         rs.getString("email"),
                                         rs.getString("phone")
                );
                list.add(s);
            }

        } catch (SQLException e) {
            throw new SupplyChainException("Database error while listing suppliers: " + e.getMessage());
        }

        return list;
    }

    
    private String generateSupplierId() {
        String prefix = "S";
        int nextNum = 2000;

        String sql = "SELECT supplierId FROM supplier ORDER BY supplierId DESC LIMIT 1";

        try (Connection conn = DBConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                String lastId = rs.getString("supplierId");
                nextNum = Integer.parseInt(lastId.replaceAll("\\D", "")) + 1;
            }

        } catch (SQLException e) {
            // ignore; default to S2001
        }

        return prefix + nextNum;
    }
}