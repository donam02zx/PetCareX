package com.petcarex.model;

import java.time.LocalDate;

public class ThuCung {
    private int maThuCung;
    private String tenThuCung;
    private String loai;
    private String giong;
    private LocalDate ngaySinh;
    private String gioiTinh;
    private String tinhTrangSucKhoe;
    private int maKH;
    
    // Additional fields for display
    private String tenChu;
    
    // Constructors
    public ThuCung() {}
    
    public ThuCung(String tenThuCung, String loai, LocalDate ngaySinh, String gioiTinh, int maKH) {
        this.tenThuCung = tenThuCung;
        this.loai = loai;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.maKH = maKH;
        this.tinhTrangSucKhoe = "Khỏe mạnh";
    }
    
    // Getters and Setters
    public int getMaThuCung() { return maThuCung; }
    public void setMaThuCung(int maThuCung) { this.maThuCung = maThuCung; }
    
    public String getTenThuCung() { return tenThuCung; }
    public void setTenThuCung(String tenThuCung) { this.tenThuCung = tenThuCung; }
    
    public String getLoai() { return loai; }
    public void setLoai(String loai) { this.loai = loai; }
    
    public String getGiong() { return giong; }
    public void setGiong(String giong) { this.giong = giong; }
    
    public LocalDate getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(LocalDate ngaySinh) { this.ngaySinh = ngaySinh; }
    
    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }
    
    public String getTinhTrangSucKhoe() { return tinhTrangSucKhoe; }
    public void setTinhTrangSucKhoe(String tinhTrangSucKhoe) { this.tinhTrangSucKhoe = tinhTrangSucKhoe; }
    
    public int getMaKH() { return maKH; }
    public void setMaKH(int maKH) { this.maKH = maKH; }
    
    public String getTenChu() { return tenChu; }
    public void setTenChu(String tenChu) { this.tenChu = tenChu; }
    
    // Helper methods
    public int getTuoi() {
        if (ngaySinh == null) return 0;
        
        LocalDate now = LocalDate.now();
        int years = now.getYear() - ngaySinh.getYear();
        int months = now.getMonthValue() - ngaySinh.getMonthValue();
        
        if (months < 0) {
            years--;
            months += 12;
        }
        
        return years > 0 ? years : months;
    }
    
    public String getTuoiDisplay() {
        int tuoi = getTuoi();
        if (tuoi == 0) return "Sơ sinh";
        
        if (ngaySinh == null) return "Không xác định";
        
        LocalDate now = LocalDate.now();
        int years = now.getYear() - ngaySinh.getYear();
        
        if (years > 0) {
            return years + " tuổi";
        } else {
            int months = now.getMonthValue() - ngaySinh.getMonthValue();
            if (months < 0) months += 12;
            return months + " tháng";
        }
    }
    
    public boolean isConNon() {
        return getTuoi() < 1; // Dưới 1 tuổi
    }
    
    @Override
    public String toString() {
        return tenThuCung + " (" + loai + ")";
    }
}