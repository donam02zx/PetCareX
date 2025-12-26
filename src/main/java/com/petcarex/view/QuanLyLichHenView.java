// file: com/petcarex/view/QuanLyLichHenView.java
package com.petcarex.view;

import com.petcarex.controller.QuanLyLichHenController;
import com.petcarex.model.LichHen;
import com.petcarex.model.ThuCung;
import com.petcarex.model.DichVu;
import com.petcarex.model.NhanVien;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class QuanLyLichHenView extends JPanel {
    private QuanLyLichHenController controller;
    
    // UI Components
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnRefresh, btnFilter, btnAdd, btnEdit, btnDelete;
    private JButton btnConfirm, btnComplete, btnCancel;
    private JTextField txtSearch;
    private JComboBox<String> cboTrangThai, cboChiNhanh;
    private JSpinner spnFromDate, spnToDate;
    
    public QuanLyLichHenView(QuanLyLichHenController controller) {
        this.controller = controller;
        initComponents();
        setupLayout();
        loadData();
    }
    
    private void initComponents() {
        // Filter components
        txtSearch = new JTextField(15);
        
        cboTrangThai = new JComboBox<>();
        cboTrangThai.addItem("Tất cả");
        List<String> trangThaiList = controller.getDanhSachTrangThai();
        if (trangThaiList != null) {
            for (String trangThai : trangThaiList) {
                cboTrangThai.addItem(trangThai);
            }
        }
        
        cboChiNhanh = new JComboBox<>();
        cboChiNhanh.addItem("Tất cả");
        List<String> chiNhanhList = controller.getDanhSachChiNhanh();
        if (chiNhanhList != null) {
            for (String chiNhanh : chiNhanhList) {
                cboChiNhanh.addItem(chiNhanh);
            }
        }
        
        // Date spinners
        SpinnerDateModel fromModel = new SpinnerDateModel();
        spnFromDate = new JSpinner(fromModel);
        JSpinner.DateEditor fromEditor = new JSpinner.DateEditor(spnFromDate, "dd/MM/yyyy");
        spnFromDate.setEditor(fromEditor);
        
        SpinnerDateModel toModel = new SpinnerDateModel();
        spnToDate = new JSpinner(toModel);
        JSpinner.DateEditor toEditor = new JSpinner.DateEditor(spnToDate, "dd/MM/yyyy");
        spnToDate.setEditor(toEditor);
        
        // Buttons
        btnRefresh = new JButton("Làm mới");
        btnRefresh.addActionListener(e -> refreshTable());
        
        btnFilter = new JButton("Lọc");
        btnFilter.addActionListener(e -> filterData());
        
        btnAdd = new JButton("Thêm lịch");
        btnAdd.addActionListener(e -> showAddDialog());
        
        btnEdit = new JButton("Chi tiết");
        btnEdit.addActionListener(e -> showDetailDialog());
        
        btnDelete = new JButton("Xóa");
        btnDelete.addActionListener(e -> deleteLichHen());
        
        btnConfirm = new JButton("Xác nhận");
        btnConfirm.addActionListener(e -> confirmLichHen());
        
        btnComplete = new JButton("Hoàn thành");
        btnComplete.addActionListener(e -> completeLichHen());
        
        btnCancel = new JButton("Hủy lịch");
        btnCancel.addActionListener(e -> cancelLichHen());
        
        // Table
        String[] columns = {
            "Mã LH", "Thú cưng", "Tên chủ", "Loại lịch hẹn", "Thời gian",
            "Chi nhánh", "Bác sĩ", "Dịch vụ", "Trạng thái", "Ghi chú"
        };
        
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);
        
        // Custom renderer for status column (column index 7)
        table.getColumnModel().getColumn(9).setCellRenderer(new StatusCellRenderer());
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Top panel: Search and filter
        JPanel topPanel = new JPanel(new BorderLayout());
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Tìm kiếm:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnRefresh);
        searchPanel.add(btnFilter);
        
        JPanel filterPanel = new JPanel(new GridLayout(2, 4, 5, 5));
        filterPanel.add(new JLabel("Từ ngày:"));
        filterPanel.add(spnFromDate);
        filterPanel.add(new JLabel("Đến ngày:"));
        filterPanel.add(spnToDate);
        filterPanel.add(new JLabel("Trạng thái:"));
        filterPanel.add(cboTrangThai);
        filterPanel.add(new JLabel("Chi nhánh:"));
        filterPanel.add(cboChiNhanh);
        
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(filterPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnConfirm);
        buttonPanel.add(btnComplete);
        buttonPanel.add(btnCancel);
        buttonPanel.add(btnDelete);
        
        // Center panel: Table
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Add components
        add(topPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
    }
    
    private void loadData() {
        refreshTable();
    }
    
    String[] columns = {
    	    "Mã LH", "Thú cưng", "Chủ", "Loại", "Thời gian", 
    	    "Chi nhánh", "Bác sĩ", "Dịch vụ", "Trạng thái", "Ghi chú"
    	};
    
    
    public void refreshTable() {
        tableModel.setRowCount(0);
        
        if (controller != null) {
            List<LichHen> dsLichHen = controller.getAllLichHen();
            if (dsLichHen != null) {
                for (LichHen lh : dsLichHen) {
                    Object[] row = {
                    		  lh.getMaLichHen(),
                    		  lh.getTenThuCung(),
                    		  lh.getTenChu() != null ? lh.getTenChu() : "N/A", // HIỂN THỊ TÊN CHỦ
                    		  lh.getLoaiLichHen(),
                    		  lh.getThoiGianDisplay(),
                    		  lh.getTenChiNhanh(),
                    		  lh.getTenBacSi() != null ? lh.getTenBacSi() : "Chưa chọn",
                    		  lh.getTenDichVu() != null ? lh.getTenDichVu() : "Chưa chọn",
                    		  lh.getTrangThai(),
                    		  lh.getGhiChu() != null ? lh.getGhiChu() : ""
                    };
                    tableModel.addRow(row);
                }
            }
        }
    }
    
    private void filterData() {
        // Get filter values
        String trangThai = (String) cboTrangThai.getSelectedItem();
        if ("Tất cả".equals(trangThai)) {
            trangThai = null;
        }
        
        String chiNhanh = (String) cboChiNhanh.getSelectedItem();
        Integer maChiNhanh = null;
        if (!"Tất cả".equals(chiNhanh)) {
            // Convert chi nhánh name to ID (temporary)
            maChiNhanh = chiNhanh.equals("PetCareX Quận 1") ? 1 : 2;
        }
        
        // Get date range
        java.util.Date fromDate = (java.util.Date) spnFromDate.getValue();
        java.util.Date toDate = (java.util.Date) spnToDate.getValue();
        
        LocalDateTime from = fromDate.toInstant()
            .atZone(java.time.ZoneId.systemDefault())
            .toLocalDateTime()
            .withHour(0).withMinute(0).withSecond(0);
            
        LocalDateTime to = toDate.toInstant()
            .atZone(java.time.ZoneId.systemDefault())
            .toLocalDateTime()
            .withHour(23).withMinute(59).withSecond(59);
        
        // Apply filter
        List<LichHen> filteredList = controller.filterLichHen(from, to, trangThai, maChiNhanh);
        
        // Update table
        tableModel.setRowCount(0);
        if (filteredList != null) {
            for (LichHen lh : filteredList) {
                Object[] row = {
                		  lh.getMaLichHen(),
                		  lh.getTenThuCung(),
                		  lh.getTenChu() != null ? lh.getTenChu() : "N/A", // HIỂN THỊ TÊN CHỦ
                		  lh.getLoaiLichHen(),
                		  lh.getThoiGianDisplay(),
                		  lh.getTenChiNhanh(),
                		  lh.getTenBacSi() != null ? lh.getTenBacSi() : "Chưa chọn",
                		  lh.getTenDichVu() != null ? lh.getTenDichVu() : "Chưa chọn",
                		  lh.getTrangThai(),
                		  lh.getGhiChu() != null ? lh.getGhiChu() : ""
                };
                tableModel.addRow(row);
            }
        }
    }
    
    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
            "Đặt lịch cho khách hàng", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(this);
        
        // Form fields
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Thu cưng
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Thú cưng:"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        JComboBox<ThuCung> cboThuCung = new JComboBox<>();
        List<ThuCung> dsThuCung = controller.getAllThuCung();
        if (dsThuCung != null) {
            for (ThuCung tc : dsThuCung) {
                cboThuCung.addItem(tc);
            }
        }
        formPanel.add(cboThuCung, gbc);
        
        // Loại lịch
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Loại lịch:"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        JComboBox<String> cboLoaiLich = new JComboBox<>(new String[]{
            "Khám bệnh", "Tiêm phòng", "Spa & Grooming", "Khác"
        });
        formPanel.add(cboLoaiLich, gbc);
        
        // Dịch vụ
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Dịch vụ:"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        JComboBox<DichVu> cboDichVu = new JComboBox<>();
        List<DichVu> dsDichVu = controller.getAllDichVu();
        if (dsDichVu != null) {
            for (DichVu dv : dsDichVu) {
                cboDichVu.addItem(dv);
            }
        }
        formPanel.add(cboDichVu, gbc);
        
        // Bác sĩ
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Bác sĩ:"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        JComboBox<NhanVien> cboBacSi = new JComboBox<>();
        List<NhanVien> dsBacSi = controller.getAllBacSi();
        if (dsBacSi != null) {
            for (NhanVien nv : dsBacSi) {
                cboBacSi.addItem(nv);
            }
        }
        formPanel.add(cboBacSi, gbc);
        
        // Chi nhánh
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Chi nhánh:"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        JComboBox<String> cboChiNhanh = new JComboBox<>(new String[]{
            "PetCareX Quận 1", "PetCareX Quận 7"
        });
        formPanel.add(cboChiNhanh, gbc);
        
        // Thời gian
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Thời gian:"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner spnNgayGio = new JSpinner(dateModel);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(spnNgayGio, "dd/MM/yyyy HH:mm");
        spnNgayGio.setEditor(timeEditor);
        formPanel.add(spnNgayGio, gbc);
        
        // Ghi chú
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Ghi chú:"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.BOTH;
        JTextArea txtGhiChu = new JTextArea(3, 20);
        txtGhiChu.setLineWrap(true);
        txtGhiChu.setWrapStyleWord(true);
        JScrollPane scrollGhiChu = new JScrollPane(txtGhiChu);
        formPanel.add(scrollGhiChu, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton btnSave = new JButton("Lưu");
        JButton btnCancelDialog = new JButton("Hủy");
        
        btnSave.addActionListener(e -> {
            try {
                ThuCung selectedTC = (ThuCung) cboThuCung.getSelectedItem();
                DichVu selectedDV = (DichVu) cboDichVu.getSelectedItem();
                NhanVien selectedBS = (NhanVien) cboBacSi.getSelectedItem();
                
                LichHen lichHen = new LichHen();
                lichHen.setMaThuCung(selectedTC.getMaThuCung());
                lichHen.setLoaiLichHen((String) cboLoaiLich.getSelectedItem());
                
                // Convert Date to LocalDateTime
                java.util.Date selectedDate = (java.util.Date) spnNgayGio.getValue();
                LocalDateTime thoiGian = selectedDate.toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDateTime();
                lichHen.setThoiGian(thoiGian);
                
                // Chi nhánh
                int maChiNhanh = cboChiNhanh.getSelectedIndex() + 1;
                lichHen.setMaChiNhanh(maChiNhanh);
                
                if (selectedBS != null) {
                    lichHen.setMaBacSi(selectedBS.getMaNV());
                }
                
                if (selectedDV != null) {
                    lichHen.setMaDichVu(selectedDV.getMaDichVu());
                }
                
                lichHen.setGhiChu(txtGhiChu.getText());
                
                if (controller.datLichChoKhach(lichHen)) {
                    dialog.dispose();
                }
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi: " + ex.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnCancelDialog.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancelDialog);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void showDetailDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showError("Vui lòng chọn một lịch hẹn để xem chi tiết");
            return;
        }
        
        int maLichHen = (int) tableModel.getValueAt(selectedRow, 0);
        LichHen lichHen = controller.getChiTietLichHen(maLichHen);
        
        if (lichHen == null) {
            showError("Không tìm thấy thông tin lịch hẹn");
            return;
        }
        
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
            "Chi tiết lịch hẹn #" + maLichHen, true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        
        // Information panel
        JPanel infoPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        infoPanel.add(new JLabel("Mã lịch hẹn:"));
        infoPanel.add(new JLabel(String.valueOf(lichHen.getMaLichHen())));
        
        infoPanel.add(new JLabel("Thú cưng:"));
        infoPanel.add(new JLabel(lichHen.getTenThuCung()));
        
        infoPanel.add(new JLabel("Loại lịch:"));
        infoPanel.add(new JLabel(lichHen.getLoaiLichHen()));
        
        infoPanel.add(new JLabel("Thời gian:"));
        infoPanel.add(new JLabel(lichHen.getThoiGianDisplay()));
        
        infoPanel.add(new JLabel("Chi nhánh:"));
        infoPanel.add(new JLabel(lichHen.getTenChiNhanh()));
        
        infoPanel.add(new JLabel("Bác sĩ:"));
        infoPanel.add(new JLabel(lichHen.getTenBacSi() != null ? lichHen.getTenBacSi() : "Chưa chọn"));
        
        infoPanel.add(new JLabel("Trạng thái:"));
        JLabel lblStatus = new JLabel(lichHen.getTrangThai());
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        if ("Đã hủy".equals(lichHen.getTrangThai())) {
            lblStatus.setForeground(Color.RED);
        } else if ("Đã hoàn thành".equals(lichHen.getTrangThai())) {
            lblStatus.setForeground(new Color(0, 128, 0));
        } else if ("Đã xác nhận".equals(lichHen.getTrangThai())) {
            lblStatus.setForeground(new Color(0, 0, 255));
        }
        infoPanel.add(lblStatus);
        
        infoPanel.add(new JLabel("Ghi chú:"));
        JTextArea txtGhiChu = new JTextArea(lichHen.getGhiChu() != null ? lichHen.getGhiChu() : "");
        txtGhiChu.setEditable(false);
        txtGhiChu.setLineWrap(true);
        txtGhiChu.setWrapStyleWord(true);
        txtGhiChu.setBackground(infoPanel.getBackground());
        infoPanel.add(new JScrollPane(txtGhiChu));
        
        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton btnClose = new JButton("Đóng");
        btnClose.addActionListener(e -> dialog.dispose());
        buttonPanel.add(btnClose);
        
        dialog.add(infoPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void deleteLichHen() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showError("Vui lòng chọn một lịch hẹn để xóa");
            return;
        }
        
        int maLichHen = (int) tableModel.getValueAt(selectedRow, 0);
        String trangThai = (String) tableModel.getValueAt(selectedRow, 9);
        
        if ("Đã hoàn thành".equals(trangThai) || "Đã hủy".equals(trangThai)) {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa lịch hẹn này?\nLịch hẹn đã hoàn thành/hủy sẽ bị xóa vĩnh viễn.",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    controller.xoaLichHen(maLichHen);
                } catch (Exception e) {
                    showError("Lỗi khi xóa lịch hẹn: " + e.getMessage());
                }
            }
        } else {
            showError("Chỉ có thể xóa lịch hẹn đã hoàn thành hoặc đã hủy");
        }
    }
    
