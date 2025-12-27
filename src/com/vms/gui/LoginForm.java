package com.vms.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import com.vms.dao.UserDAO;
import com.vms.models.User;
import com.formdev.flatlaf.FlatClientProperties;


public class LoginForm extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private UserDAO userDAO;

    public LoginForm() {
        userDAO = new UserDAO();
        initComponents();
    }

    private void initComponents() {
        setTitle("HUVMS - Secure Login");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main background panel with a subtle gradient-like color
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(30, 30, 30));
        add(mainPanel);

        // Login Card
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(320, 400));
        card.setBackground(new Color(45, 45, 45));
        card.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Add rounded corners and shadow effect via FlatLaf properties
        card.putClientProperty(FlatClientProperties.STYLE, "arc: 20; background: #2d2d2d");

        // Logo or Title
        JLabel lblTitle = new JLabel("HUVMS LOGIN");
        lblTitle.setFont(new Font("Inter", Font.BOLD, 24));
        lblTitle.setForeground(new Color(0, 150, 255));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblTitle);
        card.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel lblSubTitle = new JLabel("Haramaya University Vehicle Management System");
        lblSubTitle.setFont(new Font("Inter", Font.PLAIN, 12));
        lblSubTitle.setForeground(Color.GRAY);
        lblSubTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblSubTitle);
        card.add(Box.createRigidArea(new Dimension(0, 40)));

        // Username field
        JLabel lblUser = new JLabel("Username");
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblUser);
        card.add(Box.createRigidArea(new Dimension(0, 5)));

        txtUsername = new JTextField();
        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your username");
        txtUsername.putClientProperty(FlatClientProperties.STYLE, "arc: 10");
        txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        card.add(txtUsername);
        card.add(Box.createRigidArea(new Dimension(0, 20)));

        // Password field
        JLabel lblPass = new JLabel("Password");
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblPass);
        card.add(Box.createRigidArea(new Dimension(0, 5)));

        txtPassword = new JPasswordField();
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your password");
        txtPassword.putClientProperty(FlatClientProperties.STYLE, "arc: 10; showRevealButton: true");
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        card.add(txtPassword);
        card.add(Box.createRigidArea(new Dimension(0, 30)));

        // Login Button
        btnLogin = new JButton("Login to Dashboard");
        btnLogin.setBackground(new Color(0, 120, 215));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Inter", Font.BOLD, 14));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnLogin.putClientProperty(FlatClientProperties.STYLE, "arc: 10");
        card.add(btnLogin);

        mainPanel.add(card);

        // Action Listeners
        btnLogin.addActionListener(e -> performLogin());
        txtPassword.addActionListener(e -> performLogin());
    }

    private void performLogin() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Login Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        User user = userDAO.login(username, password);
        if (user != null) {
            new MainDashboard(user).setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
