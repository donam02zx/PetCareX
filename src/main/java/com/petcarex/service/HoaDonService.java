// file: com/petcarex/service/HoaDonService.java
package com.petcarex.service;

import com.petcarex.dao.HoaDonDAO;
import com.petcarex.model.HoaDon;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class HoaDonService {
    private final HoaDonDAO hoaDonDAO;
    
    public HoaDonService() {
        this.hoaDonDAO = new HoaDonDAO();
    }
    
    public HoaDon themHoaDon(HoaDon hoaDon) throws SQLException {
        hoaDonDAO.create(hoaDon);
        return hoaDon;
    }
    
    public HoaDon timHoaDon(int maHoaDon) throws SQLException {
        return hoaDonDAO.read(maHoaDon);
    }
    
    public List<HoaDon> layTatCaHoaDon() throws SQLException {
        return hoaDonDAO.findAll();
    }
    
    public List<HoaDon> layHoaDonTheoTrangThai(String trangThai) throws SQLException {
        return hoaDonDAO.findByTrangThai(trangThai);
    }
    
    public List<HoaDon> layHoaDonTheoKhachHang(int maKH) throws SQLException {
        return hoaDonDAO.findByKhachHang(maKH);
    }
    
    public List<HoaDon> layHoaDonTheoNhanVien(int maNV) throws SQLException {
        return hoaDonDAO.findByNhanVien(maNV);
    }
    
    public List<HoaDon> layHoaDonTheoKhoangThoiGian(LocalDateTime from, LocalDateTime to) throws SQLException {
        return hoaDonDAO.findByDateRange(from, to);
    }
    
    public void capNhatHoaDon(HoaDon hoaDon) throws SQLException {
        hoaDonDAO.update(hoaDon);
    }
    
    public void thanhToanHoaDon(int maHoaDon, String hinhThucThanhToan) throws SQLException {
        hoaDonDAO.thanhToanHoaDon(maHoaDon, hinhThucThanhToan);
    }
    
    public void huyHoaDon(int maHoaDon) throws SQLException {
        hoaDonDAO.huyHoaDon(maHoaDon);
    }
    
    public double tinhTongDoanhThu(LocalDateTime from, LocalDateTime to) throws SQLException {
        return hoaDonDAO.getTongDoanhThu(from, to);
    }
}