//    private void confirmLichHen() {
//        int selectedRow = table.getSelectedRow();
//        if (selectedRow == -1) {
//            showError("Vui lòng chọn một lịch hẹn để xác nhận");
//            return;
//        }
//        
//        String trangThai = (String) tableModel.getValueAt(selectedRow, 9);
//        if (!"Chờ xác nhận".equals(trangThai)) {
//            showError("Chỉ có thể xác nhận lịch hẹn đang chờ xác nhận");
//            return;
//        }
//        
//        int maLichHen = (int) tableModel.getValueAt(selectedRow, 0);
//        controller.xacNhanLichHen(maLichHen);
//    }
    
    private void confirmLichHen() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showError("Vui lòng chọn một lịch hẹn để xác nhận");
            return;
        }
        
        // DEBUG: Hiển thị tất cả columns
        System.out.println("\n=== DEBUG CONFIRM APPOINTMENT ===");
        for (int i = 0; i < table.getColumnCount(); i++) {
            Object value = table.getValueAt(selectedRow, i);
            System.out.println("Column " + i + " (" + table.getColumnName(i) + "): " + value + 
                             " (Type: " + (value != null ? value.getClass().getSimpleName() : "null") + ")");
        }
        
        // Lấy mã lịch hẹn từ column 0
        int maLichHen = (int) table.getValueAt(selectedRow, 0);
        System.out.println("MaLichHen from table: " + maLichHen);
        
        // Lấy trạng thái từ column 8 (index 8, là cột thứ 9)
        String trangThai = (String) table.getValueAt(selectedRow, 8);
        System.out.println("TrangThai from table: '" + trangThai + "'");
        
        if (!"Chờ xác nhận".equals(trangThai)) {
            showError("Chỉ có thể xác nhận lịch hẹn đang 'Chờ xác nhận'. Trạng thái hiện tại: '" + trangThai + "'");
            return;
        }
        
        controller.xacNhanLichHen(maLichHen);
    }
    
