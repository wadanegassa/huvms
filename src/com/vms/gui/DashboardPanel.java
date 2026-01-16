package com.vms.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import com.vms.dao.VehicleDAO;
import com.vms.dao.TripDAO;
import com.vms.dao.DriverDAO;
import com.vms.dao.VehicleRequestDAO;
import com.vms.models.User;

/**
 * Ultra-Modern DashboardPanel with premium widget-style cards (Restored).
 */
public class DashboardPanel extends JPanel {
        private User currentUser;
        private JPanel cardsContainer;
        private Timer refreshTimer;

        private VehicleDAO vehicleDAO = new VehicleDAO();
        private TripDAO tripDAO = new TripDAO();
        private DriverDAO driverDAO = new DriverDAO();
        private VehicleRequestDAO requestDAO = new VehicleRequestDAO();

        public DashboardPanel(User user) {
                this.currentUser = user;
                initComponents();
                startAutoRefresh();
        }

        private void startAutoRefresh() {
                refreshTimer = new Timer(5000, e -> loadStats());
                refreshTimer.start();

                this.addComponentListener(new java.awt.event.ComponentAdapter() {
                        @Override
                        public void componentShown(java.awt.event.ComponentEvent e) {
                                loadStats();
                                refreshTimer.start();
                        }

                        @Override
                        public void componentHidden(java.awt.event.ComponentEvent e) {
                                refreshTimer.stop();
                        }
                });
        }

