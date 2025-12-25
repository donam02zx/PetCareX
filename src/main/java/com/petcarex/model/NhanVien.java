package com.petcarex.model;

import java.time.LocalDate;

public class NhanVien {
    private int maNV;
    private String hoTen;
    private String cccd;
    private LocalDate ngaySinh;
    private String gioiTinh;
    private String soDienThoai;
    private String email;
    private LocalDate ngayVaoLam;
    private String chucVu;
    private double luongCoBan;
    private String trangThai;
    private Integer maChiNhanh;
    private String chungChiHanhNghe;
    
    // Constructors
    public NhanVien() {}
    
    public NhanVien(String hoTen, String cccd, LocalDate ngaySinh, String gioiTinh,
                   String soDienThoai, String email, LocalDate ngayVaoLam, 
                   String chucVu, double luongCoBan) {
        this.hoTen = hoTen;
        this.cccd = cccd;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.ngayVaoLam = ngayVaoLam;
        this.chucVu = chucVu;
        this.luongCoBan = luongCoBan;
        this.trangThai = "Đang làm việc";
    }
    
    // Getters and Setters
    public int getMaNV() { return maNV; }
    public void setMaNV(int maNV) { this.maNV = maNV; }
    
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    
    public String getCccd() { return cccd; }
    public void setCccd(String cccd) { this.cccd = cccd; }
    
    public LocalDate getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(LocalDate ngaySinh) { this.ngaySinh = ngaySinh; }
    
    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }
    
    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public LocalDate getNgayVaoLam() { return ngayVaoLam; }
    public void setNgayVaoLam(LocalDate ngayVaoLam) { this.ngayVaoLam = ngayVaoLam; }
    
    public String getChucVu() { return chucVu; }
    public void setChucVu(String chucVu) { this.chucVu = chucVu; }
    
    public double getLuongCoBan() { return luongCoBan; }
    public void setLuongCoBan(double luongCoBan) { this.luongCoBan = luongCoBan; }
    
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    
    public Integer getMaChiNhanh() { return maChiNhanh; }
    public void setMaChiNhanh(Integer maChiNhanh) { this.maChiNhanh = maChiNhanh; }
    
    public String getChungChiHanhNghe() { return chungChiHanhNghe; }
    public void setChungChiHanhNghe(String chungChiHanhNghe) { this.chungChiHanhNghe = chungChiHanhNghe; }
    
    // Helper methods
    public int getTuoi() {
        return LocalDate.now().getYear() - ngaySinh.getYear();
    }
    
    public boolean isBacSi() {
        return "Bác sĩ thú y".equals(chucVu);
    }
    
    public boolean isQuanLy() {
        return "Quản lý chi nhánh".equals(chucVu);
    }
    
    @Override
    public String toString() {
        return hoTen + " - " + chucVu + " (" + email + ")";
    }
}