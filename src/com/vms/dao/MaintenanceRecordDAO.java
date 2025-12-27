package com.vms.dao;

import com.vms.db.DBConnection;
import com.vms.models.MaintenanceRecord;
import com.vms.models.Vehicle;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * MaintenanceRecordDAO class for performing CRUD operations on the
 * maintenanceRecord table.
 */
public class MaintenanceRecordDAO {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private VehicleDAO vehicleDAO = new VehicleDAO();

    public boolean addMaintenanceRecord(MaintenanceRecord record) {
        String sql = "INSERT INTO maintenanceRecord (vehicleId, description, date, cost) VALUES (?, ?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, record.getVehicle().getVehicleId());
            pstmt.setString(2, record.getDescription());
            pstmt.setString(3, dateFormat.format(record.getDate()));
            pstmt.setDouble(4, record.getCost());

            boolean success = pstmt.executeUpdate() > 0;
            if (success)
                System.out.println("[DAO] Maintenance record added successfully.");
            return success;
        } catch (SQLException e) {
            System.err.println("[DAO] Error adding maintenance record: " + e.getMessage());
            return false;
        }
    }

    public List<MaintenanceRecord> getAllMaintenanceRecords() {
        List<MaintenanceRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM maintenanceRecord";
        Connection conn = DBConnection.getConnection();
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                records.add(mapResultSetToMaintenanceRecord(rs));
            }
        } catch (SQLException | ParseException e) {
            System.err.println("[DAO] Error retrieving maintenance records: " + e.getMessage());
        }
        return records;
    }

    public boolean updateMaintenanceRecord(MaintenanceRecord record) {
        String sql = "UPDATE maintenanceRecord SET vehicleId = ?, description = ?, date = ?, cost = ? WHERE recordId = ?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, record.getVehicle().getVehicleId());
            pstmt.setString(2, record.getDescription());
            pstmt.setString(3, dateFormat.format(record.getDate()));
            pstmt.setDouble(4, record.getCost());
            pstmt.setInt(5, record.getRecordId());

            boolean success = pstmt.executeUpdate() > 0;
            if (success)
                System.out.println("[DAO] Maintenance record updated successfully.");
            return success;
        } catch (SQLException e) {
            System.err.println("[DAO] Error updating maintenance record: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteMaintenanceRecord(int recordId) {
        String sql = "DELETE FROM maintenanceRecord WHERE recordId = ?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, recordId);
            boolean success = pstmt.executeUpdate() > 0;
            if (success)
                System.out.println("[DAO] Maintenance record deleted successfully.");
            return success;
        } catch (SQLException e) {
            System.err.println("[DAO] Error deleting maintenance record: " + e.getMessage());
            return false;
        }
    }

    private MaintenanceRecord mapResultSetToMaintenanceRecord(ResultSet rs) throws SQLException, ParseException {
        int id = rs.getInt("recordId");
        int vehicleId = rs.getInt("vehicleId");
        String description = rs.getString("description");
        String dateStr = rs.getString("date");
        double cost = rs.getDouble("cost");

        Vehicle vehicle = vehicleDAO.getVehicleById(vehicleId);

        return new MaintenanceRecord(id, vehicle, description, dateFormat.parse(dateStr), cost);
    }
}
