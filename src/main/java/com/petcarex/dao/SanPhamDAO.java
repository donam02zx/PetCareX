package com.petcarex.dao;

import com.petcarex.model.SanPham;

import java.sql.*;
import java.util.List;

public class SanPhamDAO extends BaseDAO<SanPham> {

    @Override
    public void create(SanPham sp) throws SQLException {
        String sql = "INSERT INTO SAN_PHAM (TenSanPham, LoaiSanPham, MoTa, DonViTinh, GiaBan, NhaSanXuat, HanSuDung) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        executeUpdate(sql,
                sp.getTenSanPham(),
                sp.getLoaiSanPham(),
                sp.getMoTa(),
                sp.getDonViTinh(),
                sp.getGiaBan(),
                sp.getNhaSanXuat(),
                sp.getHanSuDung() != null ? Date.valueOf(sp.getHanSuDung()) : null
        );
    }

    @Override
    public SanPham read(int maSanPham) throws SQLException {
        String sql = "SELECT * FROM SAN_PHAM WHERE MaSanPham = ?";
        return executeQuerySingle(sql, this::mapResultSet, maSanPham);
    }

    @Override
    public void update(SanPham sp) throws SQLException {
        String sql = "UPDATE SAN_PHAM SET TenSanPham=?, LoaiSanPham=?, MoTa=?, DonViTinh=?, GiaBan=?, NhaSanXuat=?, HanSuDung=? " +
                "WHERE MaSanPham=?";
        executeUpdate(sql,
                sp.getTenSanPham(),
                sp.getLoaiSanPham(),
                sp.getMoTa(),
                sp.getDonViTinh(),
                sp.getGiaBan(),
                sp.getNhaSanXuat(),
                sp.getHanSuDung() != null ? Date.valueOf(sp.getHanSuDung()) : null,
                sp.getMaSanPham()
        );
    }

    @Override
    public void delete(int maSanPham) throws SQLException {
        String sql = "DELETE FROM SAN_PHAM WHERE MaSanPham = ?";
        executeUpdate(sql, maSanPham);
    }

    @Override
    public List<SanPham> findAll() throws SQLException {
        String sql = "SELECT * FROM SAN_PHAM";
        return executeQuery(sql, this::mapResultSet);
    }

    public List<SanPham> findByLoai(String loaiSanPham) throws SQLException {
        String sql = "SELECT * FROM SAN_PHAM WHERE LoaiSanPham = ?";
        return executeQuery(sql, this::mapResultSet, loaiSanPham);
    }

    public List<SanPham> searchByNameAndLoai(String name, String loaiSanPham) throws SQLException {
        String sql = "SELECT * FROM SAN_PHAM WHERE LoaiSanPham = ? AND TenSanPham LIKE ?";
        String searchTerm = "%" + name + "%";
        return executeQuery(sql, this::mapResultSet, loaiSanPham, searchTerm);
    }

    private SanPham mapResultSet(ResultSet rs) throws SQLException {
        SanPham sp = new SanPham();
        sp.setMaSanPham(rs.getInt("MaSanPham"));
        sp.setTenSanPham(rs.getString("TenSanPham"));
        sp.setLoaiSanPham(rs.getString("LoaiSanPham"));
        sp.setMoTa(rs.getString("MoTa"));
        sp.setDonViTinh(rs.getString("DonViTinh"));
        sp.setGiaBan(rs.getDouble("GiaBan"));
        sp.setNhaSanXuat(rs.getString("NhaSanXuat"));

        Date hanSuDung = rs.getDate("HanSuDung");
        if (hanSuDung != null) {
            sp.setHanSuDung(hanSuDung.toLocalDate());
        }
        return sp;
    }
}
