// file: com/petcarex/controller/ThanhToanController.java
package com.petcarex.controller;

import com.petcarex.service.ThanhToanService;
import com.petcarex.service.HoaDonService;
import com.petcarex.service.KhachHangService;
import com.petcarex.model.HoaDon;
import com.petcarex.model.ThanhToanRequest;
import com.petcarex.model.KhachHang;
import com.petcarex.view.ThanhToanView;
import javax.swing.*;
import java.util.List;

public class ThanhToanController {
    private final ThanhToanService thanhToanService;
    private final HoaDonService hoaDonService;
    private final KhachHangService khachHangService;
    private ThanhToanView view;
    
    public ThanhToanController() {
        this.thanhToanService = new ThanhToanService();
        this.hoaDonService = new HoaDonService();
        this.khachHangService = new KhachHangService();
    }
    
    public JPanel getViewPanel() {
        view = new ThanhToanView(this);
        return view;
    }
    
    public List<HoaDon> getHoaDonChuaThanhToan() {
        try {
            return hoaDonService.layHoaDonTheoTrangThai("Chưa thanh toán");
        } catch (Exception e) {
            showError("Lỗi khi tải hóa đơn: " + e.getMessage());
            return null;
        }
    }
    
    public List<HoaDon> getHoaDonDaThanhToan() {
        try {
            return hoaDonService.layHoaDonTheoTrangThai("Đã thanh toán");
        } catch (Exception e) {
            showError("Lỗi khi tải hóa đơn: " + e.getMessage());
            return null;
        }
    }
    
    public KhachHang getKhachHang(int maKH) {
        try {
            return khachHangService.timKhachHang(maKH);
        } catch (Exception e) {
            showError("Lỗi khi tải thông tin khách hàng: " + e.getMessage());
            return null;
        }
    }
    
    public HoaDon getHoaDonChiTiet(int maHoaDon) {
        try {
            return hoaDonService.timHoaDon(maHoaDon);
        } catch (Exception e) {
            showError("Lỗi khi tải chi tiết hóa đơn: " + e.getMessage());
            return null;
        }
    }
    
    public boolean thucHienThanhToan(ThanhToanRequest request) {
        try {
            // Giả sử nhân viên hiện tại là NV có mã 3
            int maNhanVienHienTai = 3;
            HoaDon hoaDon = thanhToanService.thanhToan(request, maNhanVienHienTai);
            if (hoaDon != null) {
                view.showSuccess("Thanh toán thành công! Mã HD: " + hoaDon.getMaHoaDon() + 
                               "\nTổng tiền: " + String.format("%,.0f", hoaDon.getTongTien()) + " VND" +
                               "\nĐiểm cộng: " + hoaDon.getDiemCong() + " điểm");
                view.refreshTable();
                return true;
            }
            return false;
        } catch (Exception e) {
            showError("Lỗi khi thanh toán: " + e.getMessage());
            return false;
        }
    }
    
    public boolean huyHoaDon(int maHoaDon) {
        try {
            int confirm = JOptionPane.showConfirmDialog(view,
                "Bạn có chắc muốn hủy hóa đơn này?",
                "Xác nhận hủy",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                hoaDonService.huyHoaDon(maHoaDon);
                view.showSuccess("Hủy hóa đơn thành công!");
                view.refreshTable();
                return true;
            }
            return false;
        } catch (Exception e) {
            showError("Lỗi khi hủy hóa đơn: " + e.getMessage());
            return false;
        }
    }
    
    private void showError(String message) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(view, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
        });
    }
}