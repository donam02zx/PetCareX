// file: com/petcarex/service/ThanhToanService.java
package com.petcarex.service;

import com.petcarex.dao.HoaDonDAO;
import com.petcarex.dao.KhachHangDAO;
import com.petcarex.model.HoaDon;
import com.petcarex.model.KhachHang;
import com.petcarex.model.ThanhToanRequest;
import com.petcarex.config.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class ThanhToanService {
    private final HoaDonDAO hoaDonDAO;
    private final KhachHangDAO khachHangDAO;
    private final LogDiemService logDiemService;
    
    public ThanhToanService() {
        this.hoaDonDAO = new HoaDonDAO();
        this.khachHangDAO = new KhachHangDAO();
        this.logDiemService = new LogDiemService();
    }
    
    public HoaDon thanhToan(ThanhToanRequest request, int maNhanVien) throws Exception {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getStaticConnection();
            conn.setAutoCommit(false);
            
            // 1. Lấy thông tin hóa đơn
            HoaDon hoaDon = hoaDonDAO.read(request.getMaHoaDon());
            if (hoaDon == null) {
                throw new Exception("Hóa đơn không tồn tại");
            }
            
            if ("Đã thanh toán".equals(hoaDon.getTrangThai())) {
                throw new Exception("Hóa đơn đã được thanh toán");
            }
            
            // 2. Kiểm tra điểm sử dụng
            KhachHang kh = khachHangDAO.read(hoaDon.getMaKH());
            if (kh == null) {
                throw new Exception("Khách hàng không tồn tại");
            }
            
            if (request.getDiemSuDung() > 0) {
                if (kh.getDiemLoyalty() < request.getDiemSuDung()) {
                    throw new Exception("Khách hàng không đủ điểm để sử dụng");
                }
                
                // Trừ điểm loyalty
                kh.setDiemLoyalty(kh.getDiemLoyalty() - request.getDiemSuDung());
                khachHangDAO.update(kh);
                
                // Ghi log sử dụng điểm
                logDiemService.ghiLogSuDungDiem(
                    hoaDon.getMaKH(),
                    request.getDiemSuDung(),
                    "Thanh toán hóa đơn " + hoaDon.getMaHoaDon(),
                    hoaDon.getMaHoaDon(),
                    maNhanVien
                );
                
                hoaDon.setDiemSuDung(request.getDiemSuDung());
            }
            
            // 3. Cập nhật hình thức thanh toán
            hoaDon.setHinhThucThanhToan(request.getHinhThucThanhToan());
            
            // 4. Tính toán chiết khấu thành viên
            double chietKhau = tinhChietKhauThanhVien(kh.getCapDoThanhVien());
            hoaDon.setChietKhauThanhVien(chietKhau);
            
            // 5. Tính lại tổng tiền
            double tongTienCuoi = hoaDon.tinhTongTienSauChietKhau();
            hoaDon.setTongTien(tongTienCuoi);
            
            // 6. Tính điểm cộng
            int diemCong = hoaDon.tinhDiemCongDuKien();
            hoaDon.setDiemCong(diemCong);
            
            // 7. Cập nhật trạng thái hóa đơn
            hoaDon.setTrangThai("Đã thanh toán");
            hoaDonDAO.update(hoaDon);
            
            // 8. Cộng điểm loyalty và ghi log
            logDiemService.ghiLogCongDiem(
                hoaDon.getMaKH(),
                diemCong,
                "Thanh toán hóa đơn " + hoaDon.getMaHoaDon(),
                hoaDon.getMaHoaDon()
            );
            
            // 9. Commit transaction
            conn.commit();
            
            return hoaDon;
            
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private double tinhChietKhauThanhVien(String capDo) {
        switch (capDo) {
            case "VIP":
                return 10.0;
            case "Thân thiết":
                return 5.0;
            default:
                return 0.0;
        }
    }
}