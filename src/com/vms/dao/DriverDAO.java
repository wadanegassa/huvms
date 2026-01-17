package com.vms.dao;

import com.vms.db.DBConnection;
import com.vms.models.Driver;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DriverDAO class for performing CRUD operations on the driver table.
 */
public class DriverDAO {

    public boolean addDriver(Driver driver) {
        String sql = "INSERT INTO driver (name, licenseNumber, phone, vehicleType) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, driver.getName());
            pstmt.setString(2, driver.getLicenseNumber());
            pstmt.setString(3, driver.getPhone());
            pstmt.setString(4, driver.getVehicleType());

            boolean success = pstmt.executeUpdate() > 0;
            if (success)
                System.out.println("[DAO] Driver added successfully: " + driver.getName());
            return success;
        } catch (SQLException e) {
            System.err.println("[DAO] Error adding driver: " + e.getMessage());
            return false;
        }
    }

    public boolean updateDriver(Driver driver) {
        String sql = "UPDATE driver SET name = ?, licenseNumber = ?, phone = ?, vehicleType = ? WHERE driverId = ?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, driver.getName());
            pstmt.setString(2, driver.getLicenseNumber());
            pstmt.setString(3, driver.getPhone());
            pstmt.setString(4, driver.getVehicleType());
            pstmt.setInt(5, driver.getDriverId());

            boolean success = pstmt.executeUpdate() > 0;
            if (success)
                System.out.println("[DAO] Driver updated successfully: ID " + driver.getDriverId());
            return success;
        } catch (SQLException e) {
            System.err.println("[DAO] Error updating driver: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteDriver(int driverId) {
        String sql = "DELETE FROM driver WHERE driverId = ?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, driverId);
            boolean success = pstmt.executeUpdate() > 0;
            if (success)
                System.out.println("[DAO] Driver deleted successfully: ID " + driverId);
            return success;
        } catch (SQLException e) {
            System.err.println("[DAO] Error deleting driver: " + e.getMessage());
            return false;
        }
    }

    public List<Driver> getAllDrivers() {
        List<Driver> drivers = new ArrayList<>();
        String sql = "SELECT * FROM driver";
        Connection conn = DBConnection.getConnection();
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                drivers.add(new Driver(
                        rs.getInt("driverId"),
                        rs.getString("name"),
                        rs.getString("licenseNumber"),
                        rs.getString("phone"),
                        rs.getString("vehicleType")));
            }
        } catch (SQLException e) {
            System.err.println("[DAO] Error retrieving drivers: " + e.getMessage());
        }
        return drivers;
    }

    public Driver getDriverById(int id) {
        String sql = "SELECT * FROM driver WHERE driverId = ?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Driver(
                            rs.getInt("driverId"),
                            rs.getString("name"),
                            rs.getString("licenseNumber"),
                            rs.getString("phone"),
                            rs.getString("vehicleType"));
                }
            }
        } catch (SQLException e) {
            System.err.println("[DAO] Error retrieving driver by ID: " + e.getMessage());
        }
        return null;
    }
}
