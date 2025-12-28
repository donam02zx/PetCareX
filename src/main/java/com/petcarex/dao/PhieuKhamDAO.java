package com.petcarex.dao;

import com.petcarex.model.PhieuKham;
import java.sql.*;
import java.util.List;

public class PhieuKhamDAO extends BaseDAO<PhieuKham> {

    @Override
    public void create(PhieuKham pk) throws SQLException {
        String sql = "INSERT INTO PHIEU_KHAM " +
                "(MaThuCung, MaBacSi, MaChiNhanh, TrieuChung, ChuanDoan, ToaThuoc, NgayHenTaiKham, GhiChu) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        executeUpdate(sql,
                pk.getMaThuCung(),
                pk.getMaBacSi(),
                pk.getMaChiNhanh(),
                pk.getTrieuChung(),
                pk.getChuanDoan(),
                pk.getToaThuoc(),
                pk.getNgayHenTaiKham() != null ? Date.valueOf(pk.getNgayHenTaiKham()) : null,
                pk.getGhiChu()
        );
    }

    @Override
    public PhieuKham read(int maPhieuKham) throws SQLException {
        String sql = "SELECT * FROM PHIEU_KHAM WHERE MaPhieuKham = ?";
        return executeQuerySingle(sql, this::mapResultSet, maPhieuKham);
    }

    @Override
    public void update(PhieuKham pk) throws SQLException {
        String sql = "UPDATE PHIEU_KHAM SET " +
                "MaThuCung = ?, MaBacSi = ?, MaChiNhanh = ?, TrieuChung = ?, ChuanDoan = ?, ToaThuoc = ?, " +
                "NgayHenTaiKham = ?, GhiChu = ? " +
                "WHERE MaPhieuKham = ?";
        executeUpdate(sql,
                pk.getMaThuCung(),
                pk.getMaBacSi(),
                pk.getMaChiNhanh(),
                pk.getTrieuChung(),
                pk.getChuanDoan(),
                pk.getToaThuoc(),
                pk.getNgayHenTaiKham() != null ? Date.valueOf(pk.getNgayHenTaiKham()) : null,
                pk.getGhiChu(),
                pk.getMaPhieuKham()
        );
    }

    @Override
    public void delete(int maPhieuKham) throws SQLException {
        String sql = "DELETE FROM PHIEU_KHAM WHERE MaPhieuKham = ?";
        executeUpdate(sql, maPhieuKham);
    }

    @Override
    public List<PhieuKham> findAll() throws SQLException {
        String sql = "SELECT * FROM PHIEU_KHAM ORDER BY NgayKham DESC";
        return executeQuery(sql, this::mapResultSet);
    }

    public List<PhieuKham> findByThuCung(int maThuCung) throws SQLException {
        String sql = "SELECT * FROM PHIEU_KHAM WHERE MaThuCung = ? ORDER BY NgayKham DESC";
        return executeQuery(sql, this::mapResultSet, maThuCung);
    }

    private PhieuKham mapResultSet(ResultSet rs) throws SQLException {
        PhieuKham pk = new PhieuKham();
        pk.setMaPhieuKham(rs.getInt("MaPhieuKham"));
        pk.setMaThuCung(rs.getInt("MaThuCung"));
        pk.setMaBacSi(rs.getInt("MaBacSi"));
        pk.setMaChiNhanh(rs.getInt("MaChiNhanh"));

        Timestamp ngayKhamTs = rs.getTimestamp("NgayKham");
        if (ngayKhamTs != null) pk.setNgayKham(ngayKhamTs.toLocalDateTime());

        pk.setTrieuChung(rs.getString("TrieuChung"));
        pk.setChuanDoan(rs.getString("ChuanDoan"));
        pk.setToaThuoc(rs.getString("ToaThuoc"));

        Date ngayHen = rs.getDate("NgayHenTaiKham");
        if (ngayHen != null) pk.setNgayHenTaiKham(ngayHen.toLocalDate());

        pk.setGhiChu(rs.getString("GhiChu"));
        return pk;
    }
}
