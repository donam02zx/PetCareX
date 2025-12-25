package com.petcarex.controller;

import com.petcarex.service.KhachHangService;
import com.petcarex.model.KhachHang;
import com.petcarex.view.KhachHangView;
import javax.swing.*;
import java.util.List;

public class KhachHangController {
    private final KhachHangService khachHangService;
    private KhachHangView view;
    
    public KhachHangController() {
        this.khachHangService = new KhachHangService();
    }
    
    public JPanel getViewPanel() {
        view = new KhachHangView(this);
        return view;
    }
    
    public List<KhachHang> getAllKhachHang() {
        try {
            return khachHangService.layTatCaKhachHang();
        } catch (Exception e) {
            showError("Lỗi khi tải danh sách khách hàng: " + e.getMessage());
            return null;
        }
    }
    
    public void searchKhachHang(String keyword) {
        try {
            List<KhachHang> results = khachHangService.timKiemTheoTen(keyword);
            view.displayKhachHang(results);
        } catch (Exception e) {
            showError("Lỗi khi tìm kiếm: " + e.getMessage());
        }
    }
    
    public void addKhachHang(KhachHang kh) {
        try {
            KhachHang newKH = khachHangService.themKhachHang(kh);
            view.showSuccess("Thêm khách hàng thành công! Mã KH: " + newKH.getMaKH());
            view.refreshTable();
        } catch (Exception e) {
            showError("Lỗi khi thêm khách hàng: " + e.getMessage());
        }
    }
    
    public void updateKhachHang(KhachHang kh) {
        try {
            khachHangService.capNhatKhachHang(kh);
            view.showSuccess("Cập nhật khách hàng thành công!");
            view.refreshTable();
        } catch (Exception e) {
            showError("Lỗi khi cập nhật: " + e.getMessage());
        }
    }
    
    public void deleteKhachHang(int maKH) {
        try {
            int confirm = JOptionPane.showConfirmDialog(view,
                "Bạn có chắc muốn xóa khách hàng này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                khachHangService.xoaKhachHang(maKH);
                view.showSuccess("Xóa khách hàng thành công!");
                view.refreshTable();
            }
        } catch (Exception e) {
            showError("Lỗi khi xóa khách hàng: " + e.getMessage());
        }
    }
    
    private void showError(String message) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(view, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
        });
    }
}