//    private void completeLichHen() {
//        int selectedRow = table.getSelectedRow();
//        if (selectedRow == -1) {
//            showError("Vui lòng chọn một lịch hẹn để đánh dấu hoàn thành");
//            return;
//        }
//        
//        String trangThai = (String) tableModel.getValueAt(selectedRow, 9);
//        if (!"Đã xác nhận".equals(trangThai)) {
//            showError("Chỉ có thể đánh dấu hoàn thành lịch hẹn đã xác nhận");
//            return;
//        }
//        
//        int maLichHen = (int) tableModel.getValueAt(selectedRow, 0);
//        controller.hoanThanhLichHen(maLichHen);
//    }
    
    private void completeLichHen() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showError("Vui lòng chọn một lịch hẹn để đánh dấu hoàn thành");
            return;
        }
        
        // DEBUG (có thể xóa sau)
        System.out.println("\n=== DEBUG COMPLETE APPOINTMENT ===");
        
        // Lấy mã lịch hẹn từ column 0
        int maLichHen = (int) table.getValueAt(selectedRow, 0);
        
        // Lấy trạng thái từ column 8 (index 8, là cột thứ 9)
        String trangThai = (String) table.getValueAt(selectedRow, 8);
        System.out.println("TrangThai from table: '" + trangThai + "'");
        
        if (!"Đã xác nhận".equals(trangThai)) {
            showError("Chỉ có thể đánh dấu hoàn thành lịch hẹn đã 'Đã xác nhận'. Trạng thái hiện tại: '" + trangThai + "'");
            return;
        }
        
        controller.hoanThanhLichHen(maLichHen);
    }

    
    private void cancelLichHen() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showError("Vui lòng chọn một lịch hẹn để hủy");
            return;
        }
        
        String trangThai = (String) tableModel.getValueAt(selectedRow, 9);
        if ("Đã hoàn thành".equals(trangThai) || "Đã hủy".equals(trangThai)) {
            showError("Không thể hủy lịch hẹn đã hoàn thành hoặc đã hủy");
            return;
        }
        
        int maLichHen = (int) tableModel.getValueAt(selectedRow, 0);
        controller.huyLichHen(maLichHen);
    }
    
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
    
    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Custom cell renderer for status column
    private class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, 
                isSelected, hasFocus, row, column);
            
            String status = (String) value;
            
            if (isSelected) {
                c.setBackground(table.getSelectionBackground());
                c.setForeground(table.getSelectionForeground());
            } else {
                switch (status) {
                    case "Chờ xác nhận":
                        c.setForeground(new Color(255, 140, 0)); // Orange
                        c.setBackground(new Color(255, 248, 225));
                        break;
                    case "Đã xác nhận":
                        c.setForeground(new Color(0, 100, 255)); // Blue
                        c.setBackground(new Color(225, 235, 255));
                        break;
                    case "Đã hoàn thành":
                        c.setForeground(new Color(0, 128, 0)); // Green
                        c.setBackground(new Color(225, 255, 225));
                        break;
                    case "Đã hủy":
                        c.setForeground(Color.RED);
                        c.setBackground(new Color(255, 225, 225));
                        break;
                    default:
                        c.setForeground(table.getForeground());
                        c.setBackground(table.getBackground());
                }
            }
            
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setHorizontalAlignment(SwingConstants.CENTER);
            setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
            
            return c;
        }
    }
}