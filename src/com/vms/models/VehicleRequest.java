package com.vms.models;

import java.util.Date;

/**
 * VehicleRequest model.
 */
public class VehicleRequest {
    private int requestId;
    private String staffName;
    private String destination;
    private Date date;
    private double distance;
    private String status;

    public VehicleRequest() {
    }

    public VehicleRequest(int requestId, String staffName, String destination, Date date, double distance,
            String status) {
        this.requestId = requestId;
        this.staffName = staffName;
        this.destination = destination;
        this.date = date;
        this.distance = distance;
        this.status = status;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
