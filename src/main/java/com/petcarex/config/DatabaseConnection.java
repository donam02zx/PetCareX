package com.petcarex.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    
    private final String URL = "jdbc:mysql://localhost:3306/PetCareX_DB";
    private final String USER = "root";
    private final String PASSWORD = "123123";
    
    private DatabaseConnection() {
        // Không tạo kết nối ngay
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
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
                    System.out.println("✅ Kết nối database thành công!");
                } catch (ClassNotFoundException | SQLException e) {
                    System.err.println("❌ Lỗi kết nối database: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi kiểm tra kết nối: " + e.getMessage());
        }
        return this.connection;
    }
    
    public static Connection getStaticConnection() {
        return getInstance().getConnection();
    }
    
    public void closeConnection() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
                System.out.println("Đã đóng kết nối database");
                this.connection = null; // Quan trọng: đặt về null
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi đóng kết nối: " + e.getMessage());
        }
    }
}