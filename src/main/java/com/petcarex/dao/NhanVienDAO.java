package com.petcarex.dao;

import com.petcarex.model.NhanVien;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;

public class NhanVienDAO extends BaseDAO<NhanVien> {
    
    @Override
    public void create(NhanVien nv) throws SQLException {
        String sql = "INSERT INTO NHAN_VIEN (HoTen, CCCD, NgaySinh, GioiTinh, " +
                    "SoDienThoai, Email, NgayVaoLam, ChucVu, LuongCoBan, " +
                    "TrangThai, MaChiNhanh, ChungChiHanhNghe) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        executeUpdate(sql,
            nv.getHoTen(),
            nv.getCccd(),
            Date.valueOf(nv.getNgaySinh()),
            nv.getGioiTinh(),
            nv.getSoDienThoai(),
            nv.getEmail(),
            Date.valueOf(nv.getNgayVaoLam()),
            nv.getChucVu(),
            nv.getLuongCoBan(),
            nv.getTrangThai(),
            nv.getMaChiNhanh(),
            nv.getChungChiHanhNghe()
        );
    }
    
    @Override
    public NhanVien read(int maNV) throws SQLException {
        String sql = "SELECT * FROM NHAN_VIEN WHERE MaNV = ?";
        return executeQuerySingle(sql, this::mapResultSet, maNV);
    }
    
    @Override
    public void update(NhanVien nv) throws SQLException {
        String sql = "UPDATE NHAN_VIEN SET HoTen = ?, CCCD = ?, NgaySinh = ?, " +
                    "GioiTinh = ?, SoDienThoai = ?, Email = ?, NgayVaoLam = ?, " +
                    "ChucVu = ?, LuongCoBan = ?, TrangThai = ?, MaChiNhanh = ?, " +
                    "ChungChiHanhNghe = ? WHERE MaNV = ?";
        
        executeUpdate(sql,
            nv.getHoTen(),
            nv.getCccd(),
            Date.valueOf(nv.getNgaySinh()),
            nv.getGioiTinh(),
            nv.getSoDienThoai(),
            nv.getEmail(),
            Date.valueOf(nv.getNgayVaoLam()),
            nv.getChucVu(),
            nv.getLuongCoBan(),
            nv.getTrangThai(),
            nv.getMaChiNhanh(),
            nv.getChungChiHanhNghe(),
            nv.getMaNV()
        );
    }
    
    @Override
    public void delete(int maNV) throws SQLException {
        String sql = "DELETE FROM NHAN_VIEN WHERE MaNV = ?";
        executeUpdate(sql, maNV);
    }
    
    @Override
    public List<NhanVien> findAll() throws SQLException {
        String sql = "SELECT * FROM NHAN_VIEN ORDER BY HoTen";
        return executeQuery(sql, this::mapResultSet);
    }
    
    // Additional methods
    public NhanVien findByCCCD(String cccd) throws SQLException {
        String sql = "SELECT * FROM NHAN_VIEN WHERE CCCD = ?";
        return executeQuerySingle(sql, this::mapResultSet, cccd);
    }
    
    public NhanVien findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM NHAN_VIEN WHERE Email = ?";
        return executeQuerySingle(sql, this::mapResultSet, email);
    }
    
    public List<NhanVien> findByChucVu(String chucVu) throws SQLException {
        String sql = "SELECT * FROM NHAN_VIEN WHERE ChucVu = ? ORDER BY HoTen";
        return executeQuery(sql, this::mapResultSet, chucVu);
    }
    
    public List<NhanVien> findByChiNhanh(int maChiNhanh) throws SQLException {
        String sql = "SELECT * FROM NHAN_VIEN WHERE MaChiNhanh = ? ORDER BY HoTen";
        return executeQuery(sql, this::mapResultSet, maChiNhanh);
    }
    
    public List<NhanVien> findDangLamViec() throws SQLException {
        String sql = "SELECT * FROM NHAN_VIEN WHERE TrangThai = 'Đang làm việc' ORDER BY HoTen";
        return executeQuery(sql, this::mapResultSet);
    }
    
    
    // Map ResultSet to NhanVien object
    private NhanVien mapResultSet(ResultSet rs) throws SQLException {
        NhanVien nv = new NhanVien();
        
        nv.setMaNV(rs.getInt("MaNV"));
        nv.setHoTen(rs.getString("HoTen"));
        nv.setCccd(rs.getString("CCCD"));
        
        Date ngaySinh = rs.getDate("NgaySinh");
        if (ngaySinh != null) {
            nv.setNgaySinh(ngaySinh.toLocalDate());
        }
        
        nv.setGioiTinh(rs.getString("GioiTinh"));
        nv.setSoDienThoai(rs.getString("SoDienThoai"));
        nv.setEmail(rs.getString("Email"));
        
        Date ngayVaoLam = rs.getDate("NgayVaoLam");
        if (ngayVaoLam != null) {
            nv.setNgayVaoLam(ngayVaoLam.toLocalDate());
        }
        
        nv.setChucVu(rs.getString("ChucVu"));
        nv.setLuongCoBan(rs.getDouble("LuongCoBan"));
        nv.setTrangThai(rs.getString("TrangThai"));
        
        Integer maChiNhanh = rs.getInt("MaChiNhanh");
        if (!rs.wasNull()) {
            nv.setMaChiNhanh(maChiNhanh);
        }
        
        nv.setChungChiHanhNghe(rs.getString("ChungChiHanhNghe"));
        
        return nv;
    }
}