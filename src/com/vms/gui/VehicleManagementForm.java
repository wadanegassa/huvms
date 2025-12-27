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
    private JTextField txtPlate, txtStatus;
    private JComboBox<String> comboType;
    private JButton btnAdd, btnUpdate, btnDelete;
    private VehicleDAO vehicleDAO;

    public VehicleManagementForm() {
        vehicleDAO = new VehicleDAO();
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Table Section
        tableModel = new DefaultTableModel(new Object[] { "ID", "Plate Number", "Vehicle Type", "Current Status" }, 0) {
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

        // Plate Number
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Plate Number"), gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        txtPlate = new JTextField();
        txtPlate.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        inputPanel.add(txtPlate, gbc);

        // Vehicle Type
        gbc.gridx = 1;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Vehicle Type"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        comboType = new JComboBox<>(new String[] { "Car", "Bus", "Truck" });
        comboType.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        inputPanel.add(comboType, gbc);

        // Status
        gbc.gridx = 2;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Status"), gbc);
        gbc.gridx = 2;
        gbc.gridy = 1;
        txtStatus = new JTextField();
        txtStatus.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        inputPanel.add(txtStatus, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        btnAdd = new JButton("Add Vehicle");
        styleButton(btnAdd, "#0078d7", Color.WHITE);

        btnUpdate = new JButton("Update Selected");
        styleButton(btnUpdate, "#2d2d2d", Color.LIGHT_GRAY);

        btnDelete = new JButton("Remove");
        styleButton(btnDelete, "#d11a2a", Color.WHITE);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(inputPanel, BorderLayout.CENTER);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);

        // Listeners
        btnAdd.addActionListener(e -> addVehicle());
        btnUpdate.addActionListener(e -> updateVehicle());
        btnDelete.addActionListener(e -> deleteVehicle());

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                txtPlate.setText(table.getValueAt(row, 1).toString());
                comboType.setSelectedItem(table.getValueAt(row, 2).toString());
                txtStatus.setText(table.getValueAt(row, 3).toString());
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
        List<Vehicle> vehicles = vehicleDAO.getAllVehicles();
        for (Vehicle v : vehicles) {
            tableModel.addRow(new Object[] { v.getVehicleId(), v.getPlateNumber(), v.getVehicleType(), v.getStatus() });
        }
    }

    private void addVehicle() {
        if (txtPlate.getText().isEmpty())
            return;
        Vehicle v = new Vehicle(0, txtPlate.getText(), (String) comboType.getSelectedItem(), txtStatus.getText());
        if (vehicleDAO.addVehicle(v)) {
            loadData();
            clearInputs();
        }
    }

    private void updateVehicle() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;
        int id = (int) table.getValueAt(row, 0);
        Vehicle v = new Vehicle(id, txtPlate.getText(), (String) comboType.getSelectedItem(), txtStatus.getText());
        if (vehicleDAO.updateVehicle(v)) {
            loadData();
        }
    }

    private void deleteVehicle() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;
        int id = (int) table.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove this vehicle?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (vehicleDAO.deleteVehicle(id)) {
                loadData();
                clearInputs();
            }
        }
    }

    private void clearInputs() {
        txtPlate.setText("");
        txtStatus.setText("");
        table.clearSelection();
    }
}
