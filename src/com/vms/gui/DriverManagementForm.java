package com.vms.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import com.vms.dao.DriverDAO;
import com.vms.models.Driver;
import com.formdev.flatlaf.FlatClientProperties;

/**
 * Modern DriverManagementForm for CRUD operations on drivers.
 */
public class DriverManagementForm extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtName, txtLicense, txtPhone;
    private JComboBox<String> comboVehicleType;
    private JButton btnAdd, btnUpdate, btnDelete;
    private DriverDAO driverDAO;
    private Timer refreshTimer;

    public DriverManagementForm() {
        driverDAO = new DriverDAO();
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
        JLabel title = new JLabel("Driver Directory");
        title.setFont(new Font("Inter", Font.BOLD, 24));
        title.setForeground(new Color(220, 220, 220));
        add(title, BorderLayout.NORTH);

        // Table Section
        tableModel = new DefaultTableModel(new Object[] { "ID", "Name", "License Number", "Phone", "Vehicle Type" },
                0) {
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
        inputPanel.add(createLabel("Name"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        inputPanel.add(createLabel("License Number"), gbc);
        gbc.gridx = 2;
        gbc.gridy = 0;
        inputPanel.add(createLabel("Phone"), gbc);
        gbc.gridx = 3;
        gbc.gridy = 0;
        inputPanel.add(createLabel("Vehicle Type"), gbc);

        // Row 2: Fields
        gbc.gridx = 0;
        gbc.gridy = 1;
        txtName = createTextField();
        inputPanel.add(txtName, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        txtLicense = createTextField();
        inputPanel.add(txtLicense, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        txtPhone = createTextField();
        inputPanel.add(txtPhone, gbc);

        gbc.gridx = 3;
        gbc.gridy = 1;
        comboVehicleType = new JComboBox<>(new String[] { "Bus", "SUV", "Truck", "Van", "Sedan" });
        comboVehicleType.putClientProperty(FlatClientProperties.STYLE,
                "arc: 10; background: #333333; foreground: #ffffff; borderWidth: 0; margin: 5,10,5,10");
        comboVehicleType.setPreferredSize(new Dimension(100, 35));
        inputPanel.add(comboVehicleType, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setBackground(new Color(25, 25, 25));
        buttonPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        btnAdd = createButton("Add Driver", "#0078d7", Color.WHITE);
        btnUpdate = createButton("Update", "#2d2d2d", Color.LIGHT_GRAY);
        btnDelete = createButton("Delete", "#d11a2a", Color.WHITE);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBackground(new Color(25, 25, 25));
        southPanel.add(inputPanel, BorderLayout.CENTER);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);

        // Listeners
        btnAdd.addActionListener(e -> addDriver());
        btnUpdate.addActionListener(e -> updateDriver());
        btnDelete.addActionListener(e -> deleteDriver());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    txtName.setText(table.getValueAt(row, 1).toString());
                    txtLicense.setText(table.getValueAt(row, 2).toString());
                    txtPhone.setText(table.getValueAt(row, 3).toString());
                    comboVehicleType.setSelectedItem(table.getValueAt(row, 4).toString());
                }
            }
        });
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
        List<Driver> drivers = driverDAO.getAllDrivers();
        for (Driver d : drivers) {
            tableModel.addRow(new Object[] { d.getDriverId(), d.getName(), d.getLicenseNumber(), d.getPhone(),
                    d.getVehicleType() });
        }
    }

    private void addDriver() {
        try {
            Driver d = new Driver(0, txtName.getText(), txtLicense.getText(), txtPhone.getText(),
                    comboVehicleType.getSelectedItem().toString());
            if (driverDAO.addDriver(d)) {
                loadData();
                clearInputs();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void updateDriver() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;
        int id = (int) table.getValueAt(row, 0);
        try {
            Driver d = new Driver(id, txtName.getText(), txtLicense.getText(), txtPhone.getText(),
                    comboVehicleType.getSelectedItem().toString());
            if (driverDAO.updateDriver(d)) {
                loadData();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void deleteDriver() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;
        int id = (int) table.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (driverDAO.deleteDriver(id)) {
                loadData();
                clearInputs();
            }
        }
    }

    private void clearInputs() {
        txtName.setText("");
        txtLicense.setText("");
        txtPhone.setText("");
        comboVehicleType.setSelectedIndex(0);
        table.clearSelection();
    }
}
