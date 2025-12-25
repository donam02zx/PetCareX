package com.petcarex.dao;

import com.petcarex.model.KhachHang;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;

public class KhachHangDAO extends BaseDAO<KhachHang> {
    
    @Override
    public void create(KhachHang kh) throws SQLException {
        String sql = "INSERT INTO KHACH_HANG (HoTen, CCCD, SoDienThoai, Email, " +
                    "GioiTinh, NgaySinh, DiaChi, CapDoThanhVien, DiemLoyalty, " +
                    "NgayDangKy, TongChiTieuNam) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        executeUpdate(sql,
            kh.getHoTen(),
            kh.getCccd(),
            kh.getSoDienThoai(),
            kh.getEmail(),
            kh.getGioiTinh(),
            Date.valueOf(kh.getNgaySinh()),
            kh.getDiaChi(),
            kh.getCapDoThanhVien(),
            kh.getDiemLoyalty(),
            Date.valueOf(kh.getNgayDangKy()),
            kh.getTongChiTieuNam()
        );
    }
    
    @Override
    public KhachHang read(int maKH) throws SQLException {
        String sql = "SELECT * FROM KHACH_HANG WHERE MaKH = ?";
        
        return executeQuerySingle(sql, this::mapResultSet, maKH);
    }
    
    @Override
    public void update(KhachHang kh) throws SQLException {
        String sql = "UPDATE KHACH_HANG SET HoTen = ?, CCCD = ?, SoDienThoai = ?, " +
                    "Email = ?, GioiTinh = ?, NgaySinh = ?, DiaChi = ?, " +
                    "CapDoThanhVien = ?, DiemLoyalty = ?, TongChiTieuNam = ? " +
                    "WHERE MaKH = ?";
        
        executeUpdate(sql,
            kh.getHoTen(),
            kh.getCccd(),
            kh.getSoDienThoai(),
            kh.getEmail(),
            kh.getGioiTinh(),
            Date.valueOf(kh.getNgaySinh()),
            kh.getDiaChi(),
            kh.getCapDoThanhVien(),
            kh.getDiemLoyalty(),
            kh.getTongChiTieuNam(),
            kh.getMaKH()
        );
    }
    
    @Override
    public void delete(int maKH) throws SQLException {
        String sql = "DELETE FROM KHACH_HANG WHERE MaKH = ?";
        executeUpdate(sql, maKH);
    }
    
    @Override
    public List<KhachHang> findAll() throws SQLException {
        String sql = "SELECT * FROM KHACH_HANG ORDER BY HoTen";
        return executeQuery(sql, this::mapResultSet);
    }
    
    // Additional methods
    public KhachHang findByCCCD(String cccd) throws SQLException {
        String sql = "SELECT * FROM KHACH_HANG WHERE CCCD = ?";
        return executeQuerySingle(sql, this::mapResultSet, cccd);
    }
    
    public KhachHang findByPhone(String phone) throws SQLException {
        String sql = "SELECT * FROM KHACH_HANG WHERE SoDienThoai = ?";
        return executeQuerySingle(sql, this::mapResultSet, phone);
    }
    
    public List<KhachHang> searchByName(String name) throws SQLException {
        String sql = "SELECT * FROM KHACH_HANG WHERE HoTen LIKE ? ORDER BY HoTen";
        return executeQuery(sql, this::mapResultSet, "%" + name + "%");
    }
    
    public List<KhachHang> findByMemberLevel(String level) throws SQLException {
        String sql = "SELECT * FROM KHACH_HANG WHERE CapDoThanhVien = ? ORDER BY HoTen";
        return executeQuery(sql, this::mapResultSet, level);
    }
    
    // Thêm method này vào class KhachHangDAO
    public KhachHang findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM KHACH_HANG WHERE Email = ?";
        return executeQuerySingle(sql, this::mapResultSet, email);
    }
    
    // Map ResultSet to KhachHang object
    private KhachHang mapResultSet(ResultSet rs) throws SQLException {
        KhachHang kh = new KhachHang();
        
        kh.setMaKH(rs.getInt("MaKH"));
        kh.setHoTen(rs.getString("HoTen"));
        kh.setCccd(rs.getString("CCCD"));
        kh.setSoDienThoai(rs.getString("SoDienThoai"));
        kh.setEmail(rs.getString("Email"));
        kh.setGioiTinh(rs.getString("GioiTinh"));
        
        Date ngaySinh = rs.getDate("NgaySinh");
        if (ngaySinh != null) {
            kh.setNgaySinh(ngaySinh.toLocalDate());
        }
        
        kh.setDiaChi(rs.getString("DiaChi"));
        kh.setCapDoThanhVien(rs.getString("CapDoThanhVien"));
        kh.setDiemLoyalty(rs.getInt("DiemLoyalty"));
        
        Date ngayDangKy = rs.getDate("NgayDangKy");
        if (ngayDangKy != null) {
            kh.setNgayDangKy(ngayDangKy.toLocalDate());
        }
        
        kh.setTongChiTieuNam(rs.getDouble("TongChiTieuNam"));
        
        return kh;
    }
}