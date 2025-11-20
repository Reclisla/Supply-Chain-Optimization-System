package com.service;

import java.util.List;

import com.exception.SupplyChainException;
import com.management.WarehouseManagement; 
import com.model.Warehouse;

public class WarehouseService {

    private WarehouseManagement warehouseMgmt;

    public WarehouseService() {
        this.warehouseMgmt = new WarehouseManagement();
    }

    // Adds a new warehouse record into the system.
     
    public Warehouse addWarehouse(String name, String location, int capacity)
            throws SupplyChainException {

        if (name == null || name.trim().isEmpty())
            throw new SupplyChainException("Warehouse name cannot be empty.");
        if (location == null || location.trim().isEmpty())
            throw new SupplyChainException("Warehouse location cannot be empty.");
        if (capacity <= 0)
            throw new SupplyChainException("Warehouse capacity must be greater than zero.");

        return warehouseMgmt.addWarehouse(name, location, capacity);
    }
    
   

    // Retrieves warehouse details by ID
     
    public Warehouse getById(String warehouseId) throws SupplyChainException {
        if (warehouseId == null || warehouseId.trim().isEmpty())
            throw new SupplyChainException("Warehouse ID cannot be empty.");

        return warehouseMgmt.getById(warehouseId);
    }

    // Lists all warehouses from the system. (Renamed to match UI call)
    
    public List<Warehouse> listAll() throws SupplyChainException {
        return warehouseMgmt.listAll();
    }

    // Deletes a warehouse record by ID. 
     
    public boolean deleteWarehouse(String warehouseId) throws SupplyChainException {
        if (warehouseId == null || warehouseId.trim().isEmpty())
            throw new SupplyChainException("Warehouse ID cannot be empty.");

        boolean deleted = warehouseMgmt.deleteWarehouse(warehouseId);
        
       
        return deleted;
    }
    
    public Warehouse updateWarehouse(String warehouseId, String name, String location, int capacity)
            throws SupplyChainException {

        if (warehouseId == null || warehouseId.trim().isEmpty())
            throw new SupplyChainException("Warehouse ID cannot be empty for update.");
        if (name == null || name.trim().isEmpty())
            throw new SupplyChainException("Warehouse name cannot be empty.");
        if (location == null || location.trim().isEmpty())
            throw new SupplyChainException("Warehouse location cannot be empty.");
        if (capacity <= 0)
            throw new SupplyChainException("Warehouse capacity must be greater than zero.");

        Warehouse existing = warehouseMgmt.getById(warehouseId);
        
        if (existing == null) {
             throw new SupplyChainException("No warehouse found with ID: " + warehouseId);
        }

        if (capacity < existing.getUsedCapacity()) {
            throw new SupplyChainException(
                "New capacity (" + capacity + ") is less than current used capacity (" + existing.getUsedCapacity() + "). Cannot update."
            );
        }

        warehouseMgmt.updateWarehouse(warehouseId, name, location, capacity);
        
        return warehouseMgmt.getById(warehouseId);
    }
}
