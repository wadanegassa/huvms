package com.vms.models;

import java.util.Date;

/**
 * FuelRecord model.
 */
public class FuelRecord {
    private int recordId;
    private Vehicle vehicle;
    private double amount;
    private double cost;
    private Date date;

    public FuelRecord() {
    }

    public FuelRecord(int recordId, Vehicle vehicle, double amount, double cost, Date date) {
        this.recordId = recordId;
        this.vehicle = vehicle;
        this.amount = amount;
        this.cost = cost;
        this.date = date;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
