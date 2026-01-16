package com.vms.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import com.vms.models.User;
import com.formdev.flatlaf.FlatClientProperties;

public class MainDashboard extends JFrame {
    private User currentUser;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JLabel lblModuleTitle;

    public MainDashboard(User user) {
        this.currentUser = user;
        initComponents();
    }

    private void initComponents() {
        setTitle("HUVMS Dashboard (v1.0.1) - " + currentUser.getUsername());
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(240, 700));
        sidebar.setBackground(new Color(35, 35, 35));
        sidebar.setBorder(new EmptyBorder(20, 15, 20, 15));

        // Sidebar Header
        JLabel lblBrand = new JLabel("HUVMS");
        lblBrand.setFont(new Font("Inter", Font.BOLD, 24));
        lblBrand.setForeground(new Color(0, 150, 255));
        lblBrand.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(lblBrand);
        sidebar.add(Box.createRigidArea(new Dimension(0, 30)));

        // Sidebar Buttons
        String[] modules;
        if ("ADMIN".equals(currentUser.getRole())) {
            modules = new String[] { "Dashboard", "Vehicles", "Drivers", "Trips", "Fuel", "Maintenance", "Requests" };
        } else {
            modules = new String[] { "Dashboard", "Requests", "Trips" };
        }

        for (String module : modules) {
            sidebar.add(createSidebarButton(module));
            sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        if ("ADMIN".equals(currentUser.getRole())) {
            sidebar.add(Box.createRigidArea(new Dimension(0, 20)));
            JLabel lblAdmin = new JLabel("ADMINISTRATION");
            lblAdmin.setFont(new Font("Inter", Font.BOLD, 10));
            lblAdmin.setForeground(Color.DARK_GRAY);
            sidebar.add(lblAdmin);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
            sidebar.add(createSidebarButton("Users"));
        }

        sidebar.add(Box.createVerticalGlue());

        // Logout Button
        JButton btnLogout = createSidebarButton("Logout");
        btnLogout.setForeground(new Color(255, 80, 80));
        btnLogout.addActionListener(e -> {
            new LoginForm().setVisible(true);
            this.dispose();
        });
        sidebar.add(btnLogout);

        add(sidebar, BorderLayout.WEST);

        // Main Area
        JPanel mainArea = new JPanel(new BorderLayout());
        mainArea.setBackground(new Color(25, 25, 25));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(30, 30, 30));
        header.setPreferredSize(new Dimension(0, 60));
        header.setBorder(new EmptyBorder(0, 20, 0, 20));

        lblModuleTitle = new JLabel("Dashboard Overview");
        lblModuleTitle.setFont(new Font("Inter", Font.BOLD, 18));
        header.add(lblModuleTitle, BorderLayout.WEST);

        JLabel lblUser = new JLabel(currentUser.getUsername() + " | " + currentUser.getRole());
        lblUser.setForeground(Color.GRAY);
        header.add(lblUser, BorderLayout.EAST);

        mainArea.add(header, BorderLayout.NORTH);

        // Card Panel for Content
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(new Color(25, 25, 25));

        // Add Dashboard Panel
        cardPanel.add(new DashboardPanel(currentUser), "Dashboard");

        // Add Management Panels
        cardPanel.add(new VehicleManagementForm(), "Vehicles");
        cardPanel.add(new DriverManagementForm(), "Drivers");
        cardPanel.add(new TripManagementForm(currentUser), "Trips");
        cardPanel.add(new FuelManagementForm(), "Fuel");
        cardPanel.add(new MaintenanceManagementForm(), "Maintenance");
        cardPanel.add(new RequestManagementForm(currentUser), "Requests");
        if ("ADMIN".equals(currentUser.getRole())) {
            cardPanel.add(new UserManagementForm(), "Users");
        }

        mainArea.add(cardPanel, BorderLayout.CENTER);
        add(mainArea, BorderLayout.CENTER);
    }

    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(210, 40));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(new Font("Inter", Font.PLAIN, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(0, 15, 0, 0));

        btn.putClientProperty(FlatClientProperties.STYLE,
                "background: #232323; " +
                        "foreground: #bbbbbb");

        if (!text.equals("Logout")) {
            btn.addActionListener(e -> showModule(text));
        }

        return btn;
    }

    private void showModule(String module) {
        lblModuleTitle.setText(module + (module.equals("Dashboard") ? " Overview" : " Management"));
        cardLayout.show(cardPanel, module);
    }
}
