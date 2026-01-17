package com.vms.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.File;

/**
 * DBConnection class implementing the Singleton pattern for SQLite database
 * connectivity.
 * Uses an absolute path to ensure records persist in the same file regardless
 * of execution context.
 */
public class DBConnection {
    // Using an absolute path for the database file
    private static final String DB_FILENAME = "vms.db";
    private static final String DB_PATH = System.getProperty("user.home") + File.separator + DB_FILENAME;
    private static final String URL = "jdbc:sqlite:" + DB_PATH;

    private static Connection connection = null;

    // Private constructor to prevent instantiation
    private DBConnection() {
    }

    /**
     * Returns the single instance of the database connection.
     * 
     * @return Connection object
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // Load SQLite JDBC driver
                Class.forName("org.sqlite.JDBC");

                // Print the absolute path for verification
                System.out.println("Using Database File: " + DB_PATH);

                connection = DriverManager.getConnection(URL);

                // Ensure auto-commit is enabled for persistence
                connection.setAutoCommit(true);

                System.out.println("Successfully connected to SQLite database.");
                initializeDatabase();
            }
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
        }
        return connection;
    }

    /**
     * Initializes the database schema if tables do not exist.
     */
    private static void initializeDatabase() {
        String[] queries = {
                "CREATE TABLE IF NOT EXISTS vehicle (vehicleId INTEGER PRIMARY KEY AUTOINCREMENT, plateNumber TEXT NOT NULL UNIQUE, vehicleType TEXT NOT NULL, status TEXT NOT NULL)",
                "CREATE TABLE IF NOT EXISTS driver (driverId INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, licenseNumber TEXT NOT NULL UNIQUE, phone TEXT NOT NULL, vehicleType TEXT)",
                "CREATE TABLE IF NOT EXISTS trip (tripId INTEGER PRIMARY KEY AUTOINCREMENT, vehicleId INTEGER, driverId INTEGER, destination TEXT NOT NULL, date TEXT NOT NULL, distance REAL NOT NULL, requesterName TEXT NOT NULL, FOREIGN KEY (vehicleId) REFERENCES vehicle(vehicleId), FOREIGN KEY (driverId) REFERENCES driver(driverId))",
                "CREATE TABLE IF NOT EXISTS fuelRecord (recordId INTEGER PRIMARY KEY AUTOINCREMENT, vehicleId INTEGER, amount REAL NOT NULL, cost REAL NOT NULL, date TEXT NOT NULL, FOREIGN KEY (vehicleId) REFERENCES vehicle(vehicleId))",
                "CREATE TABLE IF NOT EXISTS maintenanceRecord (recordId INTEGER PRIMARY KEY AUTOINCREMENT, vehicleId INTEGER, description TEXT NOT NULL, date TEXT NOT NULL, cost REAL NOT NULL, FOREIGN KEY (vehicleId) REFERENCES vehicle(vehicleId))",
                "CREATE TABLE IF NOT EXISTS vehicleRequest (requestId INTEGER PRIMARY KEY AUTOINCREMENT, staffName TEXT NOT NULL, destination TEXT NOT NULL, date TEXT NOT NULL, distance REAL NOT NULL, status TEXT NOT NULL)",
                "CREATE TABLE IF NOT EXISTS user (userId INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT NOT NULL UNIQUE, password TEXT NOT NULL, role TEXT NOT NULL)",
                "INSERT OR IGNORE INTO user (username, password, role) VALUES ('admin', 'admin123', 'ADMIN')",
                "INSERT OR IGNORE INTO user (username, password, role) VALUES ('staff', 'staff123', 'STAFF')",
                // Migration: Add vehicleType to driver if it doesn't exist
                "ALTER TABLE driver ADD COLUMN vehicleType TEXT DEFAULT 'Bus'"
        };

        try (Statement stmt = connection.createStatement()) {
            for (String sql : queries) {
                stmt.execute(sql);
            }
            System.out.println("Database schema verified/initialized.");
        } catch (SQLException e) {
            System.err.println("Error initializing database tables: " + e.getMessage());
        }
    }

    /**
     * Safely closes the database connection.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Database connection closed safely.");
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
}
