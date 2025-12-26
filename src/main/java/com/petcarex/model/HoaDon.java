// file: com/petcarex/model/HoaDon.java
package com.petcarex.model;

import java.time.LocalDateTime;

public class HoaDon {
    private int maHoaDon;
    private int maKH;
    private int maNV;
    private int maChiNhanh;
    private LocalDateTime ngayLap;
    private double tongTienHang;
    private double chietKhauThanhVien;
    private double chietKhauKhac;
    private double tongTien;
    private String hinhThucThanhToan;
    private int diemSuDung;
    private int diemCong;
    private String trangThai;
    
 // Additional fields for display
    private String tenKhachHang;
    private String tenNhanVien;
    private String tenChiNhanh;
    
    // Constructors
    public HoaDon() {
        this.ngayLap = LocalDateTime.now();
        this.trangThai = "Chưa thanh toán";
        this.diemSuDung = 0;
        this.diemCong = 0;
        this.chietKhauThanhVien = 0;
        this.chietKhauKhac = 0;
    }
    
    public HoaDon(int maKH, int maNV, int maChiNhanh, double tongTien, String hinhThucThanhToan) {
        this();
        this.maKH = maKH;
        this.maNV = maNV;
        this.maChiNhanh = maChiNhanh;
        this.tongTien = tongTien;
        this.tongTienHang = tongTien;
        this.hinhThucThanhToan = hinhThucThanhToan;
    }
    
    // Getters and Setters
    
 // Getters and Setters for additional fields
    public String getTenKhachHang() { return tenKhachHang; }
    public void setTenKhachHang(String tenKhachHang) { this.tenKhachHang = tenKhachHang; }
    
    public String getTenNhanVien() { return tenNhanVien; }
    public void setTenNhanVien(String tenNhanVien) { this.tenNhanVien = tenNhanVien; }
    
    public String getTenChiNhanh() { return tenChiNhanh; }
    public void setTenChiNhanh(String tenChiNhanh) { this.tenChiNhanh = tenChiNhanh; }
    
    public int getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(int maHoaDon) { this.maHoaDon = maHoaDon; }
    
    public int getMaKH() { return maKH; }
    public void setMaKH(int maKH) { this.maKH = maKH; }
    
    public int getMaNV() { return maNV; }
    public void setMaNV(int maNV) { this.maNV = maNV; }
    
    public int getMaChiNhanh() { return maChiNhanh; }
    public void setMaChiNhanh(int maChiNhanh) { this.maChiNhanh = maChiNhanh; }
    
    public LocalDateTime getNgayLap() { return ngayLap; }
    public void setNgayLap(LocalDateTime ngayLap) { this.ngayLap = ngayLap; }
    
    public double getTongTienHang() { return tongTienHang; }
    public void setTongTienHang(double tongTienHang) { this.tongTienHang = tongTienHang; }
    
    public double getChietKhauThanhVien() { return chietKhauThanhVien; }
    public void setChietKhauThanhVien(double chietKhauThanhVien) { this.chietKhauThanhVien = chietKhauThanhVien; }
    
    public double getChietKhauKhac() { return chietKhauKhac; }
    public void setChietKhauKhac(double chietKhauKhac) { this.chietKhauKhac = chietKhauKhac; }
    
    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }
    
    public String getHinhThucThanhToan() { return hinhThucThanhToan; }
    public void setHinhThucThanhToan(String hinhThucThanhToan) { this.hinhThucThanhToan = hinhThucThanhToan; }
    
    public int getDiemSuDung() { return diemSuDung; }
    public void setDiemSuDung(int diemSuDung) { this.diemSuDung = diemSuDung; }
    
    public int getDiemCong() { return diemCong; }
    public void setDiemCong(int diemCong) { this.diemCong = diemCong; }
    
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    
    // Helper methods
    public double tinhTongTienSauChietKhau() {
        double tienSauChietKhauThanhVien = tongTienHang * (1 - chietKhauThanhVien / 100);
        double tienCuoiCung = tienSauChietKhauThanhVien - chietKhauKhac;
        
        // Tính điểm có thể sử dụng (1 điểm = 1,000 VND)
        double tienSauDiem = tienCuoiCung - (diemSuDung * 1000);
        
        return Math.max(tienSauDiem, 0);
    }
    
    public int tinhDiemCongDuKien() {
        return (int) Math.floor(tongTien / 10000);
    }
    
    @Override
    public String toString() {
        return String.format("HĐ%06d - %,.0f VND - %s", maHoaDon, tongTien, trangThai);
    }
}