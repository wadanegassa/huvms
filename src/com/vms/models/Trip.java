package com.vms.models;

import java.util.Date;

/**
 * Trip model.
 */
public class Trip {
    private int tripId;
    private Vehicle vehicle;
    private Driver driver;
    private String destination;
    private Date date;
    private double distance;
    private String requesterName;

    public Trip() {
    }

    public Trip(int tripId, Vehicle vehicle, Driver driver, String destination, Date date, double distance,
            String requesterName) {
        this.tripId = tripId;
        this.vehicle = vehicle;
        this.driver = driver;
        this.destination = destination;
        this.date = date;
        this.distance = distance;
        this.requesterName = requesterName;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
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

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }
}
