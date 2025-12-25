package com.petcarex.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    
    // Database configuration
    private final String URL = "jdbc:mysql://localhost:3306/PetCareX_DB";
    private final String USER = "root";
    private final String PASSWORD = "123123"; // Điền password của bạn
    
    private DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Kết nối database thành công!");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Lỗi kết nối database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }
    
    public Connection getConnection() {
        try {
            if (this.connection == null || this.connection.isClosed()) {
                this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi reconnect database: " + e.getMessage());
        }
        return this.connection;
    }
    
    public void closeConnection() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
                System.out.println("Đã đóng kết nối database");
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi đóng kết nối: " + e.getMessage());
        }
    }
}