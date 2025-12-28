package com.petcarex.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PhieuKham {
    private int maPhieuKham;
    private int maThuCung;
    private int maBacSi;
    private int maChiNhanh;
    private LocalDateTime ngayKham;
    private String trieuChung;
    private String chuanDoan;
    private String toaThuoc;
    private LocalDate ngayHenTaiKham;
    private String ghiChu;

    // Getters & Setters
    public int getMaPhieuKham() { return maPhieuKham; }
    public void setMaPhieuKham(int maPhieuKham) { this.maPhieuKham = maPhieuKham; }

    public int getMaThuCung() { return maThuCung; }
    public void setMaThuCung(int maThuCung) { this.maThuCung = maThuCung; }

    public int getMaBacSi() { return maBacSi; }
    public void setMaBacSi(int maBacSi) { this.maBacSi = maBacSi; }

    public int getMaChiNhanh() { return maChiNhanh; }
    public void setMaChiNhanh(int maChiNhanh) { this.maChiNhanh = maChiNhanh; }

    public LocalDateTime getNgayKham() { return ngayKham; }
    public void setNgayKham(LocalDateTime ngayKham) { this.ngayKham = ngayKham; }

    public String getTrieuChung() { return trieuChung; }
    public void setTrieuChung(String trieuChung) { this.trieuChung = trieuChung; }

    public String getChuanDoan() { return chuanDoan; }
    public void setChuanDoan(String chuanDoan) { this.chuanDoan = chuanDoan; }

    public String getToaThuoc() { return toaThuoc; }
    public void setToaThuoc(String toaThuoc) { this.toaThuoc = toaThuoc; }

    public LocalDate getNgayHenTaiKham() { return ngayHenTaiKham; }
    public void setNgayHenTaiKham(LocalDate ngayHenTaiKham) { this.ngayHenTaiKham = ngayHenTaiKham; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}
