package com.petcarex.service;

import com.petcarex.dao.KhachHangDAO;
import com.petcarex.model.KhachHang;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class KhachHangService {
    private final KhachHangDAO khachHangDAO;
    
    public KhachHangService() {
        this.khachHangDAO = new KhachHangDAO();
    }
    
    public KhachHang themKhachHang(KhachHang kh) throws Exception {
        // Validate data
        validateKhachHang(kh);
        
        // Check if CCCD or phone already exists
        if (khachHangDAO.findByCCCD(kh.getCccd()) != null) {
            throw new Exception("CCCD đã tồn tại trong hệ thống");
        }
        
        if (khachHangDAO.findByPhone(kh.getSoDienThoai()) != null) {
            throw new Exception("Số điện thoại đã tồn tại trong hệ thống");
        }
        
        // Insert to database
        khachHangDAO.create(kh);
        
        // Get the generated ID
        KhachHang newKH = khachHangDAO.findByCCCD(kh.getCccd());
        return newKH;
    }
    
    public KhachHang timKhachHang(int maKH) throws SQLException {
        return khachHangDAO.read(maKH);
    }
    
    public KhachHang timKhachHangTheoSDT(String sdt) throws SQLException {
        return khachHangDAO.findByPhone(sdt);
    }
    
    public List<KhachHang> timKiemTheoTen(String ten) throws SQLException {
        return khachHangDAO.searchByName(ten);
    }
    
    public List<KhachHang> layTatCaKhachHang() throws SQLException {
        return khachHangDAO.findAll();
    }
    
    public void capNhatKhachHang(KhachHang kh) throws Exception {
        validateKhachHang(kh);
        khachHangDAO.update(kh);
    }
    
    public void xoaKhachHang(int maKH) throws SQLException {
        khachHangDAO.delete(maKH);
    }
    
    public void congDiemLoyalty(int maKH, int diem) throws SQLException {
        KhachHang kh = khachHangDAO.read(maKH);
        if (kh != null) {
            kh.setDiemLoyalty(kh.getDiemLoyalty() + diem);
            khachHangDAO.update(kh);
        }
    }
    
    public void truDiemLoyalty(int maKH, int diem) throws Exception {
        KhachHang kh = khachHangDAO.read(maKH);
        if (kh == null) {
            throw new Exception("Khách hàng không tồn tại");
        }
        
        if (kh.getDiemLoyalty() < diem) {
            throw new Exception("Không đủ điểm loyalty");
        }
        
        kh.setDiemLoyalty(kh.getDiemLoyalty() - diem);
        khachHangDAO.update(kh);
    }
    
    public void capNhatTongChiTieu(int maKH, double soTien) throws SQLException {
        KhachHang kh = khachHangDAO.read(maKH);
        if (kh != null) {
            kh.setTongChiTieuNam(kh.getTongChiTieuNam() + soTien);
            
            // Auto update member level
            String newLevel = tinhCapDoThanhVien(kh.getTongChiTieuNam());
            kh.setCapDoThanhVien(newLevel);
            
            khachHangDAO.update(kh);
        }
    }
    
    private String tinhCapDoThanhVien(double tongChiTieu) {
        if (tongChiTieu >= 12000000) {
            return "VIP";
        } else if (tongChiTieu >= 5000000) {
            return "Thân thiết";
        } else {
            return "Cơ bản";
        }
    }
    
    private void validateKhachHang(KhachHang kh) throws Exception {
        // Check required fields
        if (kh.getHoTen() == null || kh.getHoTen().trim().isEmpty()) {
            throw new Exception("Họ tên không được để trống");
        }
        
        if (kh.getCccd() == null || kh.getCccd().length() != 12) {
            throw new Exception("CCCD phải có 12 số");
        }
        
        if (kh.getSoDienThoai() == null || !kh.getSoDienThoai().matches("^0[0-9]{9}$")) {
            throw new Exception("Số điện thoại không hợp lệ");
        }
        
        if (kh.getEmail() == null || !kh.getEmail().contains("@")) {
            throw new Exception("Email không hợp lệ");
        }
        
        // Check age >= 18
        if (kh.getNgaySinh() != null) {
            int age = Period.between(kh.getNgaySinh(), LocalDate.now()).getYears();
            if (age < 18) {
                throw new Exception("Khách hàng phải đủ 18 tuổi");
            }
        }
    }
}