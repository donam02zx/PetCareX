// file: com/petcarex/dao/HoaDonDAO.java
package com.petcarex.dao;

import com.petcarex.config.DatabaseConnection;
import com.petcarex.model.HoaDon;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAO extends BaseDAO<HoaDon> {
    
    @Override
    public void create(HoaDon hd) throws SQLException {
        String sql = "INSERT INTO HOA_DON (MaKH, MaNV, MaChiNhanh, NgayLap, " +
                    "TongTienHang, ChietKhauThanhVien, ChietKhauKhac, TongTien, " +
                    "HinhThucThanhToan, DiemSuDung, DiemCong, TrangThai) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        executeUpdate(sql,
            hd.getMaKH(),
            hd.getMaNV(),
            hd.getMaChiNhanh(),
            Timestamp.valueOf(hd.getNgayLap()),
            hd.getTongTienHang(),
            hd.getChietKhauThanhVien(),
            hd.getChietKhauKhac(),
            hd.getTongTien(),
            hd.getHinhThucThanhToan(),
            hd.getDiemSuDung(),
            hd.getDiemCong(),
            hd.getTrangThai()
        );
    }
    
    @Override
    public HoaDon read(int maHoaDon) throws SQLException {
        String sql = getQueryWithJoins() + " WHERE hd.MaHoaDon = ?";
        return executeQuerySingle(sql, this::mapResultSetWithJoins, maHoaDon);
    }
    
    @Override
    public void update(HoaDon hd) throws SQLException {
        String sql = "UPDATE HOA_DON SET MaKH = ?, MaNV = ?, MaChiNhanh = ?, " +
                    "TongTienHang = ?, ChietKhauThanhVien = ?, ChietKhauKhac = ?, " +
                    "TongTien = ?, HinhThucThanhToan = ?, DiemSuDung = ?, " +
                    "DiemCong = ?, TrangThai = ? WHERE MaHoaDon = ?";
        
        executeUpdate(sql,
            hd.getMaKH(),
            hd.getMaNV(),
            hd.getMaChiNhanh(),
            hd.getTongTienHang(),
            hd.getChietKhauThanhVien(),
            hd.getChietKhauKhac(),
            hd.getTongTien(),
            hd.getHinhThucThanhToan(),
            hd.getDiemSuDung(),
            hd.getDiemCong(),
            hd.getTrangThai(),
            hd.getMaHoaDon()
        );
    }
    
    @Override
    public void delete(int maHoaDon) throws SQLException {
        String sql = "DELETE FROM HOA_DON WHERE MaHoaDon = ?";
        executeUpdate(sql, maHoaDon);
    }
    
    @Override
    public List<HoaDon> findAll() throws SQLException {
        String sql = getQueryWithJoins() + " ORDER BY hd.NgayLap DESC";
        return executeQuery(sql, this::mapResultSetWithJoins);
    }
    
    // Additional methods
    public List<HoaDon> findByKhachHang(int maKH) throws SQLException {
        String sql = getQueryWithJoins() + 
                    " WHERE hd.MaKH = ? " +
                    "ORDER BY hd.NgayLap DESC";
        return executeQuery(sql, this::mapResultSetWithJoins, maKH);
    }
    
    public List<HoaDon> findByNhanVien(int maNV) throws SQLException {
        String sql = getQueryWithJoins() + 
                    " WHERE hd.MaNV = ? " +
                    "ORDER BY hd.NgayLap DESC";
        return executeQuery(sql, this::mapResultSetWithJoins, maNV);
    }
    
    public List<HoaDon> findByChiNhanh(int maChiNhanh) throws SQLException {
        String sql = getQueryWithJoins() + 
                    " WHERE hd.MaChiNhanh = ? " +
                    "ORDER BY hd.NgayLap DESC";
        return executeQuery(sql, this::mapResultSetWithJoins, maChiNhanh);
    }
    
    public List<HoaDon> findByTrangThai(String trangThai) throws SQLException {
        String sql = getQueryWithJoins() + 
                    " WHERE hd.TrangThai = ? " +
                    "ORDER BY hd.NgayLap DESC";
        return executeQuery(sql, this::mapResultSetWithJoins, trangThai);
    }
    
    public List<HoaDon> findByDateRange(LocalDateTime from, LocalDateTime to) throws SQLException {
        String sql = getQueryWithJoins() + 
                    " WHERE hd.NgayLap BETWEEN ? AND ? " +
                    "ORDER BY hd.NgayLap DESC";
        return executeQuery(sql, this::mapResultSetWithJoins, 
            Timestamp.valueOf(from), Timestamp.valueOf(to));
    }
    
    public void thanhToanHoaDon(int maHoaDon, String hinhThucThanhToan) throws SQLException {
        String sql = "UPDATE HOA_DON SET TrangThai = 'Đã thanh toán', " +
                    "HinhThucThanhToan = ? WHERE MaHoaDon = ?";
        executeUpdate(sql, hinhThucThanhToan, maHoaDon);
    }
    
    public void huyHoaDon(int maHoaDon) throws SQLException {
        String sql = "UPDATE HOA_DON SET TrangThai = 'Đã hủy' WHERE MaHoaDon = ?";
        executeUpdate(sql, maHoaDon);
    }
    
    // Thống kê
