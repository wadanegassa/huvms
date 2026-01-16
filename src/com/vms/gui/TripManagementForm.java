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

    private Timer refreshTimer;

    public TripManagementForm(User user) {
        this.currentUser = user;
        this.tripDAO = new TripDAO();
        this.vehicleDAO = new VehicleDAO();
        this.driverDAO = new DriverDAO();
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
        JLabel title = new JLabel("Trip Management");
        title.setFont(new Font("Inter", Font.BOLD, 24));
        title.setForeground(new Color(220, 220, 220));
        add(title, BorderLayout.NORTH);

        // Table Section
        tableModel = new DefaultTableModel(
                new Object[] { "ID", "Vehicle", "Driver", "Destination", "Date", "Distance", "Requester" }, 0) {
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
        inputPanel.add(createLabel("Assigned Vehicle"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        inputPanel.add(createLabel("Assigned Driver"), gbc);

        // Row 2: Combos
        gbc.gridx = 0;
        gbc.gridy = 1;
        comboVehicle = new JComboBox<>();
        styleComboBox(comboVehicle);
        loadVehicles();
        inputPanel.add(comboVehicle, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        comboDriver = new JComboBox<>();
        styleComboBox(comboDriver);
        loadDrivers();
        inputPanel.add(comboDriver, gbc);

        // Row 3: Labels
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(createLabel("Destination"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        inputPanel.add(createLabel("Date (yyyy-MM-dd HH:mm:ss)"), gbc);

        // Row 4: Fields
        gbc.gridx = 0;
        gbc.gridy = 3;
        txtDestination = createTextField();
        inputPanel.add(txtDestination, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        txtDate = createTextField();
        txtDate.setText(dateFormat.format(new Date()));
        inputPanel.add(txtDate, gbc);

        // Row 5: Labels
        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(createLabel("Distance (km)"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 4;
        inputPanel.add(createLabel("Requester Name"), gbc);

        // Row 6: Fields
        gbc.gridx = 0;
        gbc.gridy = 5;
        txtDistance = createTextField();
        inputPanel.add(txtDistance, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        txtRequester = createTextField();
        inputPanel.add(txtRequester, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setBackground(new Color(25, 25, 25));
        buttonPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        btnAdd = createButton("Create Trip", "#0078d7", Color.WHITE);
        btnUpdate = createButton("Update", "#2d2d2d", Color.LIGHT_GRAY);
        btnDelete = createButton("Cancel", "#d11a2a", Color.WHITE);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBackground(new Color(25, 25, 25));
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

    private void styleComboBox(JComboBox<?> combo) {
        combo.putClientProperty(FlatClientProperties.STYLE,
                "arc: 10; background: #333333; foreground: #ffffff; borderWidth: 0");
        combo.setPreferredSize(new Dimension(100, 35));
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
        comboVehicle.removeAllItems();
        loadVehicles();
        comboDriver.removeAllItems();
        loadDrivers();

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
