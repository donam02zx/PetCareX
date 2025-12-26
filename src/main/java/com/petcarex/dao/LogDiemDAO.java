// file: com/petcarex/dao/LogDiemDAO.java
package com.petcarex.dao;

import com.petcarex.config.DatabaseConnection;
import com.petcarex.model.LogDiem;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

public class LogDiemDAO extends BaseDAO<LogDiem> {
    
    @Override
    public void create(LogDiem log) throws SQLException {
        String sql = "INSERT INTO LOG_DIEM (MaKH, SoDiem, LoaiDiem, LyDo, " +
                    "MaHoaDon, MaNhanVien, NgayGhiNhan) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        executeUpdate(sql,
            log.getMaKH(),
            log.getSoDiem(),
            log.getLoaiDiem(),
            log.getLyDo(),
            log.getMaHoaDon(),
            log.getMaNhanVien(),
            Timestamp.valueOf(log.getNgayGhiNhan())
        );
    }
    
    @Override
    public LogDiem read(int maLog) throws SQLException {
        String sql = "SELECT * FROM LOG_DIEM WHERE MaLog = ?";
        return executeQuerySingle(sql, this::mapResultSet, maLog);
    }
    
    @Override
    public void update(LogDiem log) throws SQLException {
        String sql = "UPDATE LOG_DIEM SET SoDiem = ?, LyDo = ?, " +
                    "MaNhanVien = ? WHERE MaLog = ?";
        
        executeUpdate(sql,
            log.getSoDiem(),
            log.getLyDo(),
            log.getMaNhanVien(),
            log.getMaLog()
        );
    }
    
    @Override
    public void delete(int maLog) throws SQLException {
        String sql = "DELETE FROM LOG_DIEM WHERE MaLog = ?";
        executeUpdate(sql, maLog);
    }
    
    @Override
    public List<LogDiem> findAll() throws SQLException {
        String sql = "SELECT * FROM LOG_DIEM ORDER BY NgayGhiNhan DESC";
        return executeQuery(sql, this::mapResultSet);
    }
    
    // Additional methods
    public List<LogDiem> findByKhachHang(int maKH) throws SQLException {
        String sql = "SELECT * FROM LOG_DIEM WHERE MaKH = ? ORDER BY NgayGhiNhan DESC";
        return executeQuery(sql, this::mapResultSet, maKH);
    }
    
    public List<LogDiem> findByHoaDon(int maHoaDon) throws SQLException {
        String sql = "SELECT * FROM LOG_DIEM WHERE MaHoaDon = ? ORDER BY NgayGhiNhan DESC";
        return executeQuery(sql, this::mapResultSet, maHoaDon);
    }
    
    public List<LogDiem> findByDateRange(LocalDateTime from, LocalDateTime to) throws SQLException {
        String sql = "SELECT * FROM LOG_DIEM WHERE NgayGhiNhan BETWEEN ? AND ? " +
                    "ORDER BY NgayGhiNhan DESC";
        return executeQuery(sql, this::mapResultSet, 
            Timestamp.valueOf(from), Timestamp.valueOf(to));
    }
    
    public List<LogDiem> findByLoaiDiem(String loaiDiem) throws SQLException {
        String sql = "SELECT * FROM LOG_DIEM WHERE LoaiDiem = ? ORDER BY NgayGhiNhan DESC";
        return executeQuery(sql, this::mapResultSet, loaiDiem);
    }
    
    public int getTongDiemKhachHang(int maKH) throws SQLException {
        String sql = "SELECT COALESCE(SUM(SoDiem), 0) FROM LOG_DIEM WHERE MaKH = ?";
        
        try (Connection conn = DatabaseConnection.getStaticConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, maKH);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
    
    public int getDiemSuDungTrongThang(int maKH, int month, int year) throws SQLException {
        String sql = "SELECT COALESCE(SUM(ABS(SoDiem)), 0) FROM LOG_DIEM " +
                    "WHERE MaKH = ? AND LoaiDiem = 'Sử dụng điểm' " +
                    "AND MONTH(NgayGhiNhan) = ? AND YEAR(NgayGhiNhan) = ?";
        
        try (Connection conn = DatabaseConnection.getStaticConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, maKH);
            stmt.setInt(2, month);
            stmt.setInt(3, year);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
    
    public int getDiemCongTrongThang(int maKH, int month, int year) throws SQLException {
        String sql = "SELECT COALESCE(SUM(SoDiem), 0) FROM LOG_DIEM " +
                    "WHERE MaKH = ? AND LoaiDiem = 'Cộng điểm' " +
                    "AND MONTH(NgayGhiNhan) = ? AND YEAR(NgayGhiNhan) = ?";
        
        try (Connection conn = DatabaseConnection.getStaticConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, maKH);
            stmt.setInt(2, month);
            stmt.setInt(3, year);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
    
    // Map ResultSet
    private LogDiem mapResultSet(ResultSet rs) throws SQLException {
        LogDiem log = new LogDiem();
        
        log.setMaLog(rs.getInt("MaLog"));
        log.setMaKH(rs.getInt("MaKH"));
        log.setSoDiem(rs.getInt("SoDiem"));
        log.setLoaiDiem(rs.getString("LoaiDiem"));
        log.setLyDo(rs.getString("LyDo"));
        
        Integer maHoaDon = rs.getInt("MaHoaDon");
        if (!rs.wasNull()) {
            log.setMaHoaDon(maHoaDon);
        }
        
        Integer maNhanVien = rs.getInt("MaNhanVien");
        if (!rs.wasNull()) {
            log.setMaNhanVien(maNhanVien);
        }
        
        Timestamp ngayGhiNhan = rs.getTimestamp("NgayGhiNhan");
        if (ngayGhiNhan != null) {
            log.setNgayGhiNhan(ngayGhiNhan.toLocalDateTime());
        }
        
        return log;
    }
}