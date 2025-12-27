package com.vms.models;

/**
 * Driver model.
 */
public class Driver {
    private int driverId;
    private String name;
    private String licenseNumber;
    private String phone;

    public Driver() {
    }

    public Driver(int driverId, String name, String licenseNumber, String phone) {
        this.driverId = driverId;
        this.name = name;
        this.licenseNumber = licenseNumber;
        this.phone = phone;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Driver [ID=" + driverId + ", Name=" + name + ", License=" + licenseNumber + "]";
    }
}
