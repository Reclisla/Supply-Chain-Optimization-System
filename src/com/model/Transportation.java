package com.model;

import java.util.Date;


public class Transportation {

    private String shipmentId;
    private String orderId;
    private String carrierId;
    private String status;
    private Date shippedDate;

    // Default constructor
    public Transportation() {}

    // Parameterized constructor
    public Transportation(String shipmentId, String orderId, String carrierId, String status, Date shippedDate) {
        this.shipmentId = shipmentId;
        this.orderId = orderId;
        this.carrierId = carrierId;
        this.status = status;
        this.shippedDate = shippedDate;
    }
 // Overloaded constructor without shippedDate
    public Transportation(String shipmentId, String orderId, String carrierId, String status) {
        this.shipmentId = shipmentId;
        this.orderId = orderId;
        this.carrierId = carrierId;
        this.status = status;
        this.shippedDate = new java.util.Date(); // default to current date
    }


    // Getters and setters
    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCarrierId() {
        return carrierId;
    }

    public void setCarrierId(String carrierId) {
        this.carrierId = carrierId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getShippedDate() {
        return shippedDate;
    }

    public void setShippedDate(Date shippedDate) {
        this.shippedDate = shippedDate;
    }

    @Override
    public String toString() {
        return "Transportation [shipmentId=" + shipmentId +
               ", orderId=" + orderId +
               ", carrierId=" + carrierId +
               ", status=" + status +
               ", shippedDate=" + shippedDate + "]";
    }
}
