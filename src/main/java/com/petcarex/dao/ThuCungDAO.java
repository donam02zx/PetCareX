package com.petcarex.dao;

import com.petcarex.model.ThuCung;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ThuCungDAO extends BaseDAO<ThuCung> {
    
    @Override
    public void create(ThuCung tc) throws SQLException {
        String sql = "INSERT INTO THU_CUNG (TenThuCung, Loai, Giong, NgaySinh, GioiTinh, TinhTrangSucKhoe, MaKH) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        executeUpdate(sql,
            tc.getTenThuCung(),
            tc.getLoai(),
            tc.getGiong(),
            Date.valueOf(tc.getNgaySinh()),
            tc.getGioiTinh(),
            tc.getTinhTrangSucKhoe(),
            tc.getMaKH()
        );
    }
    
    @Override
    public ThuCung read(int maThuCung) throws SQLException {
        String sql = "SELECT tc.*, kh.HoTen AS TenChu " +
                    "FROM THU_CUNG tc " +
                    "LEFT JOIN KHACH_HANG kh ON tc.MaKH = kh.MaKH " +
                    "WHERE tc.MaThuCung = ?";
        
        return executeQuerySingle(sql, this::mapResultSet, maThuCung);
    }
    
    @Override
    public void update(ThuCung tc) throws SQLException {
        String sql = "UPDATE THU_CUNG SET " +
                    "TenThuCung = ?, Loai = ?, Giong = ?, NgaySinh = ?, " +
                    "GioiTinh = ?, TinhTrangSucKhoe = ?, MaKH = ? " +
                    "WHERE MaThuCung = ?";
        
        executeUpdate(sql,
            tc.getTenThuCung(),
            tc.getLoai(),
            tc.getGiong(),
            Date.valueOf(tc.getNgaySinh()),
            tc.getGioiTinh(),
            tc.getTinhTrangSucKhoe(),
            tc.getMaKH(),
            tc.getMaThuCung()
        );
    }
    
    @Override
    public void delete(int maThuCung) throws SQLException {
        String sql = "DELETE FROM THU_CUNG WHERE MaThuCung = ?";
        executeUpdate(sql, maThuCung);
    }
    
    @Override
    public List<ThuCung> findAll() throws SQLException {
        String sql = "SELECT tc.*, kh.HoTen AS TenChu " +
                    "FROM THU_CUNG tc " +
                    "LEFT JOIN KHACH_HANG kh ON tc.MaKH = kh.MaKH " +
                    "ORDER BY tc.TenThuCung";
        
        return executeQuery(sql, this::mapResultSet);
    }
    
    // Additional methods
    public List<ThuCung> findByKhachHang(int maKH) throws SQLException {
        String sql = "SELECT tc.*, kh.HoTen AS TenChu " +
                    "FROM THU_CUNG tc " +
                    "LEFT JOIN KHACH_HANG kh ON tc.MaKH = kh.MaKH " +
                    "WHERE tc.MaKH = ? " +
                    "ORDER BY tc.TenThuCung";
        
        return executeQuery(sql, this::mapResultSet, maKH);
    }
    
    public List<ThuCung> searchByName(String name) throws SQLException {
        String sql = "SELECT tc.*, kh.HoTen AS TenChu " +
                    "FROM THU_CUNG tc " +
                    "LEFT JOIN KHACH_HANG kh ON tc.MaKH = kh.MaKH " +
                    "WHERE tc.TenThuCung LIKE ? OR kh.HoTen LIKE ? " +
                    "ORDER BY tc.TenThuCung";
        
        String searchTerm = "%" + name + "%";
        return executeQuery(sql, this::mapResultSet, searchTerm, searchTerm);
    }
    
    public List<ThuCung> searchByKhachHangAndName(int maKH, String name) throws SQLException {
        String sql = "SELECT tc.*, kh.HoTen AS TenChu " +
                    "FROM THU_CUNG tc " +
                    "LEFT JOIN KHACH_HANG kh ON tc.MaKH = kh.MaKH " +
                    "WHERE tc.MaKH = ? AND tc.TenThuCung LIKE ? " +
                    "ORDER BY tc.TenThuCung";
        
        String searchTerm = "%" + name + "%";
        return executeQuery(sql, this::mapResultSet, maKH, searchTerm);
    }
    
    public List<ThuCung> findByLoai(String loai) throws SQLException {
        String sql = "SELECT tc.*, kh.HoTen AS TenChu " +
                    "FROM THU_CUNG tc " +
                    "LEFT JOIN KHACH_HANG kh ON tc.MaKH = kh.MaKH " +
                    "WHERE tc.Loai = ? " +
                    "ORDER BY tc.TenThuCung";
        
        return executeQuery(sql, this::mapResultSet, loai);
    }
    
    // Map ResultSet to ThuCung object
    private ThuCung mapResultSet(ResultSet rs) throws SQLException {
        ThuCung tc = new ThuCung();
        
        tc.setMaThuCung(rs.getInt("MaThuCung"));
        tc.setTenThuCung(rs.getString("TenThuCung"));
        tc.setLoai(rs.getString("Loai"));
        tc.setGiong(rs.getString("Giong"));
        
        Date ngaySinh = rs.getDate("NgaySinh");
        if (ngaySinh != null) {
            tc.setNgaySinh(ngaySinh.toLocalDate());
        }
        
        tc.setGioiTinh(rs.getString("GioiTinh"));
        tc.setTinhTrangSucKhoe(rs.getString("TinhTrangSucKhoe"));
        tc.setMaKH(rs.getInt("MaKH"));
        
        // Thông tin thêm
        if (columnExists(rs, "TenChu")) {
            tc.setTenChu(rs.getString("TenChu"));
        }
        
        return tc;
    }
    
    private boolean columnExists(ResultSet rs, String columnName) {
        try {
            rs.findColumn(columnName);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}