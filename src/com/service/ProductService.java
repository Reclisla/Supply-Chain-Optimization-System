package com.service;

import java.util.List;

import com.exception.SupplyChainException;
import com.management.ProductManagement;
import com.model.Product;

public class ProductService {

    private ProductManagement productMgmt;

    public ProductService() {
        this.productMgmt = new ProductManagement();
    }

    
     // Adds a new product into the system. (Renamed to match UI call)
     
    public Product createProduct(String name, String description, double unitPrice, String category, String supplierId)
            throws SupplyChainException {

        if (name == null || name.trim().isEmpty())
            throw new SupplyChainException("Product name cannot be empty.");
        if (unitPrice <= 0)
            throw new SupplyChainException("Unit price must be greater than zero.");
        if (category == null || category.trim().isEmpty())
            throw new SupplyChainException("Product category cannot be empty.");
        if (supplierId == null || supplierId.trim().isEmpty())
            throw new SupplyChainException("Supplier ID cannot be empty.");

        return productMgmt.createProduct(name, description, unitPrice, category, supplierId);
    }

    
     // Retrieves a product by ID
     
    public Product getById(String productId) throws SupplyChainException {
        if (productId == null || productId.trim().isEmpty())
            throw new SupplyChainException("Product ID cannot be empty.");

        return productMgmt.getById(productId);
    }
    
    
     // Updates an existing product record.
     
    public void updateProduct(Product p) throws SupplyChainException {
        if (p == null)
            throw new SupplyChainException("Product object for update cannot be null.");
        if (p.getProductId() == null || p.getProductId().trim().isEmpty())
            throw new SupplyChainException("Product ID is required for update.");
            
        if (p.getUnitPrice() <= 0)
            throw new SupplyChainException("Unit price must be greater than zero.");
            
        productMgmt.updateProduct(p);
    }

    
     // Returns all products from the system. (Renamed to match UI call)
     
    public List<Product> listAll() throws SupplyChainException {
        return productMgmt.listAll();
    }

    
     // Deletes a product by ID. (Updated to match UI call signature and return type)
    
    public boolean deleteProduct(String productId) throws SupplyChainException {
        if (productId == null || productId.trim().isEmpty())
            throw new SupplyChainException("Product ID cannot be empty.");
        
        boolean deleted = productMgmt.deleteProduct(productId);
     
        return deleted;
    }
}