// file: com/petcarex/dao/LichHenDAO.java
package com.petcarex.dao;

import com.petcarex.model.LichHen;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LichHenDAO extends BaseDAO<LichHen> {
    
    @Override
    public void create(LichHen lh) throws SQLException {
        String sql = "INSERT INTO LICH_HEN (MaThuCung, LoaiLichHen, ThoiGian, MaChiNhanh, " +
                    "MaBacSi, MaDichVu, TrangThai, GhiChu, NgayTao) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        executeUpdate(sql,
            lh.getMaThuCung(),
            lh.getLoaiLichHen(),
            Timestamp.valueOf(lh.getThoiGian()),
            lh.getMaChiNhanh(),
            lh.getMaBacSi(),
            lh.getMaDichVu(),
            lh.getTrangThai(),
            lh.getGhiChu(),
            Timestamp.valueOf(lh.getNgayTao())
        );
    }
    
    @Override
    public LichHen read(int maLichHen) throws SQLException {
        String sql = getQueryWithJoins() + " WHERE lh.MaLichHen = ?";
        return executeQuerySingle(sql, this::mapResultSetWithJoins, maLichHen);
    }
    
    @Override
    public void update(LichHen lh) throws SQLException {
        String sql = "UPDATE LICH_HEN SET MaThuCung = ?, LoaiLichHen = ?, ThoiGian = ?, " +
                    "MaChiNhanh = ?, MaBacSi = ?, MaDichVu = ?, TrangThai = ?, " +
                    "GhiChu = ? WHERE MaLichHen = ?";
        
        executeUpdate(sql,
            lh.getMaThuCung(),
            lh.getLoaiLichHen(),
            Timestamp.valueOf(lh.getThoiGian()),
            lh.getMaChiNhanh(),
            lh.getMaBacSi(),
            lh.getMaDichVu(),
            lh.getTrangThai(),
            lh.getGhiChu(),
            lh.getMaLichHen()
        );
    }
    
    @Override
    public void delete(int maLichHen) throws SQLException {
        String sql = "DELETE FROM LICH_HEN WHERE MaLichHen = ?";
        executeUpdate(sql, maLichHen);
    }
    
    @Override
    public List<LichHen> findAll() throws SQLException {
        String sql = getQueryWithJoins() + " ORDER BY lh.ThoiGian DESC";
        return executeQuery(sql, this::mapResultSetWithJoins);
    }
    
    // Additional methods
    public List<LichHen> findByKhachHang(int maKH) throws SQLException {
        String sql = getQueryWithJoins() + 
                    " WHERE tc.MaKH = ? " +
                    "ORDER BY lh.ThoiGian DESC";
        return executeQuery(sql, this::mapResultSetWithJoins, maKH);
    }
    
    public List<LichHen> findByThuCung(int maThuCung) throws SQLException {
        String sql = getQueryWithJoins() + 
                    " WHERE lh.MaThuCung = ? " +
                    "ORDER BY lh.ThoiGian DESC";
        return executeQuery(sql, this::mapResultSetWithJoins, maThuCung);
    }
    
    public List<LichHen> findByBacSi(int maBacSi) throws SQLException {
        String sql = getQueryWithJoins() + 
                    " WHERE lh.MaBacSi = ? " +
                    "ORDER BY lh.ThoiGian DESC";
        return executeQuery(sql, this::mapResultSetWithJoins, maBacSi);
    }
    
    public List<LichHen> findByChiNhanh(int maChiNhanh) throws SQLException {
        String sql = getQueryWithJoins() + 
                    " WHERE lh.MaChiNhanh = ? " +
                    "ORDER BY lh.ThoiGian DESC";
        return executeQuery(sql, this::mapResultSetWithJoins, maChiNhanh);
    }
    
    public List<LichHen> findByTrangThai(String trangThai) throws SQLException {
        String sql = getQueryWithJoins() + 
                    " WHERE lh.TrangThai = ? " +
                    "ORDER BY lh.ThoiGian DESC";
        return executeQuery(sql, this::mapResultSetWithJoins, trangThai);
    }
    
    public List<LichHen> findByDateRange(LocalDateTime from, LocalDateTime to) throws SQLException {
        String sql = getQueryWithJoins() + 
                    " WHERE lh.ThoiGian BETWEEN ? AND ? " +
                    "ORDER BY lh.ThoiGian DESC";
        return executeQuery(sql, this::mapResultSetWithJoins, 
            Timestamp.valueOf(from), Timestamp.valueOf(to));
    }
    
    public void updateTrangThai(int maLichHen, String trangThai) throws SQLException {
        String sql = "UPDATE LICH_HEN SET TrangThai = ? WHERE MaLichHen = ?";
        executeUpdate(sql, trangThai, maLichHen);
    }
    
    public void cancelAppointment(int maLichHen) throws SQLException {
        updateTrangThai(maLichHen, "Đã hủy");
    }
    
    public void confirmAppointment(int maLichHen) throws SQLException {
        updateTrangThai(maLichHen, "Đã xác nhận");
    }
    
    public void completeAppointment(int maLichHen) throws SQLException {
        updateTrangThai(maLichHen, "Đã hoàn thành");
    }
    
    // Helper method for joins query
    private String getQueryWithJoins() {
    	 return "SELECT lh.*, " +
    	           "tc.TenThuCung, " +
    	           "kh.HoTen AS TenChu, " + // THÊM DÒNG NÀY
    	           "cn.TenChiNhanh, " +
    	           "nv.HoTen AS TenBacSi, " +
    	           "dv.TenDichVu " +
    	           "FROM LICH_HEN lh " +
    	           "LEFT JOIN THU_CUNG tc ON lh.MaThuCung = tc.MaThuCung " +
    	           "LEFT JOIN KHACH_HANG kh ON tc.MaKH = kh.MaKH " + // THÊM JOIN NÀY
    	           "LEFT JOIN CHI_NHANH cn ON lh.MaChiNhanh = cn.MaChiNhanh " +
    	           "LEFT JOIN NHAN_VIEN nv ON lh.MaBacSi = nv.MaNV " +
    	           "LEFT JOIN DICH_VU dv ON lh.MaDichVu = dv.MaDichVu";
    }
    
    // Map ResultSet (with joins)
    private LichHen mapResultSetWithJoins(ResultSet rs) throws SQLException {
        LichHen lh = new LichHen();
        
        lh.setMaLichHen(rs.getInt("MaLichHen"));
        lh.setMaThuCung(rs.getInt("MaThuCung"));
        lh.setLoaiLichHen(rs.getString("LoaiLichHen"));
        
        Timestamp thoiGian = rs.getTimestamp("ThoiGian");
        if (thoiGian != null) {
            lh.setThoiGian(thoiGian.toLocalDateTime());
        }
        
        lh.setMaChiNhanh(rs.getInt("MaChiNhanh"));
        
        Integer maBacSi = rs.getInt("MaBacSi");
        if (!rs.wasNull()) {
            lh.setMaBacSi(maBacSi);
        }
        
        Integer maDichVu = rs.getInt("MaDichVu");
        if (!rs.wasNull()) {
            lh.setMaDichVu(maDichVu);
        }
        
        lh.setTrangThai(rs.getString("TrangThai"));
        lh.setGhiChu(rs.getString("GhiChu"));
        
        Timestamp ngayTao = rs.getTimestamp("NgayTao");
        if (ngayTao != null) {
            lh.setNgayTao(ngayTao.toLocalDateTime());
        }
        
        // Thông tin từ joins
        lh.setTenThuCung(rs.getString("TenThuCung"));
        lh.setTenChiNhanh(rs.getString("TenChiNhanh"));
        lh.setTenBacSi(rs.getString("TenBacSi"));
        lh.setTenDichVu(rs.getString("TenDichVu"));
        lh.setTenChu(rs.getString("TenChu"));
        
        return lh;
    }
}