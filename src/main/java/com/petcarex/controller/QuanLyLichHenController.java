// file: com/petcarex/controller/QuanLyLichHenController.java
package com.petcarex.controller;

import com.petcarex.service.LichHenService;
import com.petcarex.service.ThuCungService;
import com.petcarex.service.DichVuService;
import com.petcarex.service.NhanVienService;
import com.petcarex.model.LichHen;
import com.petcarex.model.ThuCung;
import com.petcarex.model.DichVu;
import com.petcarex.model.HoaDon;
import com.petcarex.model.NhanVien;
import com.petcarex.view.QuanLyLichHenView;
import javax.swing.*;
import java.time.LocalDateTime;
import java.util.*;

public class QuanLyLichHenController {
    private final LichHenService lichHenService;
    private final ThuCungService thuCungService;
    private final DichVuService dichVuService;
    private final NhanVienService nhanVienService;
    private QuanLyLichHenView view;
    
    public QuanLyLichHenController() {
        this.lichHenService = new LichHenService();
        this.thuCungService = new ThuCungService();
        this.dichVuService = new DichVuService();
        this.nhanVienService = new NhanVienService();
    }
    
    public JPanel getViewPanel() {
        view = new QuanLyLichHenView(this);
        return view;
    }
    
    // Lấy tất cả lịch hẹn
    public List<LichHen> getAllLichHen() {
        try {
            return lichHenService.layLichHenTuNgayDenNgay(
                LocalDateTime.now().minusMonths(1), 
                LocalDateTime.now().plusMonths(3)
            );
        } catch (Exception e) {
            showError("Lỗi khi tải danh sách lịch hẹn: " + e.getMessage());
            return null;
        }
    }
    
    // Lọc lịch hẹn theo điều kiện
    public List<LichHen> filterLichHen(LocalDateTime from, LocalDateTime to, String trangThai, Integer maChiNhanh) {
        try {
            List<LichHen> allLichHen = lichHenService.layLichHenTuNgayDenNgay(from, to);
            
            // Lọc theo điều kiện
            List<LichHen> filteredList = new ArrayList<>();
            for (LichHen lh : allLichHen) {
                boolean match = true;
                
                // Lọc theo trạng thái
                if (trangThai != null && !trangThai.isEmpty() && !lh.getTrangThai().equals(trangThai)) {
                    match = false;
                }
                
                // Lọc theo chi nhánh
                if (maChiNhanh != null && lh.getMaChiNhanh() != maChiNhanh) {
                    match = false;
                }
                
                if (match) {
                    filteredList.add(lh);
                }
            }
            
            return filteredList;
        } catch (Exception e) {
            showError("Lỗi khi lọc lịch hẹn: " + e.getMessage());
            return null;
        }
    }
    
    // Lấy danh sách chi nhánh (giả định có 2 chi nhánh)
    public List<String> getDanhSachChiNhanh() {
        return Arrays.asList("PetCareX Quận 1", "PetCareX Quận 7");
    }
    
    // Lấy danh sách trạng thái
    public List<String> getDanhSachTrangThai() {
        return Arrays.asList("Chờ xác nhận", "Đã xác nhận", "Đã hoàn thành", "Đã hủy");
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
    
    // Xác nhận lịch hẹn
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
    
    // Đánh dấu hoàn thành
//    public boolean hoanThanhLichHen(int maLichHen) {
//        try {
//            lichHenService.hoanThanhLichHen(maLichHen);
//            view.showSuccess("Đánh dấu hoàn thành thành công!");
//            view.refreshTable();
//            return true;
//        } catch (Exception e) {
//            showError("Lỗi khi đánh dấu hoàn thành: " + e.getMessage());
//            return false;
//        }
//    }
    public boolean hoanThanhLichHen(int maLichHen) {
        try {
            // Sử dụng method mới để vừa hoàn thành lịch vừa tạo hóa đơn
            HoaDon hoaDon = lichHenService.hoanThanhLichHenVaTaoHoaDon(maLichHen);
            
            if (hoaDon != null) {
                view.showSuccess("Đánh dấu hoàn thành thành công!\n" +
                               "Đã tạo hóa đơn #" + hoaDon.getMaHoaDon() + 
                               "\nTổng tiền: " + String.format("%,.0f", hoaDon.getTongTien()) + " VND");
                view.refreshTable();
                return true;
            } else {
                view.showError("Không thể tạo hóa đơn từ lịch hẹn");
                return false;
            }
        } catch (Exception e) {
            showError("Lỗi khi đánh dấu hoàn thành: " + e.getMessage());
            return false;
        }
    }
    
    // Đặt lịch hộ khách hàng
    public boolean datLichChoKhach(LichHen lichHen) {
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
    
    // Lấy chi tiết lịch hẹn
    public LichHen getChiTietLichHen(int maLichHen) {
        try {
            return lichHenService.timLichHen(maLichHen);
        } catch (Exception e) {
            showError("Lỗi khi lấy chi tiết lịch hẹn: " + e.getMessage());
            return null;
        }
    }
    
    // Lấy danh sách thú cưng
    public List<ThuCung> getAllThuCung() {
        try {
            return thuCungService.layTatCaThuCung();
        } catch (Exception e) {
            showError("Lỗi khi tải danh sách thú cưng: " + e.getMessage());
            return null;
        }
    }
    
    // Lấy danh sách dịch vụ
    public List<DichVu> getAllDichVu() {
        try {
            return dichVuService.layTatCaDichVu();
        } catch (Exception e) {
            showError("Lỗi khi tải danh sách dịch vụ: " + e.getMessage());
            return null;
        }
    }
    
    // Lấy danh sách bác sĩ
    public List<NhanVien> getAllBacSi() {
        try {
            return nhanVienService.layTatCaBacSi();
        } catch (Exception e) {
            showError("Lỗi khi tải danh sách bác sĩ: " + e.getMessage());
            return null;
        }
    }
    
    // Xóa lịch hẹn
    public boolean xoaLichHen(int maLichHen) {
        try {
            lichHenService.xoaLichHen(maLichHen);
            view.showSuccess("Xóa lịch hẹn thành công!");
            view.refreshTable();
            return true;
        } catch (Exception e) {
            showError("Lỗi khi xóa lịch hẹn: " + e.getMessage());
            return false;
        }
    }
    
    private void showError(String message) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(view, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
        });
    }
}