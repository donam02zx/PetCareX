package com.petcarex;

import com.formdev.flatlaf.FlatLightLaf;
import com.petcarex.config.DatabaseConnection;
import com.petcarex.view.LoginFrame;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.err.println("Không thể load FlatLaf: " + e.getMessage());
        }
        
        // Connect to database
        DatabaseConnection.getInstance();
        
        // Run on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}