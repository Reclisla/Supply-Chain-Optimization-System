package com.service;

import java.util.List;
import com.exception.SupplyChainException;
import com.management.TransportationManagement;
import com.model.Transportation;


public class TransportationService {

    private TransportationManagement transportationMgmt;

    public TransportationService() {
        this.transportationMgmt = new TransportationManagement();
    }

     // Creates a new shipment record, validating required fields.
    
    public Transportation createShipment(String orderId, String carrierId, String status)
            throws SupplyChainException {

        if (orderId == null || orderId.trim().isEmpty())
            throw new SupplyChainException("Order ID is required to create a shipment.");

        return transportationMgmt.createShipment(orderId, carrierId, status);
    }

    // Retrieves shipment details by Shipment ID.
    public Transportation getById(String shipmentId) throws SupplyChainException {
        if (shipmentId == null || shipmentId.trim().isEmpty())
            throw new SupplyChainException("Shipment ID cannot be empty.");

        return transportationMgmt.getById(shipmentId);
    }
    
    // Retrieves shipment details by Order ID.
     
    public Transportation getByOrderId(String orderId) throws SupplyChainException {
        if (orderId == null || orderId.trim().isEmpty())
            throw new SupplyChainException("Order ID cannot be empty.");

        return transportationMgmt.getByOrderId(orderId);
    }

    // Updates the status of an existing shipment.
    public boolean updateStatus(String shipmentId, String newStatus) throws SupplyChainException {
        if (shipmentId == null || shipmentId.trim().isEmpty())
            throw new SupplyChainException("Shipment ID cannot be empty for status update.");
        if (newStatus == null || newStatus.trim().isEmpty())
            throw new SupplyChainException("New status cannot be empty.");
            

        return transportationMgmt.updateStatus(shipmentId, newStatus);
    }

    // Lists all shipments (Admin/Management view).
     
    public List<Transportation> listAll() throws SupplyChainException {
        return transportationMgmt.listAll();
    }

    // Lists shipments associated with a specific customer.
     
    public List<Transportation> listByCustomer(String customerId) throws SupplyChainException {
        if (customerId == null || customerId.trim().isEmpty())
            throw new SupplyChainException("Customer ID is required to list shipments.");

        return transportationMgmt.listByCustomer(customerId);
    }
}