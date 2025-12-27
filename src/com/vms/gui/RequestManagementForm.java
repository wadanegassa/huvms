package com.vms.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import com.vms.dao.VehicleRequestDAO;
import com.vms.dao.TripDAO;
import com.vms.dao.VehicleDAO;
import com.vms.dao.DriverDAO;
import com.vms.models.VehicleRequest;
import com.vms.models.Trip;
import com.vms.models.Vehicle;
import com.vms.models.Driver;
import com.vms.models.User;
import com.formdev.flatlaf.FlatClientProperties;

/**
 * Modern RequestManagementForm for CRUD operations on vehicle requests.
 */
public class RequestManagementForm extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtStaff, txtDestination, txtDate, txtDistance;
    private JComboBox<String> comboStatus;
    private JButton btnAdd, btnUpdate, btnDelete;
    private VehicleRequestDAO requestDAO;
    private TripDAO tripDAO;
    private VehicleDAO vehicleDAO;
    private DriverDAO driverDAO;
    private User currentUser;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public RequestManagementForm(User user) {
        this.currentUser = user;
        this.requestDAO = new VehicleRequestDAO();
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
                new Object[] { "ID", "Staff Name", "Destination", "Date Requested", "Distance (km)", "Status" }, 0) {
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

        // Row 1: Staff & Destination
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Staff Name"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Destination"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        txtStaff = new JTextField();
        txtStaff.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        inputPanel.add(txtStaff, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        txtDestination = new JTextField();
        txtDestination.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        inputPanel.add(txtDestination, gbc);

        // Row 2: Date & Distance
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Date (yyyy-MM-dd HH:mm:ss)"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Distance (km)"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        txtDate = new JTextField(dateFormat.format(new Date()));
        txtDate.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        inputPanel.add(txtDate, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        txtDistance = new JTextField();
        txtDistance.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        inputPanel.add(txtDistance, gbc);

        // Row 3: Status
        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(new JLabel("Request Status"), gbc);
        gbc.gridx = 0;
        gbc.gridy = 5;
        comboStatus = new JComboBox<>(new String[] { "PENDING", "APPROVED", "REJECTED" });
        comboStatus.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        inputPanel.add(comboStatus, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        btnAdd = new JButton("Submit Request");
        styleButton(btnAdd, "#0078d7", Color.WHITE);

        btnUpdate = new JButton("Update Status");
        styleButton(btnUpdate, "#2d2d2d", Color.LIGHT_GRAY);

        btnDelete = new JButton("Cancel");
        styleButton(btnDelete, "#d11a2a", Color.WHITE);

        // Role-based restrictions
        if ("STAFF".equals(currentUser.getRole())) {
            txtStaff.setText(currentUser.getUsername());
            txtStaff.setEditable(false);
            comboStatus.setEnabled(false);
            btnUpdate.setVisible(false);
            btnDelete.setVisible(false);
        }

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(inputPanel, BorderLayout.CENTER);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);

        // Listeners
        btnAdd.addActionListener(e -> addRequest());
        btnUpdate.addActionListener(e -> updateRequest());
        btnDelete.addActionListener(e -> deleteRequest());

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                txtStaff.setText(table.getValueAt(row, 1).toString());
                txtDestination.setText(table.getValueAt(row, 2).toString());
                txtDate.setText(table.getValueAt(row, 3).toString());
                txtDistance.setText(table.getValueAt(row, 4).toString());
                comboStatus.setSelectedItem(table.getValueAt(row, 5).toString());
            }
        });
    }

    private void styleButton(JButton btn, String bgColor, Color fgColor) {
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("Inter", Font.BOLD, 13));
        btn.setForeground(fgColor);
        btn.putClientProperty(FlatClientProperties.STYLE,
                "background: " + bgColor + "; margin: 5,15,5,15");
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<VehicleRequest> requests = requestDAO.getAllRequests();
        for (VehicleRequest r : requests) {
            if ("ADMIN".equals(currentUser.getRole()) || r.getStaffName().equals(currentUser.getUsername())) {
                tableModel.addRow(new Object[] {
                        r.getRequestId(), r.getStaffName(), r.getDestination(), dateFormat.format(r.getDate()),
                        r.getDistance(), r.getStatus()
                });
            }
        }
    }

    private void addRequest() {
        try {
            VehicleRequest r = new VehicleRequest(0, txtStaff.getText(), txtDestination.getText(),
                    dateFormat.parse(txtDate.getText()), Double.parseDouble(txtDistance.getText()),
                    (String) comboStatus.getSelectedItem());
            if (requestDAO.addRequest(r)) {
                loadData();
                clearInputs();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void updateRequest() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;
        int id = (int) table.getValueAt(row, 0);
        String oldStatus = table.getValueAt(row, 5).toString();
        String newStatus = (String) comboStatus.getSelectedItem();

        try {
            VehicleRequest r = new VehicleRequest(id, txtStaff.getText(), txtDestination.getText(),
                    dateFormat.parse(txtDate.getText()), Double.parseDouble(txtDistance.getText()),
                    newStatus);

            if ("APPROVED".equals(newStatus) && !"APPROVED".equals(oldStatus)) {
                assignTripResources(r);
            } else {
                if (requestDAO.updateRequest(r)) {
                    loadData();
                    JOptionPane.showMessageDialog(this, "Request status updated to " + newStatus);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void assignTripResources(VehicleRequest r) {
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        JComboBox<Vehicle> vehicleCombo = new JComboBox<>();
        JComboBox<Driver> driverCombo = new JComboBox<>();

        List<Vehicle> vehicles = vehicleDAO.getAllVehicles();
        List<Driver> drivers = driverDAO.getAllDrivers();

        for (Vehicle v : vehicles) {
            if ("Available".equalsIgnoreCase(v.getStatus())) {
                vehicleCombo.addItem(v);
            }
        }
        for (Driver d : drivers) {
            driverCombo.addItem(d);
        }

        if (vehicleCombo.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "No available vehicles to assign!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        panel.add(new JLabel("Select Vehicle:"));
        panel.add(vehicleCombo);
        panel.add(new JLabel("Select Driver:"));
        panel.add(driverCombo);

        int result = JOptionPane.showConfirmDialog(this, panel, "Assign Resources for Trip",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            Vehicle selectedVehicle = (Vehicle) vehicleCombo.getSelectedItem();
            Driver selectedDriver = (Driver) driverCombo.getSelectedItem();

            Trip trip = new Trip(0, selectedVehicle, selectedDriver, r.getDestination(), r.getDate(), r.getDistance(),
                    r.getStaffName());

            if (tripDAO.addTrip(trip)) {
                // Update request status
                r.setStatus("APPROVED");
                requestDAO.updateRequest(r);

                // Update vehicle status to 'On Trip'
                selectedVehicle.setStatus("On Trip");
                vehicleDAO.updateVehicle(selectedVehicle);

                loadData();
                JOptionPane.showMessageDialog(this, "Trip assigned and request approved successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create trip record.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteRequest() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;
        int id = (int) table.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Cancel this request?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (requestDAO.deleteRequest(id)) {
                loadData();
                clearInputs();
            }
        }
    }

    private void clearInputs() {
        txtStaff.setText("");
        txtDestination.setText("");
        txtDistance.setText("");
        table.clearSelection();
    }
}
