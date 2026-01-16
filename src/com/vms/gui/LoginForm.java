package com.vms.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import com.vms.dao.UserDAO;
import com.vms.models.User;
import com.formdev.flatlaf.FlatClientProperties;

/**
 * Ultra-Modern Glassmorphism LoginForm (Restored).
 */
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
        setTitle("HUVMS - Secure Access");
        setSize(480, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Vibrant Gradient Background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Multi-stop gradient for a premium look
                GradientPaint gp = new GradientPaint(0, 0, new Color(10, 20, 50), getWidth(), getHeight(),
                        new Color(40, 10, 60));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Add some subtle "blobs" or glows
                g2d.setColor(new Color(0, 150, 255, 30));
                g2d.fillOval(-100, -100, 300, 300);
                g2d.setColor(new Color(200, 0, 255, 20));
                g2d.fillOval(getWidth() - 200, getHeight() - 200, 400, 400);
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        add(mainPanel);

        // Glassmorphism Card
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 255, 255, 15)); // Very subtle white
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);

                // Subtle border
                g2d.setColor(new Color(255, 255, 255, 30));
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 40, 40);
                g2d.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(380, 520));
        card.setBorder(new EmptyBorder(50, 45, 50, 45));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        // Header Section
        JLabel lblLogo = new JLabel("HUVMS");
        lblLogo.setFont(new Font("Inter", Font.BOLD, 42));
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setHorizontalAlignment(SwingConstants.LEFT);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 5, 0);
        card.add(lblLogo, gbc);

        JLabel lblTag = new JLabel("VEHICLE MANAGEMENT SYSTEM");
        lblTag.setFont(new Font("Inter", Font.BOLD, 10));
        lblTag.setForeground(new Color(0, 150, 255));
        lblTag.setHorizontalAlignment(SwingConstants.LEFT);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 45, 0);
        card.add(lblTag, gbc);

        // Username Section
        JLabel lblUser = new JLabel("USERNAME");
        lblUser.setFont(new Font("Inter", Font.BOLD, 11));
        lblUser.setForeground(new Color(200, 200, 200, 180));
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 10, 0);
        card.add(lblUser, gbc);

        txtUsername = new JTextField();
        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter username");
        txtUsername.putClientProperty(FlatClientProperties.STYLE,
                "arc: 15; background: #ffffff15; foreground: #ffffff; borderWidth: 0; margin: 8,15,8,15");
        txtUsername.setPreferredSize(new Dimension(0, 45));
        txtUsername.setCaretColor(Color.WHITE);
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 25, 0);
        card.add(txtUsername, gbc);

        // Password Section
        JLabel lblPass = new JLabel("PASSWORD");
        lblPass.setFont(new Font("Inter", Font.BOLD, 11));
        lblPass.setForeground(new Color(200, 200, 200, 180));
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 10, 0);
        card.add(lblPass, gbc);

        txtPassword = new JPasswordField();
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter password");
        txtPassword.putClientProperty(FlatClientProperties.STYLE,
                "arc: 15; background: #ffffff15; foreground: #ffffff; borderWidth: 0; margin: 8,15,8,15; showRevealButton: true");
        txtPassword.setPreferredSize(new Dimension(0, 45));
        txtPassword.setCaretColor(Color.WHITE);
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 45, 0);
        card.add(txtPassword, gbc);

        // Login Button
        btnLogin = new JButton("SIGN IN");
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setFont(new Font("Inter", Font.BOLD, 14));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBackground(new Color(0, 120, 215));
        btnLogin.setPreferredSize(new Dimension(0, 50));
        btnLogin.putClientProperty(FlatClientProperties.STYLE,
                "arc: 20; borderWidth: 0; focusWidth: 0; background: #0078d7");
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 25, 0);
        card.add(btnLogin, gbc);

        // Footer
        JLabel lblFooter = new JLabel("© 2026 HARAMAYA UNIVERSITY");
        lblFooter.setFont(new Font("Inter", Font.BOLD, 9));
        lblFooter.setForeground(new Color(150, 150, 150, 100));
        lblFooter.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 7;
        gbc.insets = new Insets(15, 0, 0, 0);
        card.add(lblFooter, gbc);

        mainPanel.add(card);

        // Action Listeners
        btnLogin.addActionListener(e -> performLogin());
        txtPassword.addActionListener(e -> performLogin());
        txtUsername.addActionListener(e -> txtPassword.requestFocus());
    }

    private void performLogin() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = userDAO.login(username, password);
        if (user != null) {
            new MainDashboard(user).setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
