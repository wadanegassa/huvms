-- SQLite Schema for Vehicle Management System (VMS)

-- Vehicle Table
CREATE TABLE IF NOT EXISTS vehicle (
    vehicleId INTEGER PRIMARY KEY AUTOINCREMENT,
    plateNumber TEXT NOT NULL UNIQUE,
    vehicleType TEXT NOT NULL,
    status TEXT NOT NULL
);

-- Driver Table
CREATE TABLE IF NOT EXISTS driver (
    driverId INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    licenseNumber TEXT NOT NULL UNIQUE,
    phone TEXT NOT NULL
);

-- Trip Table
CREATE TABLE IF NOT EXISTS trip (
    tripId INTEGER PRIMARY KEY AUTOINCREMENT,
    vehicleId INTEGER,
    driverId INTEGER,
    destination TEXT NOT NULL,
    date DATETIME NOT NULL,
    distance REAL NOT NULL,
    requesterName TEXT NOT NULL,
    FOREIGN KEY (vehicleId) REFERENCES vehicle(vehicleId),
    FOREIGN KEY (driverId) REFERENCES driver(driverId)
);

-- FuelRecord Table
CREATE TABLE IF NOT EXISTS fuelRecord (
    recordId INTEGER PRIMARY KEY AUTOINCREMENT,
    vehicleId INTEGER,
    amount REAL NOT NULL,
    cost REAL NOT NULL,
    date DATETIME NOT NULL,
    FOREIGN KEY (vehicleId) REFERENCES vehicle(vehicleId)
);

-- MaintenanceRecord Table
CREATE TABLE IF NOT EXISTS maintenanceRecord (
    recordId INTEGER PRIMARY KEY AUTOINCREMENT,
    vehicleId INTEGER,
    description TEXT NOT NULL,
    date DATETIME NOT NULL,
    cost REAL NOT NULL,
    FOREIGN KEY (vehicleId) REFERENCES vehicle(vehicleId)
);

-- VehicleRequest Table
CREATE TABLE IF NOT EXISTS vehicleRequest (
    requestId INTEGER PRIMARY KEY AUTOINCREMENT,
    staffName TEXT NOT NULL,
    destination TEXT NOT NULL,
    date DATETIME NOT NULL,
    distance REAL NOT NULL,
    status TEXT NOT NULL,
    vehicleId INTEGER,
    driverId INTEGER,
    FOREIGN KEY (vehicleId) REFERENCES vehicle(vehicleId),
    FOREIGN KEY (driverId) REFERENCES driver(driverId)
);

-- User Table
CREATE TABLE IF NOT EXISTS user (
    userId INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    role TEXT NOT NULL
);

-- Insert default admin user
INSERT OR IGNORE INTO user (username, password, role) VALUES ('admin', 'admin123', 'ADMIN');
INSERT OR IGNORE INTO user (username, password, role) VALUES ('staff', 'staff123', 'STAFF');
