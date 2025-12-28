// file: com/petcarex/model/LichHen.java
package com.petcarex.model;

import java.time.LocalDateTime;

public class LichHen {
    private int maLichHen;
    private int maThuCung;
    private String loaiLichHen; // "Khám bệnh", "Tiêm phòng", "Grooming"
    private LocalDateTime thoiGian;
    private int maChiNhanh;
    private Integer maBacSi; // null nếu không cần bác sĩ
    private Integer maDichVu; // null nếu không chọn dịch vụ cụ thể
    private String trangThai; // "Chờ xác nhận", "Đã xác nhận", "Đã hoàn thành", "Đã hủy"
    private String ghiChu;
    private LocalDateTime ngayTao;
    private Integer maHoaDon; // Thêm field này
    private String trangThaiHoaDon; // Thêm field này
    private Double tongTienHoaDon;  
    
    // Thông tin thêm (cho hiển thị)
    private String tenThuCung;
    private String tenChiNhanh;
    private String tenBacSi;
    private String tenDichVu;
    private String tenChu;
    
    // Constructors
    public LichHen() {
        this.trangThai = "Chờ xác nhận";
        this.ngayTao = LocalDateTime.now();
    }
    
    public LichHen(int maThuCung, String loaiLichHen, LocalDateTime thoiGian, int maChiNhanh) {
        this();
        this.maThuCung = maThuCung;
        this.loaiLichHen = loaiLichHen;
        this.thoiGian = thoiGian;
        this.maChiNhanh = maChiNhanh;
    }
    
    // Getters and Setters
    public Integer getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(Integer maHoaDon) { this.maHoaDon = maHoaDon; }
    
    public String getTrangThaiHoaDon() { return trangThaiHoaDon; }
    public void setTrangThaiHoaDon(String trangThaiHoaDon) { this.trangThaiHoaDon = trangThaiHoaDon; }
    
    public Double getTongTienHoaDon() { return tongTienHoaDon; }
    public void setTongTienHoaDon(Double tongTienHoaDon) { this.tongTienHoaDon = tongTienHoaDon; }
    
    public String getTenChu() { return tenChu; }
    public void setTenChu(String tenChu) { this.tenChu = tenChu; }
    
    public int getMaLichHen() { return maLichHen; }
    public void setMaLichHen(int maLichHen) { this.maLichHen = maLichHen; }
    
    public int getMaThuCung() { return maThuCung; }
    public void setMaThuCung(int maThuCung) { this.maThuCung = maThuCung; }
    
    public String getLoaiLichHen() { return loaiLichHen; }
    public void setLoaiLichHen(String loaiLichHen) { this.loaiLichHen = loaiLichHen; }
    
    public LocalDateTime getThoiGian() { return thoiGian; }
    public void setThoiGian(LocalDateTime thoiGian) { this.thoiGian = thoiGian; }
    
    public int getMaChiNhanh() { return maChiNhanh; }
    public void setMaChiNhanh(int maChiNhanh) { this.maChiNhanh = maChiNhanh; }
    
    public Integer getMaBacSi() { return maBacSi; }
    public void setMaBacSi(Integer maBacSi) { this.maBacSi = maBacSi; }
    
    public Integer getMaDichVu() { return maDichVu; }
    public void setMaDichVu(Integer maDichVu) { this.maDichVu = maDichVu; }
    
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    
    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
    
    public LocalDateTime getNgayTao() { return ngayTao; }
    public void setNgayTao(LocalDateTime ngayTao) { this.ngayTao = ngayTao; }
    
    // Thông tin thêm
    public String getTenThuCung() { return tenThuCung; }
    public void setTenThuCung(String tenThuCung) { this.tenThuCung = tenThuCung; }
    
    public String getTenChiNhanh() { return tenChiNhanh; }
    public void setTenChiNhanh(String tenChiNhanh) { this.tenChiNhanh = tenChiNhanh; }
    
    public String getTenBacSi() { return tenBacSi; }
    public void setTenBacSi(String tenBacSi) { this.tenBacSi = tenBacSi; }
    
    public String getTenDichVu() { return tenDichVu; }
    public void setTenDichVu(String tenDichVu) { this.tenDichVu = tenDichVu; }
    
    // Helper methods
    public boolean isConfirmed() {
        return "Đã xác nhận".equals(trangThai);
    }
    
    public boolean isCancelled() {
        return "Đã hủy".equals(trangThai);
    }
    
    public boolean isCompleted() {
        return "Đã hoàn thành".equals(trangThai);
    }
    
    public String getThoiGianDisplay() {
        return thoiGian.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
    
    @Override
    public String toString() {
        return loaiLichHen + " - " + getThoiGianDisplay() + " - " + trangThai;
    }
}