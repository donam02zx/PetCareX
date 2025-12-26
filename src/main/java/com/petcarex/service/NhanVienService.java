// file: com/petcarex/service/NhanVienService.java
package com.petcarex.service;

import com.petcarex.dao.NhanVienDAO;
import com.petcarex.model.NhanVien;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class NhanVienService {
    private final NhanVienDAO nhanVienDAO;
    
    public NhanVienService() {
        this.nhanVienDAO = new NhanVienDAO();
    }
    
    public NhanVien themNhanVien(NhanVien nv) throws Exception {
        validateNhanVien(nv);
        
        // Check if CCCD already exists
        if (nhanVienDAO.findByCCCD(nv.getCccd()) != null) {
            throw new Exception("CCCD đã tồn tại trong hệ thống");
        }
        
        nhanVienDAO.create(nv);
        
        // Get the generated ID
        NhanVien newNV = nhanVienDAO.findByCCCD(nv.getCccd());
        return newNV;
    }
    
    public NhanVien timNhanVien(int maNV) throws SQLException {
        return nhanVienDAO.read(maNV);
    }
    
    public NhanVien timNhanVienTheoEmail(String email) throws SQLException {
        return nhanVienDAO.findByEmail(email);
    }
    
    public List<NhanVien> layTatCaNhanVien() throws SQLException {
        return nhanVienDAO.findAll();
    }
    
    public List<NhanVien> layNhanVienDangLamViec() throws SQLException {
        return nhanVienDAO.findDangLamViec();
    }
    
    public List<NhanVien> layNhanVienTheoChucVu(String chucVu) throws SQLException {
        return nhanVienDAO.findByChucVu(chucVu);
    }
    
    public List<NhanVien> layTatCaBacSi() throws SQLException {
        return nhanVienDAO.findByChucVu("Bác sĩ thú y");
    }
    
    public List<NhanVien> layNhanVienTheoChiNhanh(int maChiNhanh) throws SQLException {
        return nhanVienDAO.findByChiNhanh(maChiNhanh);
    }
    
    public void capNhatNhanVien(NhanVien nv) throws Exception {
        validateNhanVien(nv);
        nhanVienDAO.update(nv);
    }
    
    public void xoaNhanVien(int maNV) throws SQLException {
        nhanVienDAO.delete(maNV);
    }
    
    public void thayDoiTrangThai(int maNV, String trangThai) throws SQLException {
        NhanVien nv = nhanVienDAO.read(maNV);
        if (nv != null) {
            nv.setTrangThai(trangThai);
            nhanVienDAO.update(nv);
        }
    }
    
    public void chuyenChiNhanh(int maNV, int maChiNhanhMoi) throws SQLException {
        NhanVien nv = nhanVienDAO.read(maNV);
        if (nv != null) {
            nv.setMaChiNhanh(maChiNhanhMoi);
            nhanVienDAO.update(nv);
        }
    }
    
    private void validateNhanVien(NhanVien nv) throws Exception {
        // Check required fields
        if (nv.getHoTen() == null || nv.getHoTen().trim().isEmpty()) {
            throw new Exception("Họ tên không được để trống");
        }
        
        if (nv.getCccd() == null || nv.getCccd().length() != 12) {
            throw new Exception("CCCD phải có 12 số");
        }
        
        if (nv.getSoDienThoai() == null || !nv.getSoDienThoai().matches("^0[0-9]{9}$")) {
            throw new Exception("Số điện thoại không hợp lệ");
        }
        
        // Check age >= 18
        if (nv.getNgaySinh() != null) {
            int age = Period.between(nv.getNgaySinh(), LocalDate.now()).getYears();
            if (age < 18) {
                throw new Exception("Nhân viên phải đủ 18 tuổi");
            }
        }
        
        // Check ngayVaoLam
        if (nv.getNgayVaoLam() != null && nv.getNgayVaoLam().isAfter(LocalDate.now())) {
            throw new Exception("Ngày vào làm không được trong tương lai");
        }
        
        // Check luongCoBan >= minimum wage
        if (nv.getLuongCoBan() < 4680000) { // Minimum wage in Vietnam
            throw new Exception("Lương cơ bản phải tối thiểu 4,680,000 VND");
        }
    }
}