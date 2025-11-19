package com.management;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.exception.SupplyChainException;
import com.model.Product;


public class ProductManagement {

    private SupplierManagement supplierMgmt = new SupplierManagement();

    
    public Product createProduct(String name, String description, double unitPrice, String category, String supplierId)
            throws SupplyChainException {

        if (supplierMgmt.getById(supplierId) == null)
            throw new SupplyChainException("Supplier with ID '" + supplierId + "' does not exist.");

        String productId = generateProductId();

        String sql = "INSERT INTO product (productId, name, description, unitPrice, category, supplierId) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, productId);
            ps.setString(2, name);
            ps.setString(3, description);
            ps.setDouble(4, unitPrice);
            ps.setString(5, category);
            ps.setString(6, supplierId);

            int rows = ps.executeUpdate();
            if (rows == 0)
                throw new SupplyChainException("Failed to insert product record.");

            return new Product(productId, name, description, unitPrice, category, supplierId);

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SupplyChainException("Duplicate Product ID or invalid Supplier reference.");
        } catch (SQLException e) {
            throw new SupplyChainException("Database error while adding product: " + e.getMessage());
        }
    }

    
    public boolean updateProduct(Product p) throws SupplyChainException {
       
        // Ensure supplier exists if the ID was changed (Service layer should ideally handle this check)
        if (supplierMgmt.getById(p.getSupplierId()) == null)
            throw new SupplyChainException("Supplier with ID '" + p.getSupplierId() + "' does not exist.");
            
        String sql = "UPDATE product SET name=?, description=?, unitPrice=?, category=?, supplierId=? WHERE productId=?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getName());
            ps.setString(2, p.getDescription());
            ps.setDouble(3, p.getUnitPrice());
            ps.setString(4, p.getCategory());
            ps.setString(5, p.getSupplierId());
            ps.setString(6, p.getProductId());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            throw new SupplyChainException("Database error while updating product: " + e.getMessage());
        }
    }
    
    
     //Deletes a product by ID.
     
    public boolean deleteProduct(String productId) throws SupplyChainException {
    	
        String sql = "DELETE FROM product WHERE productId = ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, productId);
            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            throw new SupplyChainException("Database error while deleting product: " + e.getMessage());
        }
    }

    
     // Retrieves product details by product ID.
     
    public Product getById(String productId) throws SupplyChainException {

        String sql = "SELECT * FROM product WHERE productId = ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Product(
                                rs.getString("productId"),
                                rs.getString("name"),
                                rs.getString("description"),
                                rs.getDouble("unitPrice"),
                                rs.getString("category"),
                                rs.getString("supplierId")
                    );
                } else {
                    return null;
                }
            }

        } catch (SQLException e) {
            throw new SupplyChainException("Database error while retrieving product: " + e.getMessage());
        }
    }

    
     //Lists all products from the database.
     
    public List<Product> listAll() throws SupplyChainException {
        String sql = "SELECT * FROM product ORDER BY productId";
        List<Product> products = new ArrayList<>();

        try (Connection conn = DBConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Product p = new Product(
                                         rs.getString("productId"),
                                         rs.getString("name"),
                                         rs.getString("description"),
                                         rs.getDouble("unitPrice"),
                                         rs.getString("category"),
                                         rs.getString("supplierId")
                );
                products.add(p);
            }

            return products;

        } catch (SQLException e) {
            throw new SupplyChainException("Database error while listing products: " + e.getMessage());
        }
    }

    
    private String generateProductId() {
        String prefix = "P";
        int nextNum = 1000;

        String sql = "SELECT productId FROM product ORDER BY productId DESC LIMIT 1";

        try (Connection conn = DBConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                String lastId = rs.getString("productId");
                nextNum = Integer.parseInt(lastId.replaceAll("\\D", "")) + 1;
            }

        } catch (SQLException e) {
            // ignore; fallback to P1001
        }

        return prefix + nextNum;
    }
}