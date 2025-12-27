package com.vms.dao;

import com.vms.db.DBConnection;
import com.vms.models.Vehicle;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * VehicleDAO class for performing CRUD operations on the vehicle table.
 * Ensures that all operations are persisted to the SQLite database.
 */
public class VehicleDAO {

    /**
     * Adds a new vehicle to the database.
     * 
     * @param vehicle The vehicle object to add
     * @return true if successful, false otherwise
     */
    public boolean addVehicle(Vehicle vehicle) {
        String sql = "INSERT INTO vehicle (plateNumber, vehicleType, status) VALUES (?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, vehicle.getPlateNumber());
            pstmt.setString(2, vehicle.getVehicleType());
            pstmt.setString(3, vehicle.getStatus());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("[DAO] Vehicle added successfully: " + vehicle.getPlateNumber());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[DAO] Error adding vehicle: " + e.getMessage());
        }
        return false;
    }

    /**
     * Updates an existing vehicle in the database.
     * 
     * @param vehicle The vehicle object with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateVehicle(Vehicle vehicle) {
        String sql = "UPDATE vehicle SET plateNumber = ?, vehicleType = ?, status = ? WHERE vehicleId = ?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, vehicle.getPlateNumber());
            pstmt.setString(2, vehicle.getVehicleType());
            pstmt.setString(3, vehicle.getStatus());
            pstmt.setInt(4, vehicle.getVehicleId());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("[DAO] Vehicle updated successfully: ID " + vehicle.getVehicleId());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[DAO] Error updating vehicle: " + e.getMessage());
        }
        return false;
    }

    /**
     * Deletes a vehicle from the database by ID.
     * 
     * @param vehicleId The ID of the vehicle to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteVehicle(int vehicleId) {
        String sql = "DELETE FROM vehicle WHERE vehicleId = ?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, vehicleId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("[DAO] Vehicle deleted successfully: ID " + vehicleId);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[DAO] Error deleting vehicle: " + e.getMessage());
        }
        return false;
    }

    /**
     * Retrieves all vehicles from the database and prints them for verification.
     * 
     * @return List of Vehicle objects
     */
    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicle";
        Connection conn = DBConnection.getConnection();
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("[DAO] Fetching all vehicles from database...");
            while (rs.next()) {
                Vehicle v = new Vehicle(
                        rs.getInt("vehicleId"),
                        rs.getString("plateNumber"),
                        rs.getString("vehicleType"),
                        rs.getString("status"));
                vehicles.add(v);
                System.out.println("  - " + v);
            }
            System.out.println("[DAO] Total vehicles found: " + vehicles.size());
        } catch (SQLException e) {
            System.err.println("[DAO] Error retrieving vehicles: " + e.getMessage());
        }
        return vehicles;
    }

    /**
     * Retrieves a vehicle by its ID.
     * 
     * @param id The ID of the vehicle to retrieve
     * @return Vehicle object or null if not found
     */
    public Vehicle getVehicleById(int id) {
        String sql = "SELECT * FROM vehicle WHERE vehicleId = ?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Vehicle(
                            rs.getInt("vehicleId"),
                            rs.getString("plateNumber"),
                            rs.getString("vehicleType"),
                            rs.getString("status"));
                }
            }
        } catch (SQLException e) {
            System.err.println("[DAO] Error retrieving vehicle by ID: " + e.getMessage());
        }
        return null;
    }
}