        private void initComponents() {
                setLayout(new BorderLayout(30, 30));
                setBorder(new EmptyBorder(50, 50, 50, 50));
                setBackground(new Color(10, 10, 12));

                // Header Section
                JPanel header = new JPanel(new BorderLayout());
                header.setOpaque(false);

                JLabel lblWelcome = new JLabel("Dashboard Overview");
                lblWelcome.setFont(new Font("Inter", Font.BOLD, 36));
                lblWelcome.setForeground(Color.WHITE);
                header.add(lblWelcome, BorderLayout.NORTH);

                JLabel lblSub = new JLabel(
                                "Welcome back, " + currentUser.getUsername() + ". Here is your system summary.");
                lblSub.setFont(new Font("Inter", Font.PLAIN, 16));
                lblSub.setForeground(new Color(120, 120, 130));
                header.add(lblSub, BorderLayout.SOUTH);

                add(header, BorderLayout.NORTH);

                // Cards Section
                JPanel centerWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
                centerWrapper.setOpaque(false);

                cardsContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 30));
                cardsContainer.setOpaque(false);
                centerWrapper.add(cardsContainer);

                add(centerWrapper, BorderLayout.CENTER);
                loadStats();
        }

        private void loadStats() {
                cardsContainer.removeAll();

                if ("ADMIN".equals(currentUser.getRole())) {
                        int totalVehicles = vehicleDAO.getAllVehicles().size();
                        int activeTrips = tripDAO.getAllTrips().size();
                        int pendingRequests = (int) requestDAO.getAllRequests().stream()
                                        .filter(r -> "PENDING".equals(r.getStatus())).count();
                        int totalDrivers = driverDAO.getAllDrivers().size();

                        cardsContainer.add(createStatCard("Total Vehicles", String.valueOf(totalVehicles),
                                        "Active Fleet", new Color(0, 120, 215), "🚚"));
                        cardsContainer.add(createStatCard("Active Trips", String.valueOf(activeTrips),
                                        "Currently Moving", new Color(0, 180, 80), "📍"));
                        cardsContainer.add(createStatCard("Pending Requests", String.valueOf(pendingRequests),
                                        "Needs Approval", new Color(220, 150, 0), "📝"));
                        cardsContainer.add(createStatCard("Total Drivers", String.valueOf(totalDrivers),
                                        "Staff Directory", new Color(150, 0, 220), "👥"));
                } else {
                        String staffName = currentUser.getUsername();
                        int myPending = (int) requestDAO.getAllRequests().stream().filter(
                                        r -> r.getStaffName().equals(staffName) && "PENDING".equals(r.getStatus()))
                                        .count();
                        int myApproved = (int) requestDAO.getAllRequests().stream().filter(
                                        r -> r.getStaffName().equals(staffName) && "APPROVED".equals(r.getStatus()))
                                        .count();
                        int myTrips = (int) tripDAO.getAllTrips().stream()
                                        .filter(t -> t.getRequesterName().equals(staffName)).count();
                        int availableVehicles = (int) vehicleDAO.getAllVehicles().stream()
                                        .filter(v -> "AVAILABLE".equalsIgnoreCase(v.getStatus())).count();

                        cardsContainer.add(createStatCard("My Pending", String.valueOf(myPending), "Awaiting Review",
                                        new Color(220, 150, 0), "⏳"));
                        cardsContainer.add(createStatCard("My Approved", String.valueOf(myApproved), "Ready to Go",
                                        new Color(0, 180, 80), "✅"));
                        cardsContainer.add(createStatCard("My Trips", String.valueOf(myTrips), "History",
                                        new Color(0, 120, 215), "🗺️"));
                        cardsContainer.add(createStatCard("Available", String.valueOf(availableVehicles),
                                        "Fleet Status", new Color(150, 0, 220), "🚗"));
                }

                cardsContainer.revalidate();
                cardsContainer.repaint();
        }

        private JPanel createStatCard(String title, String value, String subtext, Color accentColor, String icon) {
                JPanel card = new JPanel() {
                        @Override
                        protected void paintComponent(Graphics g) {
                                Graphics2D g2d = (Graphics2D) g.create();
                                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                                RenderingHints.VALUE_ANTIALIAS_ON);

                                // Card background with subtle gradient
                                GradientPaint gp = new GradientPaint(0, 0, new Color(30, 30, 35), 0, getHeight(),
                                                new Color(20, 20, 25));
                                g2d.setPaint(gp);
                                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

                                // Subtle accent glow
                                g2d.setColor(new Color(accentColor.getRed(), accentColor.getGreen(),
                                                accentColor.getBlue(), 20));
                                g2d.fillOval(getWidth() - 80, -40, 120, 120);

                                g2d.dispose();
                        }
                };
                card.setOpaque(false);
                card.setLayout(new BorderLayout(15, 15));
                card.setBorder(new EmptyBorder(30, 30, 30, 30));
                card.setPreferredSize(new Dimension(280, 200));

                // Icon Section
                JPanel iconPanel = new JPanel() {
                        @Override
                        protected void paintComponent(Graphics g) {
                                Graphics2D g2d = (Graphics2D) g.create();
                                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                                RenderingHints.VALUE_ANTIALIAS_ON);
                                g2d.setColor(new Color(accentColor.getRed(), accentColor.getGreen(),
                                                accentColor.getBlue(), 40));
                                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                                g2d.dispose();
                        }
                };
                iconPanel.setOpaque(false);
                iconPanel.setPreferredSize(new Dimension(50, 50));
                iconPanel.setLayout(new GridBagLayout());
                JLabel lblIcon = new JLabel(icon);
                lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
                iconPanel.add(lblIcon);

                JPanel topPanel = new JPanel(new BorderLayout(15, 0));
                topPanel.setOpaque(false);
                topPanel.add(iconPanel, BorderLayout.WEST);

                JLabel lblTitle = new JLabel(title.toUpperCase());
                lblTitle.setFont(new Font("Inter", Font.BOLD, 12));
                lblTitle.setForeground(new Color(150, 150, 160));
                topPanel.add(lblTitle, BorderLayout.CENTER);

                card.add(topPanel, BorderLayout.NORTH);

                // Value Section
                JLabel lblValue = new JLabel(value);
                lblValue.setFont(new Font("Inter", Font.BOLD, 54));
                lblValue.setForeground(Color.WHITE);
                card.add(lblValue, BorderLayout.CENTER);

                // Subtext Section
                JLabel lblSub = new JLabel(subtext);
                lblSub.setFont(new Font("Inter", Font.PLAIN, 14));
                lblSub.setForeground(new Color(110, 110, 120));
                card.add(lblSub, BorderLayout.SOUTH);

                return card;
        }
}
