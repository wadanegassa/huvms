package com.vms.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import com.vms.dao.TripDAO;
import com.vms.dao.VehicleDAO;
import com.vms.dao.DriverDAO;
import com.vms.models.Trip;
import com.vms.models.Vehicle;
import com.vms.models.Driver;
import com.vms.models.User;
import com.formdev.flatlaf.FlatClientProperties;

/**
 * Modern TripManagementForm for CRUD operations on trips.
 */
public class TripManagementForm extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<Vehicle> comboVehicle;
    private JComboBox<Driver> comboDriver;
    private JTextField txtDestination, txtDate, txtDistance, txtRequester;
    private JButton btnAdd, btnUpdate, btnDelete;
    private TripDAO tripDAO;
    private VehicleDAO vehicleDAO;
    private DriverDAO driverDAO;
    private User currentUser;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public TripManagementForm(User user) {
        this.currentUser = user;
        this.tripDAO = new TripDAO();
        this.vehicleDAO = new VehicleDAO();
        this.driverDAO = new DriverDAO();
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Table Section
        tableModel = new DefaultTableModel(
                new Object[] { "ID", "Vehicle", "Driver", "Destination", "Date", "Distance", "Requester" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.getTableHeader().setReorderingAllowed(false);
        table.putClientProperty(FlatClientProperties.STYLE, "showHorizontalLines: true");

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        // Input Section
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1.0;

        // Row 1: Vehicle & Driver
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Assigned Vehicle"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Assigned Driver"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        comboVehicle = new JComboBox<>();
        comboVehicle.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        loadVehicles();
        inputPanel.add(comboVehicle, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        comboDriver = new JComboBox<>();
        comboDriver.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        loadDrivers();
        inputPanel.add(comboDriver, gbc);

        // Row 2: Destination & Date
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Destination"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Date (yyyy-MM-dd HH:mm:ss)"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        txtDestination = new JTextField();
        txtDestination.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        inputPanel.add(txtDestination, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        txtDate = new JTextField(dateFormat.format(new Date()));
        txtDate.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        inputPanel.add(txtDate, gbc);

        // Row 3: Distance & Requester
        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(new JLabel("Distance (km)"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 4;
        inputPanel.add(new JLabel("Requester Name"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        txtDistance = new JTextField();
        txtDistance.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        inputPanel.add(txtDistance, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        txtRequester = new JTextField();
        txtRequester.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        inputPanel.add(txtRequester, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        btnAdd = new JButton("Create Trip");
        styleButton(btnAdd, "#0078d7", Color.WHITE);

        btnUpdate = new JButton("Update Trip");
        styleButton(btnUpdate, "#2d2d2d", Color.LIGHT_GRAY);

        btnDelete = new JButton("Cancel Trip");
        styleButton(btnDelete, "#d11a2a", Color.WHITE);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(inputPanel, BorderLayout.CENTER);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);

        if ("STAFF".equals(currentUser.getRole())) {
            southPanel.setVisible(false);
        }

        // Listeners
        btnAdd.addActionListener(e -> addTrip());
        btnUpdate.addActionListener(e -> updateTrip());
        btnDelete.addActionListener(e -> deleteTrip());
    }

    private void styleButton(JButton btn, String bgColor, Color fgColor) {
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("Inter", Font.BOLD, 13));
        btn.setForeground(fgColor);
        btn.putClientProperty(FlatClientProperties.STYLE,
                "background: " + bgColor + "; margin: 5,15,5,15");
    }

    private void loadVehicles() {
        List<Vehicle> vehicles = vehicleDAO.getAllVehicles();
        for (Vehicle v : vehicles)
            comboVehicle.addItem(v);
    }

    private void loadDrivers() {
        List<Driver> drivers = driverDAO.getAllDrivers();
        for (Driver d : drivers)
            comboDriver.addItem(d);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<Trip> trips = tripDAO.getAllTrips();
        for (Trip t : trips) {
            if ("ADMIN".equals(currentUser.getRole()) || t.getRequesterName().equals(currentUser.getUsername())) {
                tableModel.addRow(new Object[] {
                        t.getTripId(), t.getVehicle().getPlateNumber(), t.getDriver().getName(),
                        t.getDestination(), dateFormat.format(t.getDate()), t.getDistance(), t.getRequesterName()
                });
            }
        }
    }

    private void addTrip() {
        try {
            Trip t = new Trip(0, (Vehicle) comboVehicle.getSelectedItem(), (Driver) comboDriver.getSelectedItem(),
                    txtDestination.getText(), dateFormat.parse(txtDate.getText()),
                    Double.parseDouble(txtDistance.getText()), txtRequester.getText());
            if (tripDAO.addTrip(t)) {
                loadData();
                clearInputs();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void updateTrip() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;
        int id = (int) table.getValueAt(row, 0);
        try {
            Trip t = new Trip(id, (Vehicle) comboVehicle.getSelectedItem(), (Driver) comboDriver.getSelectedItem(),
                    txtDestination.getText(), dateFormat.parse(txtDate.getText()),
                    Double.parseDouble(txtDistance.getText()), txtRequester.getText());
            if (tripDAO.updateTrip(t)) {
                loadData();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void deleteTrip() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;
        int id = (int) table.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Cancel this trip?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (tripDAO.deleteTrip(id)) {
                loadData();
                clearInputs();
            }
        }
    }

    private void clearInputs() {
        txtDestination.setText("");
        txtDistance.setText("");
        txtRequester.setText("");
        table.clearSelection();
    }
}
