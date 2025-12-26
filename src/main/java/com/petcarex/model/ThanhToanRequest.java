// file: com/petcarex/model/ThanhToanRequest.java
package com.petcarex.model;

public class ThanhToanRequest {
    private int maHoaDon;
    private String hinhThucThanhToan;
    private int diemSuDung;
    private String maGiaoDich;
    private String ghiChu;
    
    // Constructors
    public ThanhToanRequest() {}
    
    public ThanhToanRequest(int maHoaDon, String hinhThucThanhToan) {
        this.maHoaDon = maHoaDon;
        this.hinhThucThanhToan = hinhThucThanhToan;
        this.diemSuDung = 0;
    }
    
    // Getters and Setters
    public int getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(int maHoaDon) { this.maHoaDon = maHoaDon; }
    
    public String getHinhThucThanhToan() { return hinhThucThanhToan; }
    public void setHinhThucThanhToan(String hinhThucThanhToan) { this.hinhThucThanhToan = hinhThucThanhToan; }
    
    public int getDiemSuDung() { return diemSuDung; }
    public void setDiemSuDung(int diemSuDung) { this.diemSuDung = diemSuDung; }
    
    public String getMaGiaoDich() { return maGiaoDich; }
    public void setMaGiaoDich(String maGiaoDich) { this.maGiaoDich = maGiaoDich; }
    
    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}