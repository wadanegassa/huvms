package com.vms.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import com.vms.dao.UserDAO;
import com.vms.models.User;
import com.vms.models.Admin;
import com.vms.models.Staff;
import com.formdev.flatlaf.FlatClientProperties;

/**
 * Modern UserManagementForm for CRUD operations on users.
 */
public class UserManagementForm extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtUsername, txtPassword;
    private JComboBox<String> comboRole;
    private JButton btnAdd, btnUpdate, btnDelete;
    private UserDAO userDAO;

    public UserManagementForm() {
        userDAO = new UserDAO();
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Table Section
        tableModel = new DefaultTableModel(new Object[] { "ID", "Username", "Access Role" }, 0) {
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

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Username"), gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        txtUsername = new JTextField();
        txtUsername.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        inputPanel.add(txtUsername, gbc);

        // Password
        gbc.gridx = 1;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Password"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        txtPassword = new JTextField();
        txtPassword.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        inputPanel.add(txtPassword, gbc);

        // Role
        gbc.gridx = 2;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Access Role"), gbc);
        gbc.gridx = 2;
        gbc.gridy = 1;
        comboRole = new JComboBox<>(new String[] { "ADMIN", "STAFF" });
        comboRole.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        inputPanel.add(comboRole, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        btnAdd = new JButton("Create User");
        styleButton(btnAdd, "#0078d7", Color.WHITE);

        btnUpdate = new JButton("Update User");
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
        btnAdd.addActionListener(e -> addUser());
        btnUpdate.addActionListener(e -> updateUser());
        btnDelete.addActionListener(e -> deleteUser());

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                txtUsername.setText(table.getValueAt(row, 1).toString());
                comboRole.setSelectedItem(table.getValueAt(row, 2).toString());
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
        List<User> users = userDAO.getAllUsers();
        for (User u : users) {
            tableModel.addRow(new Object[] { u.getUserId(), u.getUsername(), u.getRole() });
        }
    }

    private void addUser() {
        if (txtUsername.getText().isEmpty())
            return;
        String role = (String) comboRole.getSelectedItem();
        User u = "ADMIN".equals(role) ? new Admin(0, txtUsername.getText(), txtPassword.getText())
                : new Staff(0, txtUsername.getText(), txtPassword.getText());
        if (userDAO.addUser(u)) {
            loadData();
            clearInputs();
        }
    }

    private void updateUser() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;
        int id = (int) table.getValueAt(row, 0);
        String role = (String) comboRole.getSelectedItem();
        User u = "ADMIN".equals(role) ? new Admin(id, txtUsername.getText(), txtPassword.getText())
                : new Staff(id, txtUsername.getText(), txtPassword.getText());
        if (userDAO.updateUser(u)) {
            loadData();
        }
    }

    private void deleteUser() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;
        int id = (int) table.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Remove this user account?", "Confirm",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (userDAO.deleteUser(id)) {
                loadData();
                clearInputs();
            }
        }
    }

    private void clearInputs() {
        txtUsername.setText("");
        txtPassword.setText("");
        table.clearSelection();
    }
}
