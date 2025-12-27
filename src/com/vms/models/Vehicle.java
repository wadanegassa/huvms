package com.vms.models;

/**
 * Vehicle entity class representing a vehicle in the system.
 */
public class Vehicle {
    private int vehicleId;
    private String plateNumber;
    private String vehicleType;
    private String status;

    public Vehicle() {
    }

    public Vehicle(int vehicleId, String plateNumber, String vehicleType, String status) {
        this.vehicleId = vehicleId;
        this.plateNumber = plateNumber;
        this.vehicleType = vehicleType;
        this.status = status;
    }

    // Getters and Setters
    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Vehicle [ID=" + vehicleId + ", Plate=" + plateNumber + ", Type=" + vehicleType + ", Status=" + status
                + "]";
    }
}