//    public double getTongDoanhThu(LocalDateTime from, LocalDateTime to) throws SQLException {
//        String sql = "SELECT COALESCE(SUM(TongTien), 0) FROM HOA_DON " +
//                    "WHERE TrangThai = 'Đã thanh toán' AND NgayLap BETWEEN ? AND ?";
//        
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setTimestamp(1, Timestamp.valueOf(from));
//            stmt.setTimestamp(2, Timestamp.valueOf(to));
//            
//            ResultSet rs = stmt.executeQuery();
//            if (rs.next()) {
//                return rs.getDouble(1);
//            }
//        }
//        return 0;
//    }
    
    public double getTongDoanhThu(LocalDateTime from, LocalDateTime to) throws SQLException {
        String sql = "SELECT COALESCE(SUM(TongTien), 0) FROM HOA_DON " +
                    "WHERE TrangThai = 'Đã thanh toán' AND NgayLap BETWEEN ? AND ?";
        
        try (Connection conn = DatabaseConnection.getStaticConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(from));
            stmt.setTimestamp(2, Timestamp.valueOf(to));
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        }
        return 0;
    }
    
    // Helper method for joins query
    private String getQueryWithJoins() {
        return "SELECT hd.*, " +
               "kh.HoTen AS TenKhachHang, " +
               "nv.HoTen AS TenNhanVien, " +
               "cn.TenChiNhanh " +
               "FROM HOA_DON hd " +
               "LEFT JOIN KHACH_HANG kh ON hd.MaKH = kh.MaKH " +
               "LEFT JOIN NHAN_VIEN nv ON hd.MaNV = nv.MaNV " +
               "LEFT JOIN CHI_NHANH cn ON hd.MaChiNhanh = cn.MaChiNhanh";
    }
    
    // Map ResultSet (with joins)
    private HoaDon mapResultSetWithJoins(ResultSet rs) throws SQLException {
        HoaDon hd = new HoaDon();
        
        hd.setMaHoaDon(rs.getInt("MaHoaDon"));
        hd.setMaKH(rs.getInt("MaKH"));
        hd.setMaNV(rs.getInt("MaNV"));
        hd.setMaChiNhanh(rs.getInt("MaChiNhanh"));
        
        Timestamp ngayLap = rs.getTimestamp("NgayLap");
        if (ngayLap != null) {
            hd.setNgayLap(ngayLap.toLocalDateTime());
        }
        
        hd.setTongTienHang(rs.getDouble("TongTienHang"));
        hd.setChietKhauThanhVien(rs.getDouble("ChietKhauThanhVien"));
        hd.setChietKhauKhac(rs.getDouble("ChietKhauKhac"));
        hd.setTongTien(rs.getDouble("TongTien"));
        hd.setHinhThucThanhToan(rs.getString("HinhThucThanhToan"));
        hd.setDiemSuDung(rs.getInt("DiemSuDung"));
        hd.setDiemCong(rs.getInt("DiemCong"));
        hd.setTrangThai(rs.getString("TrangThai"));
        
        // Thông tin từ joins
        hd.setTenKhachHang(rs.getString("TenKhachHang"));
        hd.setTenNhanVien(rs.getString("TenNhanVien"));
        hd.setTenChiNhanh(rs.getString("TenChiNhanh"));
        
        return hd;
    }
}