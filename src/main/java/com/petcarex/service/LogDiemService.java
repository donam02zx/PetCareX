// file: com/petcarex/service/LogDiemService.java
package com.petcarex.service;

import com.petcarex.dao.LogDiemDAO;
import com.petcarex.model.LogDiem;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class LogDiemService {
    private final LogDiemDAO logDiemDAO;
    
    public LogDiemService() {
        this.logDiemDAO = new LogDiemDAO();
    }
    
    public void ghiLogCongDiem(int maKH, int soDiem, String lyDo, Integer maHoaDon) throws SQLException {
        LogDiem log = new LogDiem(maKH, soDiem, "Cộng điểm", lyDo, maHoaDon);
        logDiemDAO.create(log);
    }
    
    public void ghiLogSuDungDiem(int maKH, int soDiem, String lyDo, Integer maHoaDon, Integer maNhanVien) throws SQLException {
        LogDiem log = new LogDiem(maKH, -soDiem, "Sử dụng điểm", lyDo, maHoaDon, maNhanVien);
        logDiemDAO.create(log);
    }
    
    public void ghiLogDieuChinhDiem(int maKH, int soDiem, String lyDo, Integer maNhanVien) throws SQLException {
        String loaiDiem = soDiem > 0 ? "Cộng điểm" : "Sử dụng điểm";
        LogDiem log = new LogDiem(maKH, soDiem, loaiDiem, lyDo, null, maNhanVien);
        logDiemDAO.create(log);
    }
    
    public List<LogDiem> layLogTheoKhachHang(int maKH) throws SQLException {
        return logDiemDAO.findByKhachHang(maKH);
    }
    
    public List<LogDiem> layLogTheoHoaDon(int maHoaDon) throws SQLException {
        return logDiemDAO.findByHoaDon(maHoaDon);
    }
    
    public List<LogDiem> layLogTheoKhoangThoiGian(LocalDateTime from, LocalDateTime to) throws SQLException {
        return logDiemDAO.findByDateRange(from, to);
    }
    
    public int tinhTongDiemHienTai(int maKH) throws SQLException {
        return logDiemDAO.getTongDiemKhachHang(maKH);
    }
    
    public int tinhDiemSuDungThang(int maKH) throws SQLException {
        LocalDateTime now = LocalDateTime.now();
        return logDiemDAO.getDiemSuDungTrongThang(maKH, now.getMonthValue(), now.getYear());
    }
    
    public int tinhDiemCongThang(int maKH) throws SQLException {
        LocalDateTime now = LocalDateTime.now();
        return logDiemDAO.getDiemCongTrongThang(maKH, now.getMonthValue(), now.getYear());
    }
    
    public List<LogDiem> layTatCaLog() throws SQLException {
        return logDiemDAO.findAll();
    }
    
    public LogDiem timLog(int maLog) throws SQLException {
        return logDiemDAO.read(maLog);
    }
}