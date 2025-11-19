package com.model;


public class Product {

    private String productId;
    private String name;
    private String description;
    private double unitPrice;
    private String category;
    private String supplierId;

    // Default constructor
    public Product() {}

    // Parameterized constructor
    public Product(String productId, String name, String description, double unitPrice, String category, String supplierId) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.unitPrice = unitPrice;
        this.category = category;
        this.supplierId = supplierId;
    }

    // Getters and setters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    @Override
    public String toString() {
        return "\nName        : " + name +
               "\nCategory    : " + category +
               "\nPrice       : ₹" + String.format("%.2f", unitPrice) +
               "\nDescription : " + description +
               "\nProductId   : " +productId+
               "\n----------------------------";
    }

}
