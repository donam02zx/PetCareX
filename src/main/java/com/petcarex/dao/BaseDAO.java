package com.petcarex.dao;

import com.petcarex.config.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDAO<T> {
    protected Connection connection;
    
    public BaseDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }
    
    // CRUD operations
    public abstract void create(T entity) throws SQLException;
    public abstract T read(int id) throws SQLException;
    public abstract void update(T entity) throws SQLException;
    public abstract void delete(int id) throws SQLException;
    public abstract List<T> findAll() throws SQLException;
    
    // Common methods
    protected void executeUpdate(String sql, Object... params) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            setParameters(stmt, params);
            stmt.executeUpdate();
        }
    }
    
    protected List<T> executeQuery(String sql, ResultSetMapper<T> mapper, Object... params) 
            throws SQLException {
        List<T> resultList = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            setParameters(stmt, params);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    resultList.add(mapper.map(rs));
                }
            }
        }
        
        return resultList;
    }
    
    protected T executeQuerySingle(String sql, ResultSetMapper<T> mapper, Object... params) 
            throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            setParameters(stmt, params);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapper.map(rs);
                }
            }
        }
        
        return null;
    }
    
    protected void setParameters(PreparedStatement stmt, Object... params) 
            throws SQLException {
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
    }
    
    // Interface for mapping ResultSet to object
    @FunctionalInterface
    public interface ResultSetMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }
}