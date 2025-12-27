package com.vms;

import com.formdev.flatlaf.FlatDarkLaf;
import com.vms.db.DBConnection;
import com.vms.gui.LoginForm;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class App {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }

        
        DBConnection.getConnection();

        
        SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}
