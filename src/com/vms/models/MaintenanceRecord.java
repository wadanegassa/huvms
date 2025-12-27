package com.vms.models;

import java.util.Date;

/**
 * MaintenanceRecord model.
 */
public class MaintenanceRecord {
    private int recordId;
    private Vehicle vehicle;
    private String description;
    private Date date;
    private double cost;

    public MaintenanceRecord() {
    }

    public MaintenanceRecord(int recordId, Vehicle vehicle, String description, Date date, double cost) {
        this.recordId = recordId;
        this.vehicle = vehicle;
        this.description = description;
        this.date = date;
        this.cost = cost;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
