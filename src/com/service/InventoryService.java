package com.service;

import java.util.List;
import com.exception.SupplyChainException;
import com.management.InventoryManagement;
import com.model.Inventory;


public class InventoryService {

    private InventoryManagement inventoryMgmt;
    // Simple low stock threshold
    int threshold= 10; 

    public InventoryService() {
        this.inventoryMgmt = new InventoryManagement();
    }

    
     //Adds stock for a specific product and warehouse (New Stock Arrival).
     
    public Inventory addStock(String productId, int quantity, String warehouseId) throws SupplyChainException {
        if (productId == null || productId.trim().isEmpty())
            throw new SupplyChainException("Product ID cannot be empty.");
        if (warehouseId == null || warehouseId.trim().isEmpty())
            throw new SupplyChainException("Warehouse ID cannot be empty.");
        if (quantity <= 0)
            throw new SupplyChainException("Quantity must be greater than zero.");

        return inventoryMgmt.addStock(productId, quantity, warehouseId);
    }

    
     //Deducts stock for a specific product and warehouse (Dispatch/Sale).
     
    public void deductStock(String productId, int quantity, String warehouseId) throws SupplyChainException {
        if (productId == null || productId.trim().isEmpty())
            throw new SupplyChainException("Product ID cannot be empty.");
        if (warehouseId == null || warehouseId.trim().isEmpty())
            throw new SupplyChainException("Warehouse ID cannot be empty.");
        if (quantity <= 0)
            throw new SupplyChainException("Quantity must be greater than zero for deduction.");

        inventoryMgmt.deductStock(productId, quantity, warehouseId);
    }

    
     //Updates quantity for an existing product in a warehouse (Admin override/correction).
     
    public void updateQuantity(String productId, String warehouseId, int newQuantity) throws SupplyChainException {
        if (productId == null || productId.trim().isEmpty())
            throw new SupplyChainException("Product ID cannot be empty.");
        if (warehouseId == null || warehouseId.trim().isEmpty())
            throw new SupplyChainException("Warehouse ID cannot be empty.");
        if (newQuantity < 0)
            throw new SupplyChainException("Quantity cannot be negative.");

        inventoryMgmt.updateQuantity(productId, warehouseId, newQuantity);
    }
    
   
     // Deletes an inventory record by its unique Inventory ID.
     
    public boolean deleteInventory(String inventoryId) throws SupplyChainException {
        if (inventoryId == null || inventoryId.trim().isEmpty())
            throw new SupplyChainException("Inventory ID cannot be empty for deletion.");

        return inventoryMgmt.deleteInventory(inventoryId);
    }
    
     //Retrieves inventory info for a specific product and warehouse.
     
    public Inventory getInventoryByProduct(String productId, String warehouseId) throws SupplyChainException {
        if (productId == null || productId.trim().isEmpty())
            throw new SupplyChainException("Product ID is required.");
        if (warehouseId == null || warehouseId.trim().isEmpty())
            throw new SupplyChainException("Warehouse ID is required.");

        return inventoryMgmt.getByProductAndWarehouse(productId, warehouseId);
    }
    
    
    // Retrieves inventory info by Inventory ID 
     
    public Inventory getById(String inventoryId) throws SupplyChainException {
         if (inventoryId == null || inventoryId.trim().isEmpty())
            throw new SupplyChainException("Inventory ID is required.");
            
        return inventoryMgmt.getById(inventoryId); 
    }

    
     // Retrieves ALL inventory records for a specific product across all warehouses.
     
    public List<Inventory> getAllInventoryByProductId(String productId) throws SupplyChainException {
        if (productId == null || productId.trim().isEmpty())
            throw new SupplyChainException("Product ID is required.");

        return inventoryMgmt.getByProductId(productId);
    }

    
     // Lists all inventory records (Admin view).
     
    public List<Inventory> listAll() throws SupplyChainException {
        return inventoryMgmt.listAll();
    }

    
     // Flags products/inventory records that are below the low stock threshold.
     
    public List<Inventory> findLowStockItems() throws SupplyChainException {
        return inventoryMgmt.findInventoryBelowQuantity(threshold);
    }
}