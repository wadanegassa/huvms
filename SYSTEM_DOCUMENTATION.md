# Haramaya University Vehicle Management System (HUVMS) - System Documentation

This document provides a comprehensive explanation of the HUVMS codebase, its architecture, data fetching mechanisms, and overall system workflow.

---

## 1. System Architecture

HUVMS is built using a **Layered Architecture** (specifically a simplified 3-tier architecture) to ensure separation of concerns, maintainability, and scalability.

### Layers:
1.  **Presentation Layer (`com.vms.gui`)**: Contains all Swing-based UI components (Forms, Dashboards, Dialogs).
2.  **Data Access Layer (`com.vms.dao`)**: Handles all interactions with the SQLite database.
3.  **Model Layer (`com.vms.models`)**: Defines the data structures (POJOs) used throughout the application.
4.  **Database Layer (`com.vms.db`)**: Manages the database connection and schema initialization.

---

## 2. Core Components & Workflow

### 2.1 Database Connectivity (`DBConnection.java`)
- **Singleton Pattern**: Ensures only one connection to the SQLite database exists at any time.
- **Persistence**: Uses an absolute path (`user.home/vms.db`) to ensure data persists across different execution environments.
- **Auto-Initialization**: Automatically creates tables and default users (`admin`, `staff`) if they don't exist.

### 2.2 Data Access Objects (DAOs)
Each entity (Vehicle, Driver, Trip, etc.) has a corresponding DAO class.
- **Data Fetching**: Uses JDBC `PreparedStatement` and `Statement` to execute SQL queries.
- **Mapping**: Converts database rows (`ResultSet`) into Java Objects (Models).
- **CRUD Operations**: Implements Create, Read, Update, and Delete logic.

**Example: `VehicleDAO.getAllVehicles()`**
1.  Gets the singleton connection from `DBConnection`.
2.  Executes `SELECT * FROM vehicle`.
3.  Iterates through the `ResultSet`.
4.  Instantiates a `Vehicle` object for each row and adds it to a `List`.

### 2.3 Role-Based Access Control (RBAC)
The system distinguishes between **ADMIN** and **STAFF** roles.
- **Admin**: Full access to all modules (Vehicles, Drivers, Users, Reports, etc.).
- **Staff**: Restricted access. Can only:
    - Submit Trip Requests.
    - View their own Requests.
    - View Trips assigned to them.
- **Implementation**: The `MainDashboard` filters the sidebar buttons based on the `currentUser.getRole()`.

---

## 3. Key Workflows Explained

### 3.1 Trip Approval Workflow
This is the most complex workflow in the system:
1.  **Request Submission**: A Staff member submits a request via `RequestManagementForm`.
2.  **Admin Review**: Admin sees the request in their dashboard.
3.  **Approval & Assignment**:
    - Admin selects the request and changes status to `APPROVED`.
    - A dialog pops up prompting the Admin to select an **Available Vehicle** and a **Driver**.
    - Upon confirmation:
        - A new record is inserted into the `trip` table.
        - The `vehicleRequest` status is updated to `APPROVED`.
        - The selected vehicle's status is updated to `On Trip`.
4.  **Visibility**: The `TripManagementForm` filters trips so Staff only see those where `requesterName` matches their username.

### 3.2 UI Modernization (`FlatLaf`)
- Uses the **FlatLaf** library for a modern, dark-themed look.
- **CardLayout**: The `MainDashboard` uses a `CardLayout` to switch between different management panels without opening new windows.
- **Custom Styling**: Uses `FlatClientProperties` to apply rounded corners (`arc`), custom backgrounds, and modern table styles.

---

## 4. File Structure Overview

- `src/com/vms/App.java`: Entry point. Initializes the UI theme and shows the Login form.
- `src/com/vms/models/`: Data models (Vehicle, Driver, Trip, User, etc.).
- `src/com/vms/dao/`: Database logic for each model.
- `src/com/vms/gui/`:
    - `LoginForm.java`: Handles authentication.
    - `MainDashboard.java`: The main shell with the sidebar and content area.
    - `*ManagementForm.java`: Individual panels for managing different entities.
- `lib/`: External libraries (`flatlaf.jar`, `sqlite-jdbc.jar`).

---

## 5. How Data Flows
1.  **User Action**: User clicks "Save" on a form.
2.  **GUI Layer**: Form collects data from text fields and creates a Model object.
3.  **DAO Layer**: Form calls `dao.addEntity(model)`.
4.  **DB Layer**: DAO executes SQL via `DBConnection`.
5.  **GUI Refresh**: Form calls `loadData()` to refresh the table with the latest database state.

---

## 6. Troubleshooting & Maintenance
- **Database Location**: The database file is located at `~/vms.db`.
- **Logs**: System actions and errors are logged to the console with prefixes like `[DAO]` or `[GUI]`.
- **Styling**: If UI elements look off, check the `FlatClientProperties.STYLE` strings in the GUI classes.
