package com.model;

import java.util.Date;

/**
 * Model class representing an inventory entry.
 */
public class Inventory {

    private String inventoryId;
    private String productId;
    private int quantityInStock;
    private Date lastUpdated;
    private String warehouseId; 

    // Default constructor
    public Inventory() {}

    // Constructor with warehouse
    public Inventory(String inventoryId, String productId, int quantityInStock, Date lastUpdated, String warehouseId) {
        this.inventoryId = inventoryId;
        this.productId = productId;
        this.quantityInStock = quantityInStock;
        this.lastUpdated = lastUpdated;
        this.warehouseId = warehouseId;
    }

    public Inventory(String inventoryId, String productId, int quantityInStock, Date lastUpdated) {
        this(inventoryId, productId, quantityInStock, lastUpdated, null);
    }

    // Getters and Setters
    public String getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    @Override
    public String toString() {
        String details = "";
        details += "Inventory ID: " + inventoryId + "\n";
        details += "Product ID: " + productId + "\n";
        details += "Quantity In Stock: " + quantityInStock + "\n";
        details += "Last Updated: " + lastUpdated + "\n";
        details += "Warehouse ID: " + warehouseId + "\n";
        return details;
    }

}

