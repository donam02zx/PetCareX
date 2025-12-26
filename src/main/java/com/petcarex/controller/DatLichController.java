// file: com/petcarex/controller/DatLichController.java
package com.petcarex.controller;

import com.petcarex.service.LichHenService;
import com.petcarex.service.ThuCungService;
import com.petcarex.service.DichVuService;
import com.petcarex.service.NhanVienService;
import com.petcarex.model.LichHen;
import com.petcarex.model.ThuCung;
import com.petcarex.model.DichVu;
import com.petcarex.model.NhanVien;
import com.petcarex.view.DatLichView;
import javax.swing.*;
import java.time.LocalDateTime;
import java.util.List;

public class DatLichController {
    private final LichHenService lichHenService;
    private final ThuCungService thuCungService;
    private final DichVuService dichVuService;
    private final NhanVienService nhanVienService;
    private DatLichView view;
    private int currentMaKH; // Dùng cho khách hàng
    
    public DatLichController(int maKH) {
        this.lichHenService = new LichHenService();
        this.thuCungService = new ThuCungService();
        this.dichVuService = new DichVuService();
        this.nhanVienService = new NhanVienService();
        this.currentMaKH = maKH;
    }
    
    public DatLichController() {
        this.lichHenService = new LichHenService();
        this.thuCungService = new ThuCungService();
        this.dichVuService = new DichVuService();
        this.nhanVienService = new NhanVienService();
    }
    
    public JPanel getViewPanel() {
        view = new DatLichView(this, currentMaKH > 0);
        return view;
    }
    
    // Lấy danh sách thú cưng của khách hàng
    public List<ThuCung> getThuCungCuaKhachHang() {
        try {
            return thuCungService.layThuCungTheoKhachHang(currentMaKH);
        } catch (Exception e) {
            showError("Lỗi khi tải danh sách thú cưng: " + e.getMessage());
            return null;
        }
    }
    
    // Lấy danh sách dịch vụ
    public List<DichVu> getDichVu() {
        try {
            return dichVuService.layTatCaDichVu();
        } catch (Exception e) {
            showError("Lỗi khi tải danh sách dịch vụ: " + e.getMessage());
            return null;
        }
    }
    
    // Lấy danh sách bác sĩ
    public List<NhanVien> getBacSi() {
        try {
            return nhanVienService.layTatCaBacSi();
        } catch (Exception e) {
            showError("Lỗi khi tải danh sách bác sĩ: " + e.getMessage());
            return null;
        }
    }
    
    // Lấy bác sĩ khả dụng
    public List<NhanVien> getBacSiKhaDung(int maChiNhanh, LocalDateTime thoiGian) {
        try {
            return lichHenService.layBacSiKhaDung(maChiNhanh, thoiGian);
        } catch (Exception e) {
            showError("Lỗi khi tải bác sĩ khả dụng: " + e.getMessage());
            return null;
        }
    }
    
    // Lấy lịch hẹn của khách hàng
    public List<LichHen> getLichHenCuaKhachHang() {
        try {
            return lichHenService.layLichHenCuaKhachHang(currentMaKH);
        } catch (Exception e) {
            showError("Lỗi khi tải lịch hẹn: " + e.getMessage());
            return null;
        }
    }
    
    // Đặt lịch mới
    public boolean datLichMoi(LichHen lichHen) {
        try {
            LichHen newLH = lichHenService.datLich(lichHen);
            if (newLH != null) {
                view.showSuccess("Đặt lịch thành công! Mã lịch hẹn: " + newLH.getMaLichHen());
                view.refreshTable();
                return true;
            }
            return false;
        } catch (Exception e) {
            showError("Lỗi khi đặt lịch: " + e.getMessage());
            return false;
        }
    }
    
    // Hủy lịch hẹn
    public boolean huyLichHen(int maLichHen) {
        try {
            int confirm = JOptionPane.showConfirmDialog(view,
                "Bạn có chắc muốn hủy lịch hẹn này?",
                "Xác nhận hủy",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                lichHenService.huyLichHen(maLichHen);
                view.showSuccess("Hủy lịch hẹn thành công!");
                view.refreshTable();
                return true;
            }
            return false;
        } catch (Exception e) {
            showError("Lỗi khi hủy lịch hẹn: " + e.getMessage());
            return false;
        }
    }
    
    // Thêm vào DatLichController.java
    public void xoaLichHen(int maLichHen) {
        try {
            int confirm = JOptionPane.showConfirmDialog(view,
                "Bạn có chắc muốn xóa lịch hẹn này?\nLịch hẹn sẽ bị xóa vĩnh viễn.",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                lichHenService.xoaLichHen(maLichHen);
                view.showSuccess("Xóa lịch hẹn thành công!");
                view.refreshTable();
            }
        } catch (Exception e) {
            showError("Lỗi khi xóa lịch hẹn: " + e.getMessage());
        }
    }
    
    // Xác nhận lịch hẹn (cho nhân viên)
    public boolean xacNhanLichHen(int maLichHen) {
        try {
            lichHenService.xacNhanLichHen(maLichHen);
            view.showSuccess("Xác nhận lịch hẹn thành công!");
            view.refreshTable();
            return true;
        } catch (Exception e) {
            showError("Lỗi khi xác nhận lịch hẹn: " + e.getMessage());
            return false;
        }
    }
    
    private void showError(String message) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(view, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
        });
    }
}