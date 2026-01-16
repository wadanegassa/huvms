# HUVMS - System Documentation

## Project Overview
The **Haramaya University Vehicle Management System (HUVMS)** is a modern Java Swing application designed to manage vehicle fleets, driver assignments, trips, and maintenance logs. It features a role-based access system (Admin and Staff) and real-time data synchronization.

---

## 1. Technologies & Libraries
The project uses the following core technologies:
- **Java 17+**: Core programming language.
- **SQLite**: Lightweight, file-based relational database for data persistence.
- **FlatLaf (v3.x)**: A modern, high-performance look and feel for Swing, providing the dark theme and premium UI components.
- **SQLite JDBC Driver**: For connecting the Java application to the SQLite database.

---

## 2. Database Architecture
### Schema
The database (`vms.db`) consists of the following tables:
- `user`: Stores system users (Admin/Staff) with encrypted-like plain text passwords (for simplicity in this version).
- `vehicle`: Manages the fleet (Plate Number, Type, Status).
- `driver`: Stores driver details (Name, License, Phone).
- `trip`: Records completed or scheduled trips, linking vehicles and drivers.
- `vehicleRequest`: Handles staff requests for vehicles.
- `fuelRecord`: Tracks fuel consumption and costs.
- `maintenanceRecord`: Logs vehicle service history.

### Connection Management
The system uses a **Singleton Pattern** in `DBConnection.java` to ensure only one database connection is active at a time, preventing locking issues and optimizing performance.

---

## 3. Core Logic & Features
### Role-Based Access Control (RBAC)
- **Admin**: Full access to all management modules (Vehicles, Drivers, Users, etc.) and system-wide statistics.
- **Staff**: Limited access. Can only view their own requests, assigned trips, and available vehicles.

### Real-Time Data Polling
Instead of manual refresh buttons, the system implements **Auto-Refresh** using `javax.swing.Timer`:
- Every **5 seconds**, the active form re-fetches data from the database.
- **Visibility Listeners**: Timers only run when a tab is visible, saving CPU resources.
- **Dropdown Sync**: When adding a new vehicle or driver, dropdowns in other forms (like Trips) update automatically.

---

## 4. UI Design System
The UI is built with a "Premium Dark" aesthetic:
- **Custom Gradients**: Used in the Login Panel for a high-end feel.
- **FlatLaf Styling**: Extensive use of `FlatClientProperties` for:
    - **Rounded Corners (`arc`)**: Applied to cards, text fields, and buttons.
    - **Custom Borders**: Accent borders on dashboard cards.
    - **Placeholder Text**: Improved user guidance in input fields.
- **Responsive Layouts**: Use of `GridBagLayout` and `BorderLayout` to ensure the UI looks good on different screen sizes without excessive stretching.

---

## 5. Project Structure
- `com.vms.App`: Entry point of the application.
- `com.vms.gui`: All UI components (Forms, Dashboard, Login).
- `com.vms.dao`: Data Access Objects for database interaction.
- `com.vms.models`: Plain Old Java Objects (POJOs) representing data entities.
- `com.vms.db`: Database connection logic.

---

## 6. How to Run
1. Ensure `lib/flatlaf.jar` and `lib/sqlite-jdbc.jar` are in the classpath.
2. Compile all files in `src/`.
3. Run `com.vms.App`.
4. Default Credentials:
   - **Admin**: `admin` / `admin123`
   - **Staff**: `staff` / `staff123`
