package com.petcarex.view;

import com.petcarex.dao.SanPhamDAO;
import com.petcarex.model.SanPham;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class TraCuuThuoc extends JPanel {

    private final JTable table;
    private final DefaultTableModel tableModel;
    private final JTextField txtSearch;

    private final SanPhamDAO sanPhamDAO;

    public TraCuuThuoc() {
        setLayout(new BorderLayout());

        sanPhamDAO = new SanPhamDAO();

        // Top search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtSearch = new JTextField(20);
        JButton btnSearch = new JButton("Tìm kiếm");
        searchPanel.add(new JLabel("Tên thuốc:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        add(searchPanel, BorderLayout.NORTH);

        // Table setup
        String[] columns = {"Mã SP", "Tên thuốc", "Đơn vị", "Giá bán", "Nhà SX", "Hạn sử dụng"};
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

        // Load all medicines initially
        loadThuoc("");

        // Search button action
        btnSearch.addActionListener(e -> loadThuoc(txtSearch.getText().trim()));
    }

    /**
     * Load medicines of type 'Thuốc', optionally filtered by name
     */
    private void loadThuoc(String keyword) {
        try {
            List<SanPham> list;
            if (keyword.isEmpty()) {
                list = sanPhamDAO.findByLoai("Thuốc");
            } else {
                list = sanPhamDAO.searchByNameAndLoai(keyword, "Thuốc");
            }

            tableModel.setRowCount(0); // clear previous
            for (SanPham sp : list) {
                Object[] row = new Object[] {
                        sp.getMaSanPham(),
                        sp.getTenSanPham(),
                        sp.getDonViTinh(),
                        sp.getGiaBan(),
                        sp.getNhaSanXuat(),
                        sp.getHanSuDung() != null ? sp.getHanSuDung().toString() : ""
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tải dữ liệu thuốc: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public JTable getTable() {
        return table;
    }
}
