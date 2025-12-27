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
    private JButton btnAdd, btnUpdate, btnDelete;
    private DriverDAO driverDAO;

    public DriverManagementForm() {
        driverDAO = new DriverDAO();
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Table Section
        tableModel = new DefaultTableModel(new Object[] { "ID", "Full Name", "License Number", "Phone Contact" }, 0) {
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

        // Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Full Name"), gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        txtName = new JTextField();
        txtName.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        inputPanel.add(txtName, gbc);

        // License
        gbc.gridx = 1;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("License Number"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        txtLicense = new JTextField();
        txtLicense.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        inputPanel.add(txtLicense, gbc);

        // Phone
        gbc.gridx = 2;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Phone Contact"), gbc);
        gbc.gridx = 2;
        gbc.gridy = 1;
        txtPhone = new JTextField();
        txtPhone.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        inputPanel.add(txtPhone, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        btnAdd = new JButton("Register Driver");
        styleButton(btnAdd, "#0078d7", Color.WHITE);

        btnUpdate = new JButton("Update Info");
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
        btnAdd.addActionListener(e -> addDriver());
        btnUpdate.addActionListener(e -> updateDriver());
        btnDelete.addActionListener(e -> deleteDriver());

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                txtName.setText(table.getValueAt(row, 1).toString());
                txtLicense.setText(table.getValueAt(row, 2).toString());
                txtPhone.setText(table.getValueAt(row, 3).toString());
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
        List<Driver> drivers = driverDAO.getAllDrivers();
        for (Driver d : drivers) {
            tableModel.addRow(new Object[] { d.getDriverId(), d.getName(), d.getLicenseNumber(), d.getPhone() });
        }
    }

    private void addDriver() {
        if (txtName.getText().isEmpty())
            return;
        Driver d = new Driver(0, txtName.getText(), txtLicense.getText(), txtPhone.getText());
        if (driverDAO.addDriver(d)) {
            loadData();
            clearInputs();
        }
    }

    private void updateDriver() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;
        int id = (int) table.getValueAt(row, 0);
        Driver d = new Driver(id, txtName.getText(), txtLicense.getText(), txtPhone.getText());
        if (driverDAO.updateDriver(d)) {
            loadData();
        }
    }

    private void deleteDriver() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;
        int id = (int) table.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove this driver?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
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
        table.clearSelection();
    }
}
