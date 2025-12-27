package com.vms.dao;

import com.vms.db.DBConnection;
import com.vms.models.Trip;
import com.vms.models.Vehicle;
import com.vms.models.Driver;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * TripDAO class for performing CRUD operations on the trip table.
 */
public class TripDAO {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private VehicleDAO vehicleDAO = new VehicleDAO();
    private DriverDAO driverDAO = new DriverDAO();

    public boolean addTrip(Trip trip) {
        String sql = "INSERT INTO trip (vehicleId, driverId, destination, date, distance, requesterName) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, trip.getVehicle().getVehicleId());
            pstmt.setInt(2, trip.getDriver().getDriverId());
            pstmt.setString(3, trip.getDestination());
            pstmt.setString(4, dateFormat.format(trip.getDate()));
            pstmt.setDouble(5, trip.getDistance());
            pstmt.setString(6, trip.getRequesterName());

            boolean success = pstmt.executeUpdate() > 0;
            if (success)
                System.out.println("[DAO] Trip added successfully.");
            return success;
        } catch (SQLException e) {
            System.err.println("[DAO] Error adding trip: " + e.getMessage());
            return false;
        }
    }

    public List<Trip> getAllTrips() {
        List<Trip> trips = new ArrayList<>();
        String sql = "SELECT * FROM trip";
        Connection conn = DBConnection.getConnection();
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                trips.add(mapResultSetToTrip(rs));
            }
        } catch (SQLException | ParseException e) {
            System.err.println("[DAO] Error retrieving trips: " + e.getMessage());
        }
        return trips;
    }

    public boolean updateTrip(Trip trip) {
        String sql = "UPDATE trip SET vehicleId = ?, driverId = ?, destination = ?, date = ?, distance = ?, requesterName = ? WHERE tripId = ?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, trip.getVehicle().getVehicleId());
            pstmt.setInt(2, trip.getDriver().getDriverId());
            pstmt.setString(3, trip.getDestination());
            pstmt.setString(4, dateFormat.format(trip.getDate()));
            pstmt.setDouble(5, trip.getDistance());
            pstmt.setString(6, trip.getRequesterName());
            pstmt.setInt(7, trip.getTripId());

            boolean success = pstmt.executeUpdate() > 0;
            if (success)
                System.out.println("[DAO] Trip updated successfully.");
            return success;
        } catch (SQLException e) {
            System.err.println("[DAO] Error updating trip: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteTrip(int tripId) {
        String sql = "DELETE FROM trip WHERE tripId = ?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, tripId);
            boolean success = pstmt.executeUpdate() > 0;
            if (success)
                System.out.println("[DAO] Trip deleted successfully.");
            return success;
        } catch (SQLException e) {
            System.err.println("[DAO] Error deleting trip: " + e.getMessage());
            return false;
        }
    }

    private Trip mapResultSetToTrip(ResultSet rs) throws SQLException, ParseException {
        int id = rs.getInt("tripId");
        int vehicleId = rs.getInt("vehicleId");
        int driverId = rs.getInt("driverId");
        String destination = rs.getString("destination");
        String dateStr = rs.getString("date");
        double distance = rs.getDouble("distance");
        String requester = rs.getString("requesterName");

        Vehicle vehicle = vehicleDAO.getVehicleById(vehicleId);
        Driver driver = driverDAO.getDriverById(driverId);

        return new Trip(id, vehicle, driver, destination, dateFormat.parse(dateStr), distance, requester);
    }
}
