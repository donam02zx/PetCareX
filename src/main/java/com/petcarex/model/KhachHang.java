package com.petcarex.model;

import java.time.LocalDate;

public class KhachHang {
    private int maKH;
    private String hoTen;
    private String cccd;
    private String soDienThoai;
    private String email;
    private String gioiTinh;
    private LocalDate ngaySinh;
    private String diaChi;
    private String capDoThanhVien;
    private int diemLoyalty;
    private LocalDate ngayDangKy;
    private double tongChiTieuNam;
    
    // Constructors
    public KhachHang() {}
    
    public KhachHang(String hoTen, String cccd, String soDienThoai, String email, 
                    String gioiTinh, LocalDate ngaySinh, String diaChi) {
        this.hoTen = hoTen;
        this.cccd = cccd;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.diaChi = diaChi;
        this.capDoThanhVien = "Cơ bản";
        this.diemLoyalty = 0;
        this.ngayDangKy = LocalDate.now();
        this.tongChiTieuNam = 0;
    }
    
    // Getters and Setters
    public int getMaKH() { return maKH; }
    public void setMaKH(int maKH) { this.maKH = maKH; }
    
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    
    public String getCccd() { return cccd; }
    public void setCccd(String cccd) { this.cccd = cccd; }
    
    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }
    
    public LocalDate getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(LocalDate ngaySinh) { this.ngaySinh = ngaySinh; }
    
    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }
    
    public String getCapDoThanhVien() { return capDoThanhVien; }
    public void setCapDoThanhVien(String capDoThanhVien) { this.capDoThanhVien = capDoThanhVien; }
    
    public int getDiemLoyalty() { return diemLoyalty; }
    public void setDiemLoyalty(int diemLoyalty) { this.diemLoyalty = diemLoyalty; }
    
    public LocalDate getNgayDangKy() { return ngayDangKy; }
    public void setNgayDangKy(LocalDate ngayDangKy) { this.ngayDangKy = ngayDangKy; }
    
    public double getTongChiTieuNam() { return tongChiTieuNam; }
    public void setTongChiTieuNam(double tongChiTieuNam) { this.tongChiTieuNam = tongChiTieuNam; }
    
    // Helper methods
    public int getTuoi() {
        return LocalDate.now().getYear() - ngaySinh.getYear();
    }
    
    public boolean isVIP() {
        return "VIP".equals(capDoThanhVien);
    }
    
    public boolean isAdult() {
        return getTuoi() >= 18;
    }
    
    @Override
    public String toString() {
        return hoTen + " (" + cccd + ") - " + capDoThanhVien;
    }
}