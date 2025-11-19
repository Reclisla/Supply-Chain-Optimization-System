package com.model;

import java.util.Date;


public class Order {

    private String orderId;
    private String customerId;
    private Date orderDate;
    private double totalAmount;
    private String status;

    // Default constructor
    public Order() {}

    // Parameterized constructor
    public Order(String orderId, String customerId, Date orderDate, double totalAmount, String status) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    // Getters and setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "-----------------------------\n" +
               "        ORDER DETAILS        \n" +
               "-----------------------------\n" +
               "Order ID     : " + orderId + "\n" +
               "Customer ID  : " + customerId + "\n" +
               "Order Date   : " + orderDate + "\n" +
               "Total Amount : ₹" + totalAmount + "\n" +
               "Status       : " + status + "\n" +
               "-----------------------------";
    }

}
