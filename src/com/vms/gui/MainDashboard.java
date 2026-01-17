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

    private JPanel sidebar;
    private java.util.Map<String, JButton> sidebarButtons = new java.util.HashMap<>();
    private String activeModule = "Dashboard";

    public MainDashboard(User user) {
        this.currentUser = user;
        initComponents();
    }

    private void initComponents() {
        setTitle("HUVMS Dashboard (v1.0.1) - " + currentUser.getUsername());
        setSize(1280, 850);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Sidebar with Custom Gradient
        sidebar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(25, 25, 30), 0, getHeight(),
                        new Color(15, 15, 20));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        sidebar.setLayout(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setBorder(new EmptyBorder(0, 0, 0, 0));

        // Sidebar Content Wrapper
        JPanel sidebarContent = new JPanel();
        sidebarContent.setOpaque(false);
        sidebarContent.setLayout(new BoxLayout(sidebarContent, BoxLayout.Y_AXIS));
        sidebarContent.setBorder(new EmptyBorder(30, 20, 30, 20));

        // Brand Logo
        JLabel lblBrand = new JLabel("HUVMS");
        lblBrand.setFont(new Font("Inter", Font.BOLD, 28));
        lblBrand.setForeground(new Color(0, 150, 255));
        lblBrand.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebarContent.add(lblBrand);

        JLabel lblSubBrand = new JLabel("FLEET MANAGEMENT");
        lblSubBrand.setFont(new Font("Inter", Font.BOLD, 10));
        lblSubBrand.setForeground(new Color(100, 100, 110));
        lblSubBrand.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebarContent.add(lblSubBrand);

        sidebarContent.add(Box.createRigidArea(new Dimension(0, 40)));

        // Navigation Section
        JLabel lblNav = new JLabel("NAVIGATION");
        lblNav.setFont(new Font("Inter", Font.BOLD, 11));
        lblNav.setForeground(new Color(70, 70, 80));
        lblNav.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebarContent.add(lblNav);
        sidebarContent.add(Box.createRigidArea(new Dimension(0, 15)));

        // Sidebar Buttons
        String[][] modules;
        if ("ADMIN".equals(currentUser.getRole())) {
            modules = new String[][] {
                    { "Dashboard", "📊" }, { "Vehicles", "🚗" }, { "Drivers", "👥" },
                    { "Trips", "📍" }, { "Fuel", "⛽" }, { "Maintenance", "🔧" }, { "Requests", "📝" }
            };
        } else {
            modules = new String[][] {
                    { "Dashboard", "📊" }, { "Requests", "📝" }, { "My Trips", "🗺️" }
            };
        }

        for (String[] module : modules) {
            JButton btn = createSidebarButton(module[0], module[1]);
            sidebarButtons.put(module[0], btn);
            sidebarContent.add(btn);
            sidebarContent.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        if ("ADMIN".equals(currentUser.getRole())) {
            sidebarContent.add(Box.createRigidArea(new Dimension(0, 25)));
            JLabel lblAdmin = new JLabel("ADMINISTRATION");
            lblAdmin.setFont(new Font("Inter", Font.BOLD, 11));
            lblAdmin.setForeground(new Color(70, 70, 80));
            lblAdmin.setAlignmentX(Component.LEFT_ALIGNMENT);
            sidebarContent.add(lblAdmin);
            sidebarContent.add(Box.createRigidArea(new Dimension(0, 15)));

            JButton btnUsers = createSidebarButton("Users", "👤");
            sidebarButtons.put("Users", btnUsers);
            sidebarContent.add(btnUsers);
        }

        sidebar.add(sidebarContent, BorderLayout.CENTER);

        // User Profile Section at Bottom
        JPanel profilePanel = new JPanel(new BorderLayout(15, 0));
        profilePanel.setOpaque(false);
        profilePanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Simple Avatar Circle
        JPanel avatar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0, 150, 255, 40));
                g2d.fillOval(0, 0, getWidth(), getHeight());
                g2d.setColor(new Color(0, 150, 255));
                g2d.setFont(new Font("Inter", Font.BOLD, 14));
                String initial = currentUser.getUsername().substring(0, 1).toUpperCase();
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(initial)) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(initial, x, y);
                g2d.dispose();
            }
        };
        avatar.setPreferredSize(new Dimension(40, 40));
        profilePanel.add(avatar, BorderLayout.WEST);

        JPanel userInfo = new JPanel(new GridLayout(2, 1));
        userInfo.setOpaque(false);
        JLabel lblName = new JLabel(currentUser.getUsername());
        lblName.setFont(new Font("Inter", Font.BOLD, 14));
        lblName.setForeground(Color.WHITE);
        JLabel lblRole = new JLabel(currentUser.getRole());
        lblRole.setFont(new Font("Inter", Font.PLAIN, 11));
        lblRole.setForeground(new Color(120, 120, 130));
        userInfo.add(lblName);
        userInfo.add(lblRole);
        profilePanel.add(userInfo, BorderLayout.CENTER);

        JButton btnLogout = new JButton("🚪");
        btnLogout.setToolTipText("Logout");
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.putClientProperty(FlatClientProperties.STYLE,
                "background: #ff505020; foreground: #ff5050; arc: 10; borderWidth: 0");
        btnLogout.setPreferredSize(new Dimension(35, 35));
        btnLogout.addActionListener(e -> {
            new LoginForm().setVisible(true);
            this.dispose();
        });
        profilePanel.add(btnLogout, BorderLayout.EAST);

        sidebar.add(profilePanel, BorderLayout.SOUTH);
        add(sidebar, BorderLayout.WEST);

        // Main Area
        JPanel mainArea = new JPanel(new BorderLayout());
        mainArea.setBackground(new Color(10, 10, 12));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(15, 15, 18));
        header.setPreferredSize(new Dimension(0, 70));
        header.setBorder(new EmptyBorder(0, 30, 0, 30));

        lblModuleTitle = new JLabel("Dashboard Overview");
        lblModuleTitle.setFont(new Font("Inter", Font.BOLD, 22));
        lblModuleTitle.setForeground(Color.WHITE);
        header.add(lblModuleTitle, BorderLayout.WEST);

        mainArea.add(header, BorderLayout.NORTH);

        // Card Panel for Content
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false);

        // Add Panels
        cardPanel.add(new DashboardPanel(currentUser), "Dashboard");
        cardPanel.add(new VehicleManagementForm(), "Vehicles");
        cardPanel.add(new DriverManagementForm(), "Drivers");
        cardPanel.add(new TripManagementForm(currentUser), "Trips");
        cardPanel.add(new TripManagementForm(currentUser), "My Trips");
        cardPanel.add(new FuelManagementForm(), "Fuel");
        cardPanel.add(new MaintenanceManagementForm(), "Maintenance");
        cardPanel.add(new RequestManagementForm(currentUser), "Requests");
        if ("ADMIN".equals(currentUser.getRole())) {
            cardPanel.add(new UserManagementForm(), "Users");
        }

        mainArea.add(cardPanel, BorderLayout.CENTER);
        add(mainArea, BorderLayout.CENTER);

        // Set initial active button
        updateSidebarSelection("Dashboard");
    }

    private JButton createSidebarButton(String text, String icon) {
        JButton btn = new JButton(icon + "   " + text);
        btn.setMaximumSize(new Dimension(220, 45));
        btn.setPreferredSize(new Dimension(220, 45));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(new Font("Inter", Font.PLAIN, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(0, 15, 0, 0));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (!text.equals(activeModule)) {
                    btn.setForeground(Color.WHITE);
                    btn.setBackground(new Color(255, 255, 255, 10));
                    btn.setOpaque(true);
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (!text.equals(activeModule)) {
                    btn.setForeground(new Color(150, 150, 160));
                    btn.setOpaque(false);
                }
            }
        });

        btn.addActionListener(e -> showModule(text));

        return btn;
    }

    private void showModule(String module) {
        activeModule = module;
        String title = module;
        if (module.equals("My Trips"))
            title = "Trip";
        lblModuleTitle.setText(title + (module.equals("Dashboard") ? " Overview" : " Management"));
        cardLayout.show(cardPanel, module);
        updateSidebarSelection(module);
    }

    private void updateSidebarSelection(String module) {
        for (java.util.Map.Entry<String, JButton> entry : sidebarButtons.entrySet()) {
            JButton btn = entry.getValue();
            if (entry.getKey().equals(module)) {
                btn.setForeground(new Color(0, 150, 255));
                btn.setBackground(new Color(0, 150, 255, 20));
                btn.setOpaque(true);
                btn.putClientProperty(FlatClientProperties.STYLE, "arc: 12; borderWidth: 0");
            } else {
                btn.setForeground(new Color(150, 150, 160));
                btn.setOpaque(false);
                btn.putClientProperty(FlatClientProperties.STYLE, "arc: 12; borderWidth: 0");
            }
        }
    }
}
