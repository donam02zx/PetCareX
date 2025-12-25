package com.petcarex.view;

import com.petcarex.controller.ThuCungController;
import com.petcarex.model.ThuCung;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ThuCungView extends JPanel {
    private ThuCungController controller;
    private boolean isNhanVien;
    
    // UI Components
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField txtSearch;
    private JButton btnSearch, btnAdd, btnEdit, btnDelete, btnRefresh;
    private JComboBox<String> cboLoaiFilter;
    
    public ThuCungView(ThuCungController controller, boolean isNhanVien) {
        this.controller = controller;
        this.isNhanVien = isNhanVien;
        
        initComponents();
        setupLayout();
        loadData();
    }
    
    private void initComponents() {
        // Search panel components
        txtSearch = new JTextField(20);
        txtSearch.setToolTipText("Nhập tên thú cưng hoặc tên chủ");
        
        btnSearch = new JButton("🔍 Tìm kiếm");
        btnSearch.addActionListener(e -> searchThuCung());
        
        // Filter by type (for employees)
        cboLoaiFilter = new JComboBox<>(new String[]{"Tất cả", "Chó", "Mèo", "Chim", "Thỏ", "Hamster", "Bò sát", "Khác"});
        cboLoaiFilter.addActionListener(e -> filterByType());
        
        // Action buttons
        btnAdd = new JButton("➕ Thêm mới");
        btnAdd.addActionListener(e -> showAddDialog());
        
        btnEdit = new JButton("✏️ Chỉnh sửa");
        btnEdit.addActionListener(e -> showEditDialog());
        
        btnDelete = new JButton("🗑️ Xóa");
        btnDelete.addActionListener(e -> deleteSelected());
        
        btnRefresh = new JButton("🔄 Làm mới");
        btnRefresh.addActionListener(e -> refreshData());
        
        // Table setup
        String[] columns;
        if (isNhanVien) {
            columns = new String[]{
                "Mã TC", "Tên thú cưng", "Loài", "Giống", "Ngày sinh", 
                "Tuổi", "Giới tính", "Tình trạng", "Chủ", "Mã KH"
            };
        } else {
            columns = new String[]{
                "Mã TC", "Tên thú cưng", "Loài", "Giống", "Ngày sinh", 
                "Tuổi", "Giới tính", "Tình trạng"
            };
        }
        
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Integer.class; // Mã TC
                if (columnIndex == 5) return Integer.class; // Tuổi
                if (columnIndex == 9) return Integer.class; // Mã KH
                return String.class;
            }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getColumn(0).setPreferredWidth(60); // Mã TC
        table.getColumnModel().getColumn(1).setPreferredWidth(120); // Tên
        table.getColumnModel().getColumn(2).setPreferredWidth(80); // Loài
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // Giống
        
        // Enable sorting with TableRowSorter
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // ===== TOP PANEL: Search and Filter =====
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.add(new JLabel("Tìm kiếm:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        
        if (isNhanVien) {
            searchPanel.add(new JLabel("Lọc theo loài:"));
            searchPanel.add(cboLoaiFilter);
        }
        
        // Action buttons panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        actionPanel.add(btnAdd);
        actionPanel.add(btnEdit);
        actionPanel.add(btnDelete);
        actionPanel.add(btnRefresh);
        
        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(actionPanel, BorderLayout.EAST);
        
        // ===== CENTER PANEL: Table =====
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách thú cưng"));
        
        // ===== BOTTOM PANEL: Statistics =====
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Thống kê"));
        
        JLabel lblStats = new JLabel("Tổng số: 0 | Chó: 0 | Mèo: 0 | Khác: 0");
        lblStats.setFont(new Font("Segoe UI", Font.BOLD, 12));
        bottomPanel.add(lblStats);
        
        // ===== ASSEMBLE =====
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    public void loadData() {
        // Clear filter before loading new data
        sorter.setRowFilter(null);
        
        List<ThuCung> ds = controller.getAllThuCung();
        displayThuCung(ds);
        updateStatistics(ds);
    }
    
    public void displayThuCung(List<ThuCung> ds) {
        tableModel.setRowCount(0);
        
        if (ds != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            for (ThuCung tc : ds) {
                Object[] row;
                if (isNhanVien) {
                    row = new Object[]{
                        tc.getMaThuCung(),
                        tc.getTenThuCung(),
                        tc.getLoai(),
                        tc.getGiong(),
                        tc.getNgaySinh() != null ? tc.getNgaySinh().format(formatter) : "",
                        calculateAge(tc.getNgaySinh()),
                        tc.getGioiTinh(),
                        tc.getTinhTrangSucKhoe() != null ? tc.getTinhTrangSucKhoe() : "Khỏe mạnh",
                        tc.getTenChu() != null ? tc.getTenChu() : "",
                        tc.getMaKH()
                    };
                } else {
                    row = new Object[]{
                        tc.getMaThuCung(),
                        tc.getTenThuCung(),
                        tc.getLoai(),
                        tc.getGiong(),
                        tc.getNgaySinh() != null ? tc.getNgaySinh().format(formatter) : "",
                        calculateAge(tc.getNgaySinh()),
                        tc.getGioiTinh(),
                        tc.getTinhTrangSucKhoe() != null ? tc.getTinhTrangSucKhoe() : "Khỏe mạnh"
                    };
                }
                tableModel.addRow(row);
            }
        }
    }
    
    private int calculateAge(LocalDate ngaySinh) {
        if (ngaySinh == null) return 0;
        
        LocalDate now = LocalDate.now();
        int years = now.getYear() - ngaySinh.getYear();
        int months = now.getMonthValue() - ngaySinh.getMonthValue();
        
        if (months < 0) {
            years--;
            months += 12;
        }
        
        // Nếu dưới 1 tuổi, trả về tháng
        if (years == 0) {
            return months;
        }
        return years;
    }
    
    private void updateStatistics(List<ThuCung> ds) {
        if (ds == null) return;
        
        int total = ds.size();
        int dogCount = 0;
        int catCount = 0;
        int otherCount = 0;
        
        for (ThuCung tc : ds) {
            if ("Chó".equals(tc.getLoai())) {
                dogCount++;
            } else if ("Mèo".equals(tc.getLoai())) {
                catCount++;
            } else {
                otherCount++;
            }
        }
        
        // Update stats label
        JPanel bottomPanel = (JPanel) getComponent(2); // Assuming bottom panel is at index 2
        if (bottomPanel != null) {
            Component[] comps = bottomPanel.getComponents();
            if (comps.length > 0 && comps[0] instanceof JLabel) {
                JLabel lblStats = (JLabel) comps[0];
                lblStats.setText(String.format("Tổng số: %d | Chó: %d | Mèo: %d | Khác: %d", 
                    total, dogCount, catCount, otherCount));
            }
        }
    }
    
    public void refreshTable() {
        loadData();
    }
    
    private void searchThuCung() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            loadData();
        } else {
            List<ThuCung> results = controller.searchThuCung(keyword);
            displayThuCung(results);
            updateStatistics(results);
        }
    }
    
    private void filterByType() {
        if (!isNhanVien) return;
        
        String selectedType = (String) cboLoaiFilter.getSelectedItem();
        if ("Tất cả".equals(selectedType)) {
            sorter.setRowFilter(null); // Clear filter
        } else {
            // Filter by selected type
            RowFilter<DefaultTableModel, Integer> filter = RowFilter.regexFilter("^" + selectedType + "$", 2);
            sorter.setRowFilter(filter);
        }
    }
    
    private void refreshData() {
        loadData();
        txtSearch.setText("");
        cboLoaiFilter.setSelectedIndex(0);
    }
    
    private void showAddDialog() {
        ThuCungDialog dialog = new ThuCungDialog(
            (JFrame) SwingUtilities.getWindowAncestor(this), 
            controller, 
            null, // null for add mode
            isNhanVien
        );
        dialog.setVisible(true);
    }
    
    private void showEditDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showError("Vui lòng chọn một thú cưng để chỉnh sửa");
            return;
        }
        
        // Get actual model row (considering sorting)
        int modelRow = table.convertRowIndexToModel(selectedRow);
        int maThuCung = (int) tableModel.getValueAt(modelRow, 0);
        
        ThuCung tc = controller.getThuCungById(maThuCung);
        if (tc != null) {
            ThuCungDialog dialog = new ThuCungDialog(
                (JFrame) SwingUtilities.getWindowAncestor(this),
                controller,
                tc,
                isNhanVien
            );
            dialog.setVisible(true);
        }
    }
    
    private void deleteSelected() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showError("Vui lòng chọn một thú cưng để xóa");
            return;
        }
        
        int modelRow = table.convertRowIndexToModel(selectedRow);
        int maThuCung = (int) tableModel.getValueAt(modelRow, 0);
        
        controller.deleteThuCung(maThuCung);
    }
    
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
    
    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }
}