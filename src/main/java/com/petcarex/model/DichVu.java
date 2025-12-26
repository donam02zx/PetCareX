// file: com/petcarex/model/DichVu.java
package com.petcarex.model;

public class DichVu {
    private int maDichVu;
    private String tenDichVu;
    private String loaiDichVu;
    private String moTa;
    private double giaCoBan;
    private int thoiGianThucHien; // phút
    private boolean active;
    
    // Constructors
    public DichVu() {}
    
    public DichVu(String tenDichVu, String loaiDichVu, double giaCoBan, int thoiGianThucHien) {
        this.tenDichVu = tenDichVu;
        this.loaiDichVu = loaiDichVu;
        this.giaCoBan = giaCoBan;
        this.thoiGianThucHien = thoiGianThucHien;
        this.active = true;
    }
    
    // Getters and Setters
    public int getMaDichVu() { return maDichVu; }
    public void setMaDichVu(int maDichVu) { this.maDichVu = maDichVu; }
    
    public String getTenDichVu() { return tenDichVu; }
    public void setTenDichVu(String tenDichVu) { this.tenDichVu = tenDichVu; }
    
    public String getLoaiDichVu() { return loaiDichVu; }
    public void setLoaiDichVu(String loaiDichVu) { this.loaiDichVu = loaiDichVu; }
    
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    
    public double getGiaCoBan() { return giaCoBan; }
    public void setGiaCoBan(double giaCoBan) { this.giaCoBan = giaCoBan; }
    
    public int getThoiGianThucHien() { return thoiGianThucHien; }
    public void setThoiGianThucHien(int thoiGianThucHien) { this.thoiGianThucHien = thoiGianThucHien; }
    
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    
    // Helper methods
    public String getThoiGianThucHienDisplay() {
        int gio = thoiGianThucHien / 60;
        int phut = thoiGianThucHien % 60;
        
        if (gio > 0) {
            return gio + " giờ " + phut + " phút";
        } else {
            return phut + " phút";
        }
    }
    
    public String getGiaDisplay() {
        return String.format("%,.0f VND", giaCoBan);
    }
    
    @Override
    public String toString() {
        return tenDichVu + " - " + getGiaDisplay();
    }
}