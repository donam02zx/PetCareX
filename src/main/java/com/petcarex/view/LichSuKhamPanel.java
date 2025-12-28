package com.petcarex.view;

import com.petcarex.dao.PhieuKhamDAO;
import com.petcarex.model.PhieuKham;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LichSuKhamPanel extends JPanel {

    private final JTable table;
    private final DefaultTableModel tableModel;
    private final PhieuKhamDAO phieuKhamDAO;

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public LichSuKhamPanel() {
        setLayout(new BorderLayout());

        phieuKhamDAO = new PhieuKhamDAO();

        // Table columns
        String[] columns = {"Mã phiếu khám", "Ngày khám", "Triệu chứng", "Chẩn đoán", "Toa thuốc", "Ngày hẹn tái khám", "Ghi chú"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Read-only
            }
        };

        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Load history for a given pet
     */
    public void loadHistory(int maThuCung) {
        try {
            List<PhieuKham> list = phieuKhamDAO.findByThuCung(maThuCung);
            tableModel.setRowCount(0); // Clear previous data

            for (PhieuKham pk : list) {
                Object[] row = new Object[] {
                        pk.getMaPhieuKham(),
                        pk.getNgayKham() != null ? pk.getNgayKham().format(dtf) : "",
                        pk.getTrieuChung(),
                        pk.getChuanDoan(),
                        pk.getToaThuoc(),
                        pk.getNgayHenTaiKham() != null ? pk.getNgayHenTaiKham().toString() : "",
                        pk.getGhiChu()
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải lịch sử khám: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public JTable getTable() {
        return table;
    }
}
