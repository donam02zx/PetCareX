package com.petcarex.dao;

import com.petcarex.config.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StatBSDAO {

    public int getTotalAppointmentsByDoctor(int maNV) throws SQLException {
        String sql = "SELECT COUNT(*) FROM PHIEU_KHAM WHERE MaBacSi = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, maNV);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    public int getTotalInjectionsByDoctor(int maNV) throws SQLException {
        String sql = "SELECT COUNT(*) FROM MUI_TIEM WHERE MaBacSi = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, maNV);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    public double getTotalExpensesByDoctor(int maNV) throws SQLException {
        String sql = "SELECT IFNULL(SUM(TongTien),0) FROM HOA_DON WHERE MaNV = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, maNV);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getDouble(1);
            }
        }
        return 0.0;
    }
}
