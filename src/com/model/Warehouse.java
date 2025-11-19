package com.model;


public class Warehouse {

    private String warehouseId;
    private String name;
    private String location;
    private int capacity;
    private int usedCapacity; 
    // Default constructor
    public Warehouse() {}

    // Parameterized constructor
    public Warehouse(String warehouseId, String name, String location, int capacity) {
        this.warehouseId = warehouseId;
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.usedCapacity = 0; 
    }

    // ✅ Full constructor including usedCapacity
    public Warehouse(String warehouseId, String name, String location, int capacity, int usedCapacity) {
        this.warehouseId = warehouseId;
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.usedCapacity = usedCapacity;
    }

    // Getters and setters
    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getUsedCapacity() {
        return usedCapacity;
    }

    public void setUsedCapacity(int usedCapacity) {
        this.usedCapacity = usedCapacity;
    }

    // Helper to check available space
    public int getAvailableCapacity() {
        return capacity - usedCapacity;
    }

    @Override
    public String toString() {
        String details = "";
        details += "Warehouse ID: " + warehouseId + "\n";
        details += "Name: " + name + "\n";
        details += "Location: " + location + "\n";
        details += "Capacity: " + capacity + "\n";
        details += "Used Capacity: " + usedCapacity + "\n";
        details += "Available Capacity: " + getAvailableCapacity() + "\n";
        return details;
    }

}
