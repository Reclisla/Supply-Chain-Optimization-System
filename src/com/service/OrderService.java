package com.service;

import java.util.List;

import com.exception.SupplyChainException;
import com.management.OrderManagement;
import com.model.Order;


public class OrderService {

    private OrderManagement orderMgmt;

    public OrderService() {
        this.orderMgmt = new OrderManagement();
    }

    
     // Creates a new order in the system.
     
    public Order createOrder(String customerId, double totalAmount) throws SupplyChainException {
        if (customerId == null || customerId.trim().isEmpty())
            throw new SupplyChainException("Customer ID cannot be empty.");
        if (totalAmount <= 0)
            throw new SupplyChainException("Total amount must be greater than zero.");

        return orderMgmt.createOrder(customerId, totalAmount);
    }

    
     // Retrieves an existing order by ID.
     
    public Order getOrderById(String orderId) throws SupplyChainException {
        if (orderId == null || orderId.trim().isEmpty())
            throw new SupplyChainException("Order ID cannot be empty.");

        return orderMgmt.getById(orderId);
    }

    
     // Retrieves all orders placed by a specific customer ID (required by UI for tracking).
     
    public List<Order> getByCustomer(String customerId) throws SupplyChainException {
        if (customerId == null || customerId.trim().isEmpty())
            throw new SupplyChainException("Customer ID is required to fetch orders.");

        return orderMgmt.getByCustomer(customerId); 
    }
    
     //Updates the status of an existing order.
     
    public void updateOrderStatus(String orderId, String status) throws SupplyChainException {
        if (orderId == null || orderId.trim().isEmpty())
            throw new SupplyChainException("Order ID is required.");
        if (status == null || status.trim().isEmpty())
            throw new SupplyChainException("Order status cannot be empty.");

        orderMgmt.updateStatus(orderId, status);
    }

    
     // Lists all orders (optional, if you add a listAll() in OrderManagement).
     
    public List<Order> listAllOrders() throws SupplyChainException {
        return orderMgmt.listAll();
    }
}