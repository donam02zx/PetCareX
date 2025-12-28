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
    	           "dv.TenDichVu, " +
    	           "hd.MaHoaDon, " +
    	           "hd.TrangThai AS TrangThaiHoaDon, " +  // Lấy từ bảng HOA_DON
    	           "hd.TongTien AS TongTienHoaDon " +     // Thêm tổng tiền hóa đơn nếu cần
    	           "FROM LICH_HEN lh " +
    	           "LEFT JOIN THU_CUNG tc ON lh.MaThuCung = tc.MaThuCung " +
    	           "LEFT JOIN KHACH_HANG kh ON tc.MaKH = kh.MaKH " + // THÊM JOIN NÀY
    	           "LEFT JOIN CHI_NHANH cn ON lh.MaChiNhanh = cn.MaChiNhanh " +
    	           "LEFT JOIN NHAN_VIEN nv ON lh.MaBacSi = nv.MaNV " +
    	           "LEFT JOIN DICH_VU dv ON lh.MaDichVu = dv.MaDichVu "+
    	 		   "LEFT JOIN HOA_DON hd ON lh.MaHoaDon = hd.MaHoaDon"; // THÊM JOIN
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
        
        // Thêm mapping cho hóa đơn
        Integer maHoaDon = rs.getInt("MaHoaDon");
        if (!rs.wasNull()) {
            lh.setMaHoaDon(maHoaDon);
        }
        
        lh.setTrangThaiHoaDon(rs.getString("TrangThaiHoaDon"));
        
        return lh;
    }
    
//    public int taoHoaDonTuLichHen(int maLichHen) throws SQLException {
//        // Lấy thông tin lịch hẹn
//        String sqlSelect = "SELECT lh.*, tc.MaKH, dv.GiaCoBan AS GiaDichVu " +
//                          "FROM LICH_HEN lh " +
//                          "LEFT JOIN THU_CUNG tc ON lh.MaThuCung = tc.MaThuCung " +
//                          "LEFT JOIN DICH_VU dv ON lh.MaDichVu = dv.MaDichVu " +
//                          "WHERE lh.MaLichHen = ?";
//        
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//        
//        try {
//            conn = getConnection();
//            
//            // Lấy thông tin lịch hẹn
//            stmt = conn.prepareStatement(sqlSelect);
//            stmt.setInt(1, maLichHen);
//            rs = stmt.executeQuery();
//            
//            if (!rs.next()) {
//                throw new SQLException("Không tìm thấy lịch hẹn");
//            }
//            
//            int maKH = rs.getInt("MaKH");
//            int maBacSi = rs.getInt("MaBacSi");
//            int maDichVu = rs.getInt("MaDichVu");
//            int maChiNhanh = rs.getInt("MaChiNhanh");
//            double giaDichVu = rs.getDouble("GiaDichVu");
//            
//            // Tính giá dựa trên loại lịch hẹn
//            double tongTien = tinhTienDichVu(rs.getString("LoaiLichHen"), giaDichVu);
//            
//            // Tạo hóa đơn
//            String sqlInsert = "INSERT INTO HOA_DON (MaKH, MaNV, MaChiNhanh, NgayLap, " +
//                             "TongTienHang, TongTien, TrangThai, GhiChuLichHen) " +
//                             "VALUES (?, ?, ?, NOW(), ?, ?, 'Chưa thanh toán', CONCAT('Từ lịch hẹn #', ?))";
//            
//            stmt = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
//            stmt.setInt(1, maKH);
//            stmt.setInt(2, maBacSi != 0 ? maBacSi : 1); // Nếu không có bác sĩ, dùng NV mặc định
//            stmt.setInt(3, maChiNhanh);
//            stmt.setDouble(4, tongTien);
//            stmt.setDouble(5, tongTien);
//            stmt.setInt(6, maLichHen);
//            
//            stmt.executeUpdate();
//            
//            // Lấy ID hóa đơn vừa tạo
//            rs = stmt.getGeneratedKeys();
//            if (rs.next()) {
//                int maHoaDon = rs.getInt(1);
//                
//                // Cập nhật lịch hẹn với mã hóa đơn (thêm cột MaHoaDon vào bảng LICH_HEN nếu chưa có)
//                // Hoặc tạo bảng liên kết LICH_HEN_HOA_DON
//                return maHoaDon;
//            }
//            
//            throw new SQLException("Không thể tạo hóa đơn");
//            
//        } finally {
//            closeResources(rs, stmt, conn);
//        }
//    }
    
