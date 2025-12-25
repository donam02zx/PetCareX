package com.petcarex.service;

import com.petcarex.dao.ThuCungDAO;
import com.petcarex.model.ThuCung;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class ThuCungService {
    private final ThuCungDAO thuCungDAO;
    
    public ThuCungService() {
        this.thuCungDAO = new ThuCungDAO();
    }
    
    public ThuCung themThuCung(ThuCung tc) throws Exception {
        validateThuCung(tc);
        thuCungDAO.create(tc);
        
        // Lấy thú cưng vừa tạo (có thể tìm theo tên + chủ)
        List<ThuCung> ds = thuCungDAO.findByKhachHang(tc.getMaKH());
        for (ThuCung t : ds) {
            if (t.getTenThuCung().equals(tc.getTenThuCung())) {
                return t;
            }
        }
        return null;
    }
    
    public ThuCung timThuCung(int maThuCung) throws SQLException {
        return thuCungDAO.read(maThuCung);
    }
    
    public List<ThuCung> layTatCaThuCung() throws SQLException {
        return thuCungDAO.findAll();
    }
    
    public List<ThuCung> layThuCungTheoKhachHang(int maKH) throws SQLException {
        return thuCungDAO.findByKhachHang(maKH);
    }
    
    public List<ThuCung> timKiemThuCung(String keyword) throws SQLException {
        return thuCungDAO.searchByName(keyword);
    }
    
    public List<ThuCung> timKiemThuCungCuaKhachHang(int maKH, String keyword) throws SQLException {
        return thuCungDAO.searchByKhachHangAndName(maKH, keyword);
    }
    
    public void capNhatThuCung(ThuCung tc) throws Exception {
        validateThuCung(tc);
        thuCungDAO.update(tc);
    }
    
    public void xoaThuCung(int maThuCung) throws SQLException {
        thuCungDAO.delete(maThuCung);
    }
    
    public int tinhTuoiThuCung(LocalDate ngaySinh) {
        if (ngaySinh == null) return 0;
        
        Period period = Period.between(ngaySinh, LocalDate.now());
        int years = period.getYears();
        int months = period.getMonths();
        
        // Trả về tuổi theo năm, nếu dưới 1 năm trả theo tháng
        if (years > 0) {
            return years;
        } else {
            return months;
        }
    }
    
    private void validateThuCung(ThuCung tc) throws Exception {
        if (tc.getTenThuCung() == null || tc.getTenThuCung().trim().isEmpty()) {
            throw new Exception("Tên thú cưng không được để trống");
        }
        
        if (tc.getLoai() == null || tc.getLoai().trim().isEmpty()) {
            throw new Exception("Loại thú cưng không được để trống");
        }
        
        if (tc.getNgaySinh() == null) {
            throw new Exception("Ngày sinh không được để trống");
        }
        
        if (tc.getNgaySinh().isAfter(LocalDate.now())) {
            throw new Exception("Ngày sinh không thể ở tương lai");
        }
        
        if (tc.getGioiTinh() == null || tc.getGioiTinh().trim().isEmpty()) {
            throw new Exception("Giới tính không được để trống");
        }
        
        if (tc.getMaKH() <= 0) {
            throw new Exception("Mã khách hàng không hợp lệ");
        }
    }
}