package com.service;

import java.util.List;

import com.exception.SupplyChainException;
import com.management.SupplierManagement;
import com.model.Supplier;


public class SupplierService {

    private SupplierManagement supplierMgmt;

    public SupplierService() {
        this.supplierMgmt = new SupplierManagement();
    }

    
     // Adds a new supplier to the system. (Renamed from addSupplier to match Management's createSupplier)
     
    public Supplier createSupplier(String name, String contactPerson, String email, String phone)
            throws SupplyChainException {

        
        if (name == null || name.trim().isEmpty())
            throw new SupplyChainException("Supplier name cannot be empty.");
        if (contactPerson == null || contactPerson.trim().isEmpty())
            throw new SupplyChainException("Contact person cannot be empty.");
        if (email == null || !email.contains("@"))
            throw new SupplyChainException("Invalid email address.");
        if (phone == null || phone.trim().isEmpty())
            throw new SupplyChainException("Phone number cannot be empty.");

        return supplierMgmt.createSupplier(name, contactPerson, email, phone);
    }

    
     // Retrieves a supplier by their ID. (Renamed from getSupplierById to match Management's getById and UI call)
     
    public Supplier getById(String supplierId) throws SupplyChainException {
        if (supplierId == null || supplierId.trim().isEmpty())
            throw new SupplyChainException("Supplier ID cannot be empty.");

        return supplierMgmt.getById(supplierId);
    }
    
    
     // Updates an existing supplier record using the Supplier object.
     
    public void updateSupplier(Supplier s) throws SupplyChainException {
        if (s == null)
            throw new SupplyChainException("Supplier object for update cannot be null.");
        if (s.getSupplierId() == null || s.getSupplierId().trim().isEmpty())
            throw new SupplyChainException("Supplier ID is required for update.");
            
        if (s.getEmail() == null || !s.getEmail().contains("@"))
            throw new SupplyChainException("Invalid email address after update changes.");

        supplierMgmt.updateSupplier(s);
    }

    
     //Returns all suppliers in the system. (Renamed from getAllSuppliers to match Management's listAll)
     
    public List<Supplier> listAll() throws SupplyChainException {
        return supplierMgmt.listAll();
    }

    
     //Deletes a supplier record by ID.
     
    public boolean deleteSupplier(String supplierId) throws SupplyChainException {
        if (supplierId == null || supplierId.trim().isEmpty())
            throw new SupplyChainException("Supplier ID cannot be empty.");

        boolean deleted = supplierMgmt.deleteSupplier(supplierId);
        
        if (!deleted) {
             throw new SupplyChainException("Deletion failed. No supplier found with ID: " + supplierId);
        }
        
        return deleted;
    }
}