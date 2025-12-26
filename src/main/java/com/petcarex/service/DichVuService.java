// file: com/petcarex/service/DichVuService.java
package com.petcarex.service;

import com.petcarex.dao.DichVuDAO;
import com.petcarex.model.DichVu;
import java.sql.SQLException;
import java.util.List;

public class DichVuService {
    private final DichVuDAO dichVuDAO;
    
    public DichVuService() {
        this.dichVuDAO = new DichVuDAO();
    }
    
    public DichVu themDichVu(DichVu dv) throws Exception {
        validateDichVu(dv);
        dichVuDAO.create(dv);
        return dv;
    }
    
    public DichVu timDichVu(int maDichVu) throws SQLException {
        return dichVuDAO.read(maDichVu);
    }
    
    public List<DichVu> layTatCaDichVu() throws SQLException {
        return dichVuDAO.findAll();
    }
    
    public List<DichVu> layDichVuTheoLoai(String loai) throws SQLException {
        return dichVuDAO.findByLoai(loai);
    }
    
    public List<DichVu> layDichVuKhaDung() throws SQLException {
        return dichVuDAO.findActiveServices();
    }
    
    public List<DichVu> layDichVuTheoChiNhanh(int maChiNhanh) throws SQLException {
        return dichVuDAO.findServicesByChiNhanh(maChiNhanh);
    }
    
    public List<DichVu> timKiemDichVu(String keyword) throws SQLException {
        return dichVuDAO.searchByName(keyword);
    }
    
    public void capNhatDichVu(DichVu dv) throws Exception {
        validateDichVu(dv);
        dichVuDAO.update(dv);
    }
    
    public void xoaDichVu(int maDichVu) throws SQLException {
        dichVuDAO.delete(maDichVu);
    }
    
    private void validateDichVu(DichVu dv) throws Exception {
        if (dv.getTenDichVu() == null || dv.getTenDichVu().trim().isEmpty()) {
            throw new Exception("Tên dịch vụ không được để trống");
        }
        
        if (dv.getLoaiDichVu() == null || dv.getLoaiDichVu().trim().isEmpty()) {
            throw new Exception("Loại dịch vụ không được để trống");
        }
        
        if (dv.getGiaCoBan() <= 0) {
            throw new Exception("Giá dịch vụ phải lớn hơn 0");
        }
        
        if (dv.getThoiGianThucHien() <= 0) {
            throw new Exception("Thời gian thực hiện phải lớn hơn 0");
        }
    }
}