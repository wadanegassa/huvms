package com.vms.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import com.vms.dao.VehicleDAO;
import com.vms.models.Vehicle;
import com.formdev.flatlaf.FlatClientProperties;

/**
 * Modern VehicleManagementForm for CRUD operations on vehicles.
 */
public class VehicleManagementForm extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtPlate, txtType, txtStatus;
    private JButton btnAdd, btnUpdate, btnDelete;
    private VehicleDAO vehicleDAO;
    private Timer refreshTimer;

    public VehicleManagementForm() {
        vehicleDAO = new VehicleDAO();
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
        JLabel title = new JLabel("Vehicle Fleet");
        title.setFont(new Font("Inter", Font.BOLD, 24));
        title.setForeground(new Color(220, 220, 220));
        add(title, BorderLayout.NORTH);

        // Table Section
        tableModel = new DefaultTableModel(new Object[] { "ID", "Plate Number", "Type", "Status" }, 0) {
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
        inputPanel.add(createLabel("Plate Number"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        inputPanel.add(createLabel("Vehicle Type"), gbc);
        gbc.gridx = 2;
        gbc.gridy = 0;
        inputPanel.add(createLabel("Status"), gbc);

        // Row 2: Fields
        gbc.gridx = 0;
        gbc.gridy = 1;
        txtPlate = createTextField();
        inputPanel.add(txtPlate, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        txtType = createTextField();
        inputPanel.add(txtType, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        txtStatus = createTextField();
        inputPanel.add(txtStatus, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setBackground(new Color(25, 25, 25));
        buttonPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        btnAdd = createButton("Add Vehicle", "#0078d7", Color.WHITE);
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
        btnAdd.addActionListener(e -> addVehicle());
        btnUpdate.addActionListener(e -> updateVehicle());
        btnDelete.addActionListener(e -> deleteVehicle());
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
        List<Vehicle> vehicles = vehicleDAO.getAllVehicles();
        for (Vehicle v : vehicles) {
            tableModel.addRow(new Object[] { v.getVehicleId(), v.getPlateNumber(), v.getVehicleType(), v.getStatus() });
        }
    }

    private void addVehicle() {
        try {
            Vehicle v = new Vehicle(0, txtPlate.getText(), txtType.getText(), txtStatus.getText());
            if (vehicleDAO.addVehicle(v)) {
                loadData();
                clearInputs();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void updateVehicle() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;
        int id = (int) table.getValueAt(row, 0);
        try {
            Vehicle v = new Vehicle(id, txtPlate.getText(), txtType.getText(), txtStatus.getText());
            if (vehicleDAO.updateVehicle(v)) {
                loadData();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void deleteVehicle() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;
        int id = (int) table.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (vehicleDAO.deleteVehicle(id)) {
                loadData();
                clearInputs();
            }
        }
    }

    private void clearInputs() {
        txtPlate.setText("");
        txtType.setText("");
        txtStatus.setText("");
        table.clearSelection();
    }
}
