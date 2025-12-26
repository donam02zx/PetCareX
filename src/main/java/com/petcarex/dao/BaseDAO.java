package com.petcarex.dao;

import com.petcarex.config.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDAO<T> {
    // KHÔNG lưu connection ở đây
    // private Connection connection; // XÓA DÒNG NÀY
    
    public BaseDAO() {
        // Constructor không làm gì cả
    }
    
    // Phương thức lấy connection mới mỗi lần cần
    protected Connection getConnection() throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        if (conn == null || conn.isClosed()) {
            throw new SQLException("Không thể thiết lập kết nối database");
        }
        return conn;
    }
    
    // CRUD operations
    public abstract void create(T entity) throws SQLException;
    public abstract T read(int id) throws SQLException;
    public abstract void update(T entity) throws SQLException;
    public abstract void delete(int id) throws SQLException;
    public abstract List<T> findAll() throws SQLException;
    
    // Common methods - SỬA LẠI: luôn lấy connection mới
    protected void executeUpdate(String sql, Object... params) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = getConnection(); // Lấy connection mới
            stmt = conn.prepareStatement(sql);
            setParameters(stmt, params);
            stmt.executeUpdate();
        } finally {
            closeResources(null, stmt, conn);
        }
    }
    
    protected List<T> executeQuery(String sql, ResultSetMapper<T> mapper, Object... params) 
            throws SQLException {
        List<T> resultList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection(); // Lấy connection mới
            stmt = conn.prepareStatement(sql);
            setParameters(stmt, params);
            
            rs = stmt.executeQuery();
            while (rs.next()) {
                resultList.add(mapper.map(rs));
            }
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return resultList;
    }
    
    protected T executeQuerySingle(String sql, ResultSetMapper<T> mapper, Object... params) 
            throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection(); // Lấy connection mới
            stmt = conn.prepareStatement(sql);
            setParameters(stmt, params);
            
            rs = stmt.executeQuery();
            if (rs.next()) {
                return mapper.map(rs);
            }
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return null;
    }
    
    protected void setParameters(PreparedStatement stmt, Object... params) 
            throws SQLException {
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
    }
    
    // Phương thức đóng tài nguyên
    protected void closeResources(ResultSet rs, PreparedStatement stmt, Connection conn) {
        try {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
        } catch (SQLException e) {
            System.err.println("Lỗi đóng ResultSet: " + e.getMessage());
        }
        
        try {
            if (stmt != null && !stmt.isClosed()) {
                stmt.close();
            }
        } catch (SQLException e) {
            System.err.println("Lỗi đóng Statement: " + e.getMessage());
        }
        
        // KHÔNG đóng connection ở đây!
        // Connection được quản lý bởi DatabaseConnection
    }
    
    // Interface for mapping ResultSet to object
    @FunctionalInterface
    public interface ResultSetMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }
}