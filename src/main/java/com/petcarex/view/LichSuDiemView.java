// file: com/petcarex/view/LichSuDiemView.java
package com.petcarex.view;

import com.petcarex.service.LogDiemService;
import com.petcarex.service.KhachHangService;
import com.petcarex.model.LogDiem;
import com.petcarex.model.KhachHang;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

public class LichSuDiemView extends JPanel {
    private LogDiemService logDiemService;
    private KhachHangService khachHangService;
    private int maKH;
    
    // UI Components
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblTongDiem, lblDiemCongThang, lblDiemSuDungThang;
    private JComboBox<String> cboLoaiDiem;
    private JButton btnRefresh, btnXuatExcel;
    
    public LichSuDiemView(int maKH) {
        this.maKH = maKH;
        this.logDiemService = new LogDiemService();
        this.khachHangService = new KhachHangService();
        initComponents();
        setupLayout();
        loadData();
        loadStatistics();
    }
    
    private void initComponents() {
        // Filter
        cboLoaiDiem = new JComboBox<>(new String[]{"Tất cả", "Cộng điểm", "Sử dụng điểm", "Điều chỉnh"});
        cboLoaiDiem.addActionListener(e -> filterTable());
        
        // Buttons
        btnRefresh = new JButton("Làm mới");
        btnRefresh.addActionListener(e -> {
            loadData();
            loadStatistics();
        });
        
        btnXuatExcel = new JButton("Xuất Excel");
        btnXuatExcel.addActionListener(e -> xuatExcel());
        
        // Statistics labels
        lblTongDiem = new JLabel("Tổng điểm hiện tại: Đang tính...");
        lblTongDiem.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTongDiem.setForeground(new Color(0, 102, 204));
        
        lblDiemCongThang = new JLabel("Điểm cộng tháng này: Đang tính...");
        lblDiemCongThang.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        lblDiemSuDungThang = new JLabel("Điểm sử dụng tháng này: Đang tính...");
        lblDiemSuDungThang.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        // Table
        String[] columns = {
            "Thời gian", "Loại điểm", "Số điểm", "Lý do", "Mã HĐ"
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
        
        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(250);
        table.getColumnModel().getColumn(4).setPreferredWidth(80);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Top panel: Statistics
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        statsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Thống kê điểm"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JPanel tongDiemPanel = new JPanel(new BorderLayout());
        tongDiemPanel.add(lblTongDiem, BorderLayout.CENTER);
        
        JPanel diemCongPanel = new JPanel(new BorderLayout());
        diemCongPanel.add(lblDiemCongThang, BorderLayout.CENTER);
        
        JPanel diemSuDungPanel = new JPanel(new BorderLayout());
        diemSuDungPanel.add(lblDiemSuDungThang, BorderLayout.CENTER);
        
        statsPanel.add(tongDiemPanel);
        statsPanel.add(diemCongPanel);
        statsPanel.add(diemSuDungPanel);
        
        // Middle panel: Filter and buttons
        JPanel filterPanel = new JPanel(new BorderLayout());
        
        JPanel leftFilter = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftFilter.add(new JLabel("Loại điểm:"));
        leftFilter.add(cboLoaiDiem);
        leftFilter.add(btnRefresh);
        
        JPanel rightFilter = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightFilter.add(btnXuatExcel);
        
        filterPanel.add(leftFilter, BorderLayout.WEST);
        filterPanel.add(rightFilter, BorderLayout.EAST);
        
        // Center panel: Table
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Add components
        add(statsPanel, BorderLayout.NORTH);
        add(filterPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
    }
    
    private void loadData() {
        try {
            List<LogDiem> logList = logDiemService.layLogTheoKhachHang(maKH);
            displayLogDiem(logList);
        } catch (Exception e) {
            showError("Lỗi khi tải lịch sử điểm: " + e.getMessage());
        }
    }
    
    private void loadStatistics() {
        try {
            // Load customer info for total points
            KhachHang kh = khachHangService.timKhachHang(maKH);
            if (kh != null) {
                lblTongDiem.setText("Tổng điểm hiện tại: " + kh.getDiemLoyalty() + " điểm");
            }
            
            // Calculate monthly points
            int diemCongThang = logDiemService.tinhDiemCongThang(maKH);
            int diemSuDungThang = logDiemService.tinhDiemSuDungThang(maKH);
            
            lblDiemCongThang.setText("Điểm cộng tháng này: " + diemCongThang + " điểm");
            lblDiemSuDungThang.setText("Điểm sử dụng tháng này: " + diemSuDungThang + " điểm");
            
        } catch (Exception e) {
            showError("Lỗi khi tải thống kê: " + e.getMessage());
        }
    }
    
    private void displayLogDiem(List<LogDiem> logList) {
        tableModel.setRowCount(0);
        
        if (logList != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            
            for (LogDiem log : logList) {
                Object[] row = {
                    log.getNgayGhiNhan().format(formatter),
                    log.getLoaiDiem(),
                    formatSoDiem(log.getSoDiem()),
                    log.getLyDo(),
                    log.getMaHoaDon() != null ? "HD" + String.format("%06d", log.getMaHoaDon()) : ""
                };
                tableModel.addRow(row);
            }
        }
    }
    
    private String formatSoDiem(int soDiem) {
        if (soDiem > 0) {
            return "+" + soDiem;
        } else if (soDiem < 0) {
            return String.valueOf(soDiem);
        } else {
            return "0";
        }
    }
    
    private void filterTable() {
        try {
            String loaiDiem = (String) cboLoaiDiem.getSelectedItem();
            List<LogDiem> allLogs = logDiemService.layLogTheoKhachHang(maKH);
            
            if ("Tất cả".equals(loaiDiem)) {
                displayLogDiem(allLogs);
            } else {
                List<LogDiem> filteredLogs = new ArrayList<>();
                for (LogDiem log : allLogs) {
                    if (loaiDiem.equals(log.getLoaiDiem())) {
                        filteredLogs.add(log);
                    }
                }
                displayLogDiem(filteredLogs);
            }
        } catch (Exception e) {
            showError("Lỗi khi lọc dữ liệu: " + e.getMessage());
        }
    }
    
    private void xuatExcel() {
        JOptionPane.showMessageDialog(this,
            "Chức năng xuất Excel đang được phát triển",
            "Thông báo",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}