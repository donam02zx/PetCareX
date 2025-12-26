// file: com/petcarex/model/LogDiem.java
package com.petcarex.model;

import java.time.LocalDateTime;

public class LogDiem {
    private int maLog;
    private int maKH;
    private int soDiem;
    private String loaiDiem; // "Cộng điểm", "Sử dụng điểm", "Điều chỉnh"
    private String lyDo;
    private Integer maHoaDon;
    private Integer maNhanVien;
    private LocalDateTime ngayGhiNhan;
    
    // Constructors
    public LogDiem() {
        this.ngayGhiNhan = LocalDateTime.now();
    }
    
    public LogDiem(int maKH, int soDiem, String loaiDiem, String lyDo, Integer maHoaDon) {
        this();
        this.maKH = maKH;
        this.soDiem = soDiem;
        this.loaiDiem = loaiDiem;
        this.lyDo = lyDo;
        this.maHoaDon = maHoaDon;
    }
    
    public LogDiem(int maKH, int soDiem, String loaiDiem, String lyDo, Integer maHoaDon, Integer maNhanVien) {
        this(maKH, soDiem, loaiDiem, lyDo, maHoaDon);
        this.maNhanVien = maNhanVien;
    }
    
    // Getters and Setters
    public int getMaLog() { return maLog; }
    public void setMaLog(int maLog) { this.maLog = maLog; }
    
    public int getMaKH() { return maKH; }
    public void setMaKH(int maKH) { this.maKH = maKH; }
    
    public int getSoDiem() { return soDiem; }
    public void setSoDiem(int soDiem) { this.soDiem = soDiem; }
    
    public String getLoaiDiem() { return loaiDiem; }
    public void setLoaiDiem(String loaiDiem) { this.loaiDiem = loaiDiem; }
    
    public String getLyDo() { return lyDo; }
    public void setLyDo(String lyDo) { this.lyDo = lyDo; }
    
    public Integer getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(Integer maHoaDon) { this.maHoaDon = maHoaDon; }
    
    public Integer getMaNhanVien() { return maNhanVien; }
    public void setMaNhanVien(Integer maNhanVien) { this.maNhanVien = maNhanVien; }
    
    public LocalDateTime getNgayGhiNhan() { return ngayGhiNhan; }
    public void setNgayGhiNhan(LocalDateTime ngayGhiNhan) { this.ngayGhiNhan = ngayGhiNhan; }
    
    // Helper methods
    public boolean isCongDiem() {
        return "Cộng điểm".equals(loaiDiem) && soDiem > 0;
    }
    
    public boolean isSuDungDiem() {
        return "Sử dụng điểm".equals(loaiDiem) && soDiem < 0;
    }
    
    public String getMoTaDiem() {
        String prefix = soDiem > 0 ? "+" : "";
        return prefix + soDiem + " điểm (" + loaiDiem + ")";
    }
    
    @Override
    public String toString() {
        return String.format("Log #%d - KH%d: %s %s", 
            maLog, maKH, getMoTaDiem(), lyDo);
    }
}