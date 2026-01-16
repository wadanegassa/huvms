package com.vms.dao;

import com.vms.db.DBConnection;
import com.vms.models.VehicleRequest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * VehicleRequestDAO class for performing CRUD operations on the vehicleRequest
 * table.
 */
public class VehicleRequestDAO {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public boolean addRequest(VehicleRequest request) {
        String sql = "INSERT INTO vehicleRequest (staffName, destination, date, distance, status) VALUES (?, ?, ?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, request.getStaffName());
            pstmt.setString(2, request.getDestination());
            pstmt.setString(3, dateFormat.format(request.getDate()));
            pstmt.setDouble(4, request.getDistance());
            pstmt.setString(5, request.getStatus());

            boolean success = pstmt.executeUpdate() > 0;
            if (success)
                System.out.println("[DAO] Vehicle request added successfully.");
            return success;
        } catch (SQLException e) {
            System.err.println("[DAO] Error adding request: " + e.getMessage());
            return false;
        }
    }

    public List<VehicleRequest> getAllRequests() {
        List<VehicleRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM vehicleRequest";
        Connection conn = DBConnection.getConnection();
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                requests.add(new VehicleRequest(
                        rs.getInt("requestId"),
                        rs.getString("staffName"),
                        rs.getString("destination"),
                        dateFormat.parse(rs.getString("date")),
                        rs.getDouble("distance"),
                        rs.getString("status")));
            }
        } catch (SQLException | ParseException e) {
            System.err.println("[DAO] Error retrieving requests: " + e.getMessage());
        }
        return requests;
    }

    public boolean updateRequest(VehicleRequest request) {
        String sql = "UPDATE vehicleRequest SET staffName = ?, destination = ?, date = ?, distance = ?, status = ? WHERE requestId = ?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, request.getStaffName());
            pstmt.setString(2, request.getDestination());
            pstmt.setString(3, dateFormat.format(request.getDate()));
            pstmt.setDouble(4, request.getDistance());
            pstmt.setString(5, request.getStatus());
            pstmt.setInt(6, request.getRequestId());

            boolean success = pstmt.executeUpdate() > 0;
            if (success)
                System.out.println("[DAO] Vehicle request updated successfully.");
            return success;
        } catch (SQLException e) {
            System.err.println("[DAO] Error updating request: " + e.getMessage());
            return false;
        }
    }

    public boolean updateRequestStatus(int requestId, String status) {
        String sql = "UPDATE vehicleRequest SET status = ? WHERE requestId = ?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, requestId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[DAO] Error updating request status: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteRequest(int requestId) {
        String sql = "DELETE FROM vehicleRequest WHERE requestId = ?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, requestId);
            boolean success = pstmt.executeUpdate() > 0;
            if (success)
                System.out.println("[DAO] Vehicle request deleted successfully.");
            return success;
        } catch (SQLException e) {
            System.err.println("[DAO] Error deleting request: " + e.getMessage());
            return false;
        }
    }
}
