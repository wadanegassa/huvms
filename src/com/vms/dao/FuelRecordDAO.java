package com.vms.dao;

import com.vms.db.DBConnection;
import com.vms.models.FuelRecord;
import com.vms.models.Vehicle;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * FuelRecordDAO class for performing CRUD operations on the fuelRecord table.
 */
public class FuelRecordDAO {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private VehicleDAO vehicleDAO = new VehicleDAO();

    public boolean addFuelRecord(FuelRecord record) {
        String sql = "INSERT INTO fuelRecord (vehicleId, amount, cost, date) VALUES (?, ?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, record.getVehicle().getVehicleId());
            pstmt.setDouble(2, record.getAmount());
            pstmt.setDouble(3, record.getCost());
            pstmt.setString(4, dateFormat.format(record.getDate()));

            boolean success = pstmt.executeUpdate() > 0;
            if (success)
                System.out.println("[DAO] Fuel record added successfully.");
            return success;
        } catch (SQLException e) {
            System.err.println("[DAO] Error adding fuel record: " + e.getMessage());
            return false;
        }
    }

    public List<FuelRecord> getAllFuelRecords() {
        List<FuelRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM fuelRecord";
        Connection conn = DBConnection.getConnection();
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                records.add(mapResultSetToFuelRecord(rs));
            }
        } catch (SQLException | ParseException e) {
            System.err.println("[DAO] Error retrieving fuel records: " + e.getMessage());
        }
        return records;
    }

    public boolean updateFuelRecord(FuelRecord record) {
        String sql = "UPDATE fuelRecord SET vehicleId = ?, amount = ?, cost = ?, date = ? WHERE recordId = ?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, record.getVehicle().getVehicleId());
            pstmt.setDouble(2, record.getAmount());
            pstmt.setDouble(3, record.getCost());
            pstmt.setString(4, dateFormat.format(record.getDate()));
            pstmt.setInt(5, record.getRecordId());

            boolean success = pstmt.executeUpdate() > 0;
            if (success)
                System.out.println("[DAO] Fuel record updated successfully.");
            return success;
        } catch (SQLException e) {
            System.err.println("[DAO] Error updating fuel record: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteFuelRecord(int recordId) {
        String sql = "DELETE FROM fuelRecord WHERE recordId = ?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, recordId);
            boolean success = pstmt.executeUpdate() > 0;
            if (success)
                System.out.println("[DAO] Fuel record deleted successfully.");
            return success;
        } catch (SQLException e) {
            System.err.println("[DAO] Error deleting fuel record: " + e.getMessage());
            return false;
        }
    }

    private FuelRecord mapResultSetToFuelRecord(ResultSet rs) throws SQLException, ParseException {
        int id = rs.getInt("recordId");
        int vehicleId = rs.getInt("vehicleId");
        double amount = rs.getDouble("amount");
        double cost = rs.getDouble("cost");
        String dateStr = rs.getString("date");

        Vehicle vehicle = vehicleDAO.getVehicleById(vehicleId);

        return new FuelRecord(id, vehicle, amount, cost, dateFormat.parse(dateStr));
    }
}
