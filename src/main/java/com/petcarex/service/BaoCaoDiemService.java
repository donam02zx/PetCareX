// file: com/petcarex/service/BaoCaoDiemService.java
package com.petcarex.service;

import com.petcarex.dao.LogDiemDAO;
import com.petcarex.dao.KhachHangDAO;
import com.petcarex.model.KhachHang;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class BaoCaoDiemService {
    private final LogDiemDAO logDiemDAO;
    private final KhachHangDAO khachHangDAO;
    
    public BaoCaoDiemService() {
        this.logDiemDAO = new LogDiemDAO();
        this.khachHangDAO = new KhachHangDAO();
    }
    
    public Map<String, Object> thongKeDiemTheoThang(int year, int month) throws SQLException {
        Map<String, Object> result = new HashMap<>();
        
        LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0, 0);
        LocalDateTime end = start.plusMonths(1).minusSeconds(1);
        
        // Lấy tất cả log trong tháng
        List<com.petcarex.model.LogDiem> logs = logDiemDAO.findByDateRange(start, end);
        
        int tongDiemCong = 0;
        int tongDiemSuDung = 0;
        int soKhachHang = 0;
        Map<Integer, Integer> diemTheoKhachHang = new HashMap<>();
        
        for (com.petcarex.model.LogDiem log : logs) {
            if ("Cộng điểm".equals(log.getLoaiDiem())) {
                tongDiemCong += log.getSoDiem();
            } else if ("Sử dụng điểm".equals(log.getLoaiDiem())) {
                tongDiemSuDung += Math.abs(log.getSoDiem());
            }
            
            // Đếm số khách hàng
            diemTheoKhachHang.put(log.getMaKH(), 
                diemTheoKhachHang.getOrDefault(log.getMaKH(), 0) + log.getSoDiem());
        }
        
        soKhachHang = diemTheoKhachHang.size();
        
        result.put("tongDiemCong", tongDiemCong);
        result.put("tongDiemSuDung", tongDiemSuDung);
        result.put("soKhachHang", soKhachHang);
        result.put("diemTheoKhachHang", diemTheoKhachHang);
        
        return result;
    }
    
    public List<Map<String, Object>> topKhachHangTichDiem(int limit) throws SQLException {
        List<Map<String, Object>> result = new ArrayList<>();
        
        // Lấy tất cả khách hàng có điểm
        List<KhachHang> allKhachHang = khachHangDAO.findAll();
        
        // Sắp xếp theo điểm giảm dần
        allKhachHang.sort((a, b) -> Integer.compare(b.getDiemLoyalty(), a.getDiemLoyalty()));
        
        // Lấy top N
        int count = Math.min(limit, allKhachHang.size());
        for (int i = 0; i < count; i++) {
            KhachHang kh = allKhachHang.get(i);
            if (kh.getDiemLoyalty() > 0) {
                Map<String, Object> item = new HashMap<>();
                item.put("maKH", kh.getMaKH());
                item.put("hoTen", kh.getHoTen());
                item.put("diemLoyalty", kh.getDiemLoyalty());
                item.put("capDo", kh.getCapDoThanhVien());
                item.put("tongChiTieu", kh.getTongChiTieuNam());
                
                result.add(item);
            }
        }
        
        return result;
    }
    
    public Map<Integer, Map<String, Object>> thongKeDiem12Thang(int maKH) throws SQLException {
        Map<Integer, Map<String, Object>> result = new HashMap<>();
        
        LocalDateTime now = LocalDateTime.now();
        int currentYear = now.getYear();
        
        for (int month = 1; month <= 12; month++) {
            int diemCong = logDiemDAO.getDiemCongTrongThang(maKH, month, currentYear);
            int diemSuDung = logDiemDAO.getDiemSuDungTrongThang(maKH, month, currentYear);
            
            Map<String, Object> monthData = new HashMap<>();
            monthData.put("diemCong", diemCong);
            monthData.put("diemSuDung", diemSuDung);
            monthData.put("diemNet", diemCong - diemSuDung);
            
            result.put(month, monthData);
        }
        
        return result;
    }
}