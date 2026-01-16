package com.vms.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import com.vms.dao.VehicleRequestDAO;
import com.vms.dao.VehicleDAO;
import com.vms.dao.DriverDAO;
import com.vms.dao.TripDAO;
import com.vms.models.VehicleRequest;
import com.vms.models.Vehicle;
import com.vms.models.Driver;
import com.vms.models.Trip;
import com.vms.models.User;
import com.formdev.flatlaf.FlatClientProperties;

/**
 * Modern RequestManagementForm for CRUD operations on vehicle requests.
 */
public class RequestManagementForm extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtRequester, txtDestination, txtDistance, txtDate;
    private JButton btnRequest, btnApprove, btnReject;
    private VehicleRequestDAO requestDAO;
    private VehicleDAO vehicleDAO;
    private DriverDAO driverDAO;
    private TripDAO tripDAO;
    private User currentUser;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Timer refreshTimer;

    public RequestManagementForm(User user) {
        this.currentUser = user;
        requestDAO = new VehicleRequestDAO();
        vehicleDAO = new VehicleDAO();
        driverDAO = new DriverDAO();
        tripDAO = new TripDAO();
        initComponents();
        loadData();
        startAutoRefresh();
    }

    private void startAutoRefresh() {
        refreshTimer = new Timer(5000, e -> loadData());
        refreshTimer.start();

        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                loadData();
                refreshTimer.start();
            }

            @Override
            public void componentHidden(java.awt.event.ComponentEvent e) {
                refreshTimer.stop();
            }
        });
    }

    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(30, 30, 30, 30));
        setBackground(new Color(25, 25, 25));

        // Header
        JLabel title = new JLabel("Vehicle Requests");
        title.setFont(new Font("Inter", Font.BOLD, 24));
        title.setForeground(new Color(220, 220, 220));
        add(title, BorderLayout.NORTH);

        // Table Section
        tableModel = new DefaultTableModel(
                new Object[] { "ID", "Requester", "Destination", "Distance", "Date", "Status" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setShowVerticalLines(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.putClientProperty(FlatClientProperties.STYLE, "showHorizontalLines: true; gridColor: #444444");

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(30, 30, 30));
        add(scrollPane, BorderLayout.CENTER);

        // Input Section
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(25, 25, 25));
        inputPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1.0;

        // Row 1: Labels
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(createLabel("Requester Name"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        inputPanel.add(createLabel("Destination"), gbc);

        // Row 2: Fields
        gbc.gridx = 0;
        gbc.gridy = 1;
        txtRequester = createTextField();
        if ("STAFF".equals(currentUser.getRole())) {
            txtRequester.setText(currentUser.getUsername());
            txtRequester.setEditable(false);
        }
        inputPanel.add(txtRequester, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        txtDestination = createTextField();
        inputPanel.add(txtDestination, gbc);

        // Row 3: Labels
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(createLabel("Distance (km)"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        inputPanel.add(createLabel("Date (yyyy-MM-dd HH:mm:ss)"), gbc);

        // Row 4: Fields
        gbc.gridx = 0;
        gbc.gridy = 3;
        txtDistance = createTextField();
        inputPanel.add(txtDistance, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        txtDate = createTextField();
        txtDate.setText(dateFormat.format(new Date()));
        inputPanel.add(txtDate, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setBackground(new Color(25, 25, 25));
        buttonPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        btnRequest = createButton("Submit Request", "#0078d7", Color.WHITE);
        btnApprove = createButton("Approve", "#009933", Color.WHITE);
        btnReject = createButton("Reject", "#d11a2a", Color.WHITE);

        if ("STAFF".equals(currentUser.getRole())) {
            buttonPanel.add(btnRequest);
        } else {
            buttonPanel.add(btnApprove);
            buttonPanel.add(btnReject);
        }

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBackground(new Color(25, 25, 25));
        southPanel.add(inputPanel, BorderLayout.CENTER);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);

        // Listeners
        btnRequest.addActionListener(e -> addRequest());
        btnApprove.addActionListener(e -> approveRequest());
        btnReject.addActionListener(e -> rejectRequest());
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.GRAY);
        label.setFont(new Font("Inter", Font.PLAIN, 12));
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.putClientProperty(FlatClientProperties.STYLE,
                "arc: 10; background: #333333; foreground: #ffffff; borderWidth: 0; margin: 5,10,5,10");
        field.setPreferredSize(new Dimension(100, 35));
        return field;
    }

    private JButton createButton(String text, String bgColor, Color fgColor) {
        JButton btn = new JButton(text);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("Inter", Font.BOLD, 13));
        btn.setForeground(fgColor);
        btn.putClientProperty(FlatClientProperties.STYLE,
                "background: " + bgColor + "; margin: 8,20,8,20; arc: 10; borderWidth: 0; focusWidth: 0");
        return btn;
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<VehicleRequest> requests = requestDAO.getAllRequests();
        for (VehicleRequest r : requests) {
            if ("ADMIN".equals(currentUser.getRole()) || r.getStaffName().equals(currentUser.getUsername())) {
                tableModel.addRow(new Object[] {
                        r.getRequestId(), r.getStaffName(), r.getDestination(), r.getDistance(),
                        dateFormat.format(r.getDate()), r.getStatus()
                });
            }
        }
    }

    private void addRequest() {
        try {
            VehicleRequest r = new VehicleRequest(0, txtRequester.getText(), txtDestination.getText(),
                    dateFormat.parse(txtDate.getText()), Double.parseDouble(txtDistance.getText()), "PENDING");
            if (requestDAO.addRequest(r)) {
                loadData();
                clearInputs();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void approveRequest() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;
        int id = (int) table.getValueAt(row, 0);

        // Assign Vehicle and Driver
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        JComboBox<Vehicle> vehicleCombo = new JComboBox<>();
        JComboBox<Driver> driverCombo = new JComboBox<>();

        for (Vehicle v : vehicleDAO.getAllVehicles())
            vehicleCombo.addItem(v);
        for (Driver d : driverDAO.getAllDrivers())
            driverCombo.addItem(d);

        panel.add(new JLabel("Select Vehicle:"));
        panel.add(vehicleCombo);
        panel.add(new JLabel("Select Driver:"));
        panel.add(driverCombo);

        int result = JOptionPane.showConfirmDialog(this, panel, "Assign Resources", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Vehicle selectedVehicle = (Vehicle) vehicleCombo.getSelectedItem();
            Driver selectedDriver = (Driver) driverCombo.getSelectedItem();

            if (requestDAO.updateRequestAssignment(id, selectedVehicle.getVehicleId(), selectedDriver.getDriverId(),
                    "APPROVED")) {
                // Automatically create a Trip record
                VehicleRequest r = null;
                for (VehicleRequest req : requestDAO.getAllRequests()) {
                    if (req.getRequestId() == id) {
                        r = req;
                        break;
                    }
                }

                if (r != null) {
                    Trip trip = new Trip(0, selectedVehicle, selectedDriver, r.getDestination(), r.getDate(),
                            r.getDistance(), r.getStaffName());
                    tripDAO.addTrip(trip);
                }

                loadData();
            }
        }
    }

    private void rejectRequest() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;
        int id = (int) table.getValueAt(row, 0);
        if (requestDAO.updateRequestStatus(id, "REJECTED")) {
            loadData();
        }
    }

    private void clearInputs() {
        txtDestination.setText("");
        txtDistance.setText("");
        table.clearSelection();
    }
}
