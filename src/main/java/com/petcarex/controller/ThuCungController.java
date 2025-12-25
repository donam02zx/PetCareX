package com.petcarex.controller;

import com.petcarex.service.ThuCungService;
import com.petcarex.model.ThuCung;
import com.petcarex.view.ThuCungView;
import javax.swing.*;
import java.util.List;

public class ThuCungController {
    private final ThuCungService thuCungService;
    private ThuCungView view;
    private int currentMaKH; // Dùng cho khách hàng xem thú cưng của mình
    private boolean isNhanVien; // Phân biệt nhân viên và khách hàng
    
    public ThuCungController() {
        this.thuCungService = new ThuCungService();
        this.isNhanVien = false;
    }
    
    public ThuCungController(int maKH) {
        this.thuCungService = new ThuCungService();
        this.currentMaKH = maKH;
        this.isNhanVien = false;
    }
    
    public ThuCungController(boolean isNhanVien) {
        this.thuCungService = new ThuCungService();
        this.isNhanVien = isNhanVien;
    }
    
    public JPanel getViewPanel() {
        view = new ThuCungView(this, isNhanVien);
        return view;
    }
    
    public List<ThuCung> getAllThuCung() {
        try {
            if (isNhanVien) {
                return thuCungService.layTatCaThuCung();
            } else {
                return thuCungService.layThuCungTheoKhachHang(currentMaKH);
            }
        } catch (Exception e) {
            showError("Lỗi khi tải danh sách thú cưng: " + e.getMessage());
            return null;
        }
    }
    
    public List<ThuCung> searchThuCung(String keyword) {
        try {
            if (isNhanVien) {
                return thuCungService.timKiemThuCung(keyword);
            } else {
                return thuCungService.timKiemThuCungCuaKhachHang(currentMaKH, keyword);
            }
        } catch (Exception e) {
            showError("Lỗi khi tìm kiếm: " + e.getMessage());
            return null;
        }
    }
    
    public void addThuCung(ThuCung tc) {
        try {
            // Nếu là khách hàng, tự động gán MaKH
            if (!isNhanVien) {
                tc.setMaKH(currentMaKH);
            }
            
            ThuCung newTC = thuCungService.themThuCung(tc);
            view.showSuccess("Thêm thú cưng thành công! Mã TC: " + newTC.getMaThuCung());
            view.refreshTable();
        } catch (Exception e) {
            showError("Lỗi khi thêm thú cưng: " + e.getMessage());
        }
    }
    
    public void updateThuCung(ThuCung tc) {
        try {
            thuCungService.capNhatThuCung(tc);
            view.showSuccess("Cập nhật thú cưng thành công!");
            view.refreshTable();
        } catch (Exception e) {
            showError("Lỗi khi cập nhật: " + e.getMessage());
        }
    }
    
    public void deleteThuCung(int maThuCung) {
        try {
            int confirm = JOptionPane.showConfirmDialog(view,
                "Bạn có chắc muốn xóa thú cưng này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                thuCungService.xoaThuCung(maThuCung);
                view.showSuccess("Xóa thú cưng thành công!");
                view.refreshTable();
            }
        } catch (Exception e) {
            showError("Lỗi khi xóa thú cưng: " + e.getMessage());
        }
    }
    
    public ThuCung getThuCungById(int maThuCung) {
        try {
            return thuCungService.timThuCung(maThuCung);
        } catch (Exception e) {
            showError("Lỗi khi lấy thông tin thú cưng: " + e.getMessage());
            return null;
        }
    }
    
    private void showError(String message) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(view, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
        });
    }
}