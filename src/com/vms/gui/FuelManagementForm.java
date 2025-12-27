package com.vms.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import com.vms.dao.FuelRecordDAO;
import com.vms.dao.VehicleDAO;
import com.vms.models.FuelRecord;
import com.vms.models.Vehicle;
import com.formdev.flatlaf.FlatClientProperties;

/**
 * Modern FuelManagementForm for CRUD operations on fuel records.
 */
public class FuelManagementForm extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<Vehicle> comboVehicle;
    private JTextField txtAmount, txtCost, txtDate;
    private JButton btnAdd, btnUpdate, btnDelete;
    private FuelRecordDAO fuelDAO;
    private VehicleDAO vehicleDAO;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public FuelManagementForm() {
        fuelDAO = new FuelRecordDAO();
        vehicleDAO = new VehicleDAO();
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Table Section
        tableModel = new DefaultTableModel(
                new Object[] { "ID", "Vehicle", "Amount (L)", "Total Cost", "Date Recorded" }, 0) {
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

        // Row 1: Vehicle & Amount
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Vehicle"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Fuel Amount (Liters)"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        comboVehicle = new JComboBox<>();
        comboVehicle.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        loadVehicles();
        inputPanel.add(comboVehicle, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        txtAmount = new JTextField();
        txtAmount.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        inputPanel.add(txtAmount, gbc);

        // Row 2: Cost & Date
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Total Cost"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Date (yyyy-MM-dd HH:mm:ss)"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        txtCost = new JTextField();
        txtCost.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        inputPanel.add(txtCost, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        txtDate = new JTextField(dateFormat.format(new Date()));
        txtDate.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        inputPanel.add(txtDate, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        btnAdd = new JButton("Add Record");
        styleButton(btnAdd, "#0078d7", Color.WHITE);

        btnUpdate = new JButton("Update Record");
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
        btnAdd.addActionListener(e -> addRecord());
        btnUpdate.addActionListener(e -> updateRecord());
        btnDelete.addActionListener(e -> deleteRecord());
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

    private void loadData() {
        tableModel.setRowCount(0);
        List<FuelRecord> records = fuelDAO.getAllFuelRecords();
        for (FuelRecord r : records) {
            tableModel.addRow(new Object[] {
                    r.getRecordId(), r.getVehicle().getPlateNumber(), r.getAmount(), r.getCost(),
                    dateFormat.format(r.getDate())
            });
        }
    }

    private void addRecord() {
        try {
            FuelRecord r = new FuelRecord(0, (Vehicle) comboVehicle.getSelectedItem(),
                    Double.parseDouble(txtAmount.getText()), Double.parseDouble(txtCost.getText()),
                    dateFormat.parse(txtDate.getText()));
            if (fuelDAO.addFuelRecord(r)) {
                loadData();
                clearInputs();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void updateRecord() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;
        int id = (int) table.getValueAt(row, 0);
        try {
            FuelRecord r = new FuelRecord(id, (Vehicle) comboVehicle.getSelectedItem(),
                    Double.parseDouble(txtAmount.getText()), Double.parseDouble(txtCost.getText()),
                    dateFormat.parse(txtDate.getText()));
            if (fuelDAO.updateFuelRecord(r)) {
                loadData();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void deleteRecord() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;
        int id = (int) table.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Remove this fuel record?", "Confirm",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (fuelDAO.deleteFuelRecord(id)) {
                loadData();
                clearInputs();
            }
        }
    }

    private void clearInputs() {
        txtAmount.setText("");
        txtCost.setText("");
        table.clearSelection();
    }
}
