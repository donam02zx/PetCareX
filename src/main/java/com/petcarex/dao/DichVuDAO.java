// file: com/petcarex/dao/DichVuDAO.java
package com.petcarex.dao;

import com.petcarex.model.DichVu;
import java.sql.*;
import java.util.List;

public class DichVuDAO extends BaseDAO<DichVu> {
    
    @Override
    public void create(DichVu dv) throws SQLException {
        String sql = "INSERT INTO DICH_VU (TenDichVu, LoaiDichVu, MoTa, GiaCoBan, ThoiGianThucHien) " +
                    "VALUES (?, ?, ?, ?, ?)";
        
        executeUpdate(sql,
            dv.getTenDichVu(),
            dv.getLoaiDichVu(),
            dv.getMoTa(),
            dv.getGiaCoBan(),
            dv.getThoiGianThucHien()
        );
    }
    
    @Override
    public DichVu read(int maDichVu) throws SQLException {
        String sql = "SELECT * FROM DICH_VU WHERE MaDichVu = ?";
        return executeQuerySingle(sql, this::mapResultSet, maDichVu);
    }
    
    @Override
    public void update(DichVu dv) throws SQLException {
        String sql = "UPDATE DICH_VU SET TenDichVu = ?, LoaiDichVu = ?, MoTa = ?, " +
                    "GiaCoBan = ?, ThoiGianThucHien = ? WHERE MaDichVu = ?";
        
        executeUpdate(sql,
            dv.getTenDichVu(),
            dv.getLoaiDichVu(),
            dv.getMoTa(),
            dv.getGiaCoBan(),
            dv.getThoiGianThucHien(),
            dv.getMaDichVu()
        );
    }
    
    @Override
    public void delete(int maDichVu) throws SQLException {
        String sql = "DELETE FROM DICH_VU WHERE MaDichVu = ?";
        executeUpdate(sql, maDichVu);
    }
    
    @Override
    public List<DichVu> findAll() throws SQLException {
        String sql = "SELECT * FROM DICH_VU ORDER BY LoaiDichVu, TenDichVu";
        return executeQuery(sql, this::mapResultSet);
    }
    
    // Additional methods
    public List<DichVu> findByLoai(String loaiDichVu) throws SQLException {
        String sql = "SELECT * FROM DICH_VU WHERE LoaiDichVu = ? ORDER BY TenDichVu";
        return executeQuery(sql, this::mapResultSet, loaiDichVu);
    }
    
    public List<DichVu> searchByName(String name) throws SQLException {
        String sql = "SELECT * FROM DICH_VU WHERE TenDichVu LIKE ? ORDER BY TenDichVu";
        return executeQuery(sql, this::mapResultSet, "%" + name + "%");
    }
    
    public List<DichVu> findActiveServices() throws SQLException {
        // Assuming we have an Active field or we check through DICH_VU_CHI_NHANH
        String sql = "SELECT DISTINCT dv.* " +
                    "FROM DICH_VU dv " +
                    "JOIN DICH_VU_CHI_NHANH dvcn ON dv.MaDichVu = dvcn.MaDichVu " +
                    "WHERE dvcn.TrangThai = 'Đang cung cấp' " +
                    "ORDER BY dv.LoaiDichVu, dv.TenDichVu";
        return executeQuery(sql, this::mapResultSet);
    }
    
    public List<DichVu> findServicesByChiNhanh(int maChiNhanh) throws SQLException {
        String sql = "SELECT dv.*, dvcn.GiaTaiChiNhanh " +
                    "FROM DICH_VU dv " +
                    "JOIN DICH_VU_CHI_NHANH dvcn ON dv.MaDichVu = dvcn.MaDichVu " +
                    "WHERE dvcn.MaChiNhanh = ? AND dvcn.TrangThai = 'Đang cung cấp' " +
                    "ORDER BY dv.LoaiDichVu, dv.TenDichVu";
        return executeQuery(sql, this::mapResultSet, maChiNhanh);
    }
    
    // Map ResultSet
    private DichVu mapResultSet(ResultSet rs) throws SQLException {
        DichVu dv = new DichVu();
        
        dv.setMaDichVu(rs.getInt("MaDichVu"));
        dv.setTenDichVu(rs.getString("TenDichVu"));
        dv.setLoaiDichVu(rs.getString("LoaiDichVu"));
        dv.setMoTa(rs.getString("MoTa"));
        dv.setGiaCoBan(rs.getDouble("GiaCoBan"));
        dv.setThoiGianThucHien(rs.getInt("ThoiGianThucHien"));
        
        return dv;
    }
}