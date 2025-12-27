# Haramaya University Vehicle Management System (HUVMS)

A professional Java Swing application with SQLite integration for managing vehicles, drivers, trips, and more.

## Features
- **Singleton DB Connection**: Ensures a single, persistent connection to the SQLite database.
- **Absolute Path Persistence**: Database is stored at `~/vms.db` to ensure records are saved and retrieved consistently across different execution contexts.
- **Full CRUD Operations**: Manage Vehicles, Drivers, Trips, Fuel Records, Maintenance, and Users.
- **Role-Based Access**: Admin and Staff roles with different permissions.
- **Modern Package Structure**: Organized under `com.vms` for professional standards.

## Project Structure
```text
HUVMS/
├── bin/                # Compiled .class files
├── lib/
│   └── sqlite-jdbc.jar # SQLite JDBC Driver
├── src/
│   └── com/vms/
│       ├── App.java    # Main entry point
│       ├── db/         # Singleton DBConnection
│       ├── dao/        # Data Access Objects (CRUD logic)
│       ├── models/     # Entity models
│       └── gui/        # Swing GUI forms
└── README.md
```

## How to Run

### 1. Compile
```bash
javac -d bin -cp "lib/sqlite-jdbc.jar" src/com/vms/models/*.java src/com/vms/db/*.java src/com/vms/dao/*.java src/com/vms/gui/*.java src/com/vms/App.java
```

### 2. Run
```bash
java -cp "bin:lib/sqlite-jdbc.jar" com.vms.App
```

## Default Credentials
- **Admin**: `admin` / `admin123`
- **Staff**: `staff` / `staff123`