//    public int taoHoaDonTuLichHen(int maLichHen) throws SQLException {
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//        
//        try {
//            conn = getConnection();
//            
//            // 1. Lấy thông tin lịch hẹn và khách hàng
//            String sqlSelect = "SELECT lh.*, tc.MaKH, tc.TenThuCung, " +
//                              "dv.GiaCoBan AS GiaDichVu " +  // Sửa thành GiaCoBan
//                              "FROM LICH_HEN lh " +
//                              "LEFT JOIN THU_CUNG tc ON lh.MaThuCung = tc.MaThuCung " +
//                              "LEFT JOIN DICH_VU dv ON lh.MaDichVu = dv.MaDichVu " +
//                              "WHERE lh.MaLichHen = ?";
//            
//            stmt = conn.prepareStatement(sqlSelect);
//            stmt.setInt(1, maLichHen);
//            rs = stmt.executeQuery();
//            
//            if (!rs.next()) {
//                throw new SQLException("Không tìm thấy lịch hẹn");
//            }
//            
//            int maKH = rs.getInt("MaKH");
//            int maBacSi = rs.getInt("MaBacSi");
//            int maDichVu = rs.getInt("MaDichVu");
//            int maChiNhanh = rs.getInt("MaChiNhanh");
//            String loaiLichHen = rs.getString("LoaiLichHen");
//            String tenThuCung = rs.getString("TenThuCung");
//            String ghiChu = rs.getString("GhiChu");
//            double giaDichVu = rs.getDouble("GiaDichVu");
//            
//            // 2. Tính tổng tiền
//            double tongTien = tinhTienDichVu(loaiLichHen, giaDichVu);
//            
//            // 3. Tạo hóa đơn
//            String sqlInsert = "INSERT INTO HOA_DON (MaKH, MaNV, MaChiNhanh, NgayLap, " +
//                             "TongTienHang, TongTien, TrangThai, GhiChu) " +
//                             "VALUES (?, ?, ?, NOW(), ?, ?, 'Chưa thanh toán', ?)";
//            
//            stmt = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
//            stmt.setInt(1, maKH);
//            stmt.setInt(2, maBacSi != 0 ? maBacSi : 1); // Dùng bác sĩ hoặc NV mặc định
//            stmt.setInt(3, maChiNhanh);
//            stmt.setDouble(4, tongTien);
//            stmt.setDouble(5, tongTien);
//            
//            String ghiChuHoaDon = String.format("Tạo từ lịch hẹn #%d - Thú cưng: %s", 
//                                               maLichHen, tenThuCung);
//            if (ghiChu != null && !ghiChu.isEmpty()) {
//                ghiChuHoaDon += " - Ghi chú: " + ghiChu;
//            }
//            stmt.setString(6, ghiChuHoaDon);
//            
//            int affectedRows = stmt.executeUpdate();
//            if (affectedRows == 0) {
//                throw new SQLException("Tạo hóa đơn thất bại");
//            }
//            
//            // 4. Lấy mã hóa đơn vừa tạo
//            rs = stmt.getGeneratedKeys();
//            if (rs.next()) {
//                int maHoaDon = rs.getInt(1);
//                
//                // 5. Cập nhật mã hóa đơn vào lịch hẹn
//                String sqlUpdate = "UPDATE LICH_HEN SET MaHoaDon = ? WHERE MaLichHen = ?";
//                stmt = conn.prepareStatement(sqlUpdate);
//                stmt.setInt(1, maHoaDon);
//                stmt.setInt(2, maLichHen);
//                stmt.executeUpdate();
//                
//                // 6. Ghi log
//                System.out.println("Đã tạo hóa đơn #" + maHoaDon + 
//                                 " từ lịch hẹn #" + maLichHen + 
//                                 " - Tổng tiền: " + String.format("%,.0f", tongTien) + " VND");
//                
//                return maHoaDon;
//            }
//            
//            throw new SQLException("Không thể lấy mã hóa đơn");
//            
//        } finally {
//            closeResources(rs, stmt, conn);
//        }
//    }
    
    public int taoHoaDonTuLichHen(int maLichHen) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            
            // CÁCH 1: Query đơn giản hơn, chỉ lấy thông tin cần thiết
            String sqlSelect = "SELECT " +
                              "lh.MaLichHen, lh.MaThuCung, lh.MaBacSi, lh.MaDichVu, " +
                              "lh.MaChiNhanh, lh.LoaiLichHen, lh.GhiChu, " +
                              "tc.MaKH, tc.TenThuCung, " +
                              "CASE WHEN dv.GiaCoBan IS NULL THEN 0 ELSE dv.GiaCoBan END AS GiaDichVu " +
                              "FROM LICH_HEN lh " +
                              "LEFT JOIN THU_CUNG tc ON lh.MaThuCung = tc.MaThuCung " +
                              "LEFT JOIN DICH_VU dv ON lh.MaDichVu = dv.MaDichVu " +
                              "WHERE lh.MaLichHen = ?";
            
            stmt = conn.prepareStatement(sqlSelect);
            stmt.setInt(1, maLichHen);
            rs = stmt.executeQuery();
            
            if (!rs.next()) {
                throw new SQLException("Không tìm thấy lịch hẹn #" + maLichHen);
            }
            
            int maKH = rs.getInt("MaKH");
            int maBacSi = rs.getInt("MaBacSi");
            int maDichVu = rs.getInt("MaDichVu");
            int maChiNhanh = rs.getInt("MaChiNhanh");
            String loaiLichHen = rs.getString("LoaiLichHen");
            String tenThuCung = rs.getString("TenThuCung");
            String ghiChu = rs.getString("GhiChu");
            double giaDichVu = rs.getDouble("GiaDichVu");
            
            // Debug
            System.out.println("=== DEBUG TẠO HÓA ĐƠN ===");
            System.out.println("MaLichHen: " + maLichHen);
            System.out.println("MaKH: " + maKH);
            System.out.println("MaDichVu: " + maDichVu);
            System.out.println("GiaDichVu: " + giaDichVu);
            System.out.println("LoaiLichHen: " + loaiLichHen);
            
            // 2. Tính tổng tiền
            double tongTien = tinhTienDichVu(loaiLichHen, giaDichVu);
            System.out.println("TongTien: " + tongTien);
            
            // 3. Kiểm tra dữ liệu hợp lệ
            if (maKH <= 0) {
                throw new SQLException("Không tìm thấy khách hàng cho lịch hẹn này");
            }
            
            // 4. Tạo hóa đơn
            String sqlInsert = "INSERT INTO HOA_DON (MaKH, MaNV, MaChiNhanh, NgayLap, " +
                             "TongTienHang, TongTien, TrangThai) " +
                             "VALUES (?, ?, ?, NOW(), ?, ?, 'Chưa thanh toán')";
            
            stmt = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, maKH);
            stmt.setInt(2, maBacSi != 0 ? maBacSi : 1);
            stmt.setInt(3, maChiNhanh);
            stmt.setDouble(4, tongTien);
            stmt.setDouble(5, tongTien);
            
            String ghiChuHoaDon = String.format("Tạo từ lịch hẹn #%d - Thú cưng: %s", 
                                               maLichHen, tenThuCung);
            if (ghiChu != null && !ghiChu.isEmpty()) {
                ghiChuHoaDon += " - Ghi chú: " + ghiChu;
            }
            stmt.setString(6, ghiChuHoaDon);
            
            System.out.println("Executing INSERT...");
            int affectedRows = stmt.executeUpdate();
            System.out.println("Affected rows: " + affectedRows);
            
            if (affectedRows == 0) {
                throw new SQLException("Tạo hóa đơn thất bại");
            }
            
            // 5. Lấy mã hóa đơn vừa tạo
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int maHoaDon = rs.getInt(1);
                System.out.println("Created MaHoaDon: " + maHoaDon);
                
                // 6. Cập nhật mã hóa đơn vào lịch hẹn
                String sqlUpdate = "UPDATE LICH_HEN SET MaHoaDon = ? WHERE MaLichHen = ?";
                stmt = conn.prepareStatement(sqlUpdate);
                stmt.setInt(1, maHoaDon);
                stmt.setInt(2, maLichHen);
                stmt.executeUpdate();
                
                System.out.println("SUCCESS: Đã tạo hóa đơn #" + maHoaDon + 
                                 " từ lịch hẹn #" + maLichHen + 
                                 " - Tổng tiền: " + String.format("%,.0f", tongTien) + " VND");
                
                return maHoaDon;
            }
            
            throw new SQLException("Không thể lấy mã hóa đơn");
            
        } catch (SQLException e) {
            System.err.println("ERROR trong taoHoaDonTuLichHen: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            throw e;
        } finally {
            closeResources(rs, stmt, conn);
        }
    }
    
    public int taoHoaDonDonGian(int maLichHen) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            
            // CHỈ lấy thông tin cơ bản, không join với DICH_VU
            String sqlSelect = "SELECT lh.MaLichHen, lh.MaThuCung, lh.MaBacSi, " +
                              "lh.MaChiNhanh, lh.LoaiLichHen, lh.GhiChu, tc.MaKH, tc.TenThuCung " +
                              "FROM LICH_HEN lh " +
                              "LEFT JOIN THU_CUNG tc ON lh.MaThuCung = tc.MaThuCung " +
                              "WHERE lh.MaLichHen = ?";
            
            stmt = conn.prepareStatement(sqlSelect);
            stmt.setInt(1, maLichHen);
            rs = stmt.executeQuery();
            
            if (!rs.next()) {
                throw new SQLException("Không tìm thấy lịch hẹn");
            }
            
            int maKH = rs.getInt("MaKH");
            int maBacSi = rs.getInt("MaBacSi");
            int maChiNhanh = rs.getInt("MaChiNhanh");
            String loaiLichHen = rs.getString("LoaiLichHen");
            String tenThuCung = rs.getString("TenThuCung");
            String ghiChu = rs.getString("GhiChu");
            
            // Chỉ dùng giá mặc định theo loại lịch hẹn
            double tongTien = tinhTienDichVu(loaiLichHen, 0);
            
            // Tạo hóa đơn
            String sqlInsert = "INSERT INTO HOA_DON (MaKH, MaNV, MaChiNhanh, NgayLap, " +
                             "TongTienHang, TongTien, TrangThai) " +
                             "VALUES (?, ?, ?, NOW(), ?, ?, 'Chưa thanh toán')";
            
            stmt = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, maKH);
            stmt.setInt(2, maBacSi != 0 ? maBacSi : 1);
            stmt.setInt(3, maChiNhanh);
            stmt.setDouble(4, tongTien);
            stmt.setDouble(5, tongTien);
            
            String ghiChuHoaDon = "Tạo từ lịch hẹn #" + maLichHen + " - Thú cưng: " + tenThuCung;
            stmt.setString(6, ghiChuHoaDon);
            
            stmt.executeUpdate();
            
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int maHoaDon = rs.getInt(1);
                
                // Cập nhật lịch hẹn
                String sqlUpdate = "UPDATE LICH_HEN SET MaHoaDon = ? WHERE MaLichHen = ?";
                stmt = conn.prepareStatement(sqlUpdate);
                stmt.setInt(1, maHoaDon);
                stmt.setInt(2, maLichHen);
                stmt.executeUpdate();
                
                return maHoaDon;
            }
            
            return -1;
            
        } finally {
            closeResources(rs, stmt, conn);
        }
    }
    
    

    private double tinhTienDichVu(String loaiLichHen, double giaDichVu) {
        if (giaDichVu > 0) {
            return giaDichVu;
        }
        
        // Giá mặc định theo loại lịch hẹn
        switch (loaiLichHen) {
            case "Khám bệnh":
                return 150000;
            case "Tiêm phòng":
                return 350000;
            case "Spa & Grooming":
                return 200000;
            default:
                return 250000;
        }
    }
}