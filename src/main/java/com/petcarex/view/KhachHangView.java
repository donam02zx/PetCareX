package com.petcarex.view;

import com.petcarex.controller.KhachHangController;
import com.petcarex.model.KhachHang;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class KhachHangView extends JPanel {
    private KhachHangController controller;
    
    // UI Components
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JButton btnSearch, btnAdd, btnEdit, btnDelete;
    
    public KhachHangView(KhachHangController controller) {
        this.controller = controller;
        initComponents();
        setupLayout();
        loadData();
    }
    
    private void initComponents() {
        // Search panel
        txtSearch = new JTextField(20);
        btnSearch = new JButton("Tìm kiếm");
        btnSearch.addActionListener(e -> {
            controller.searchKhachHang(txtSearch.getText());
        });
        
        // Buttons
        btnAdd = new JButton("Thêm mới");
        btnAdd.addActionListener(e -> showAddDialog());
        
        btnEdit = new JButton("Chỉnh sửa");
        btnEdit.addActionListener(e -> showEditDialog());
        
        btnDelete = new JButton("Xóa");
        btnDelete.addActionListener(e -> deleteSelected());
        
        // Table
        String[] columns = {
            "Mã KH", "Họ tên", "CCCD", "SĐT", "Email", 
            "Giới tính", "Ngày sinh", "Cấp độ", "Điểm"
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
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Top panel: Search and buttons
        JPanel topPanel = new JPanel(new BorderLayout());
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Tìm kiếm:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        
        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Center panel: Table
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Add components
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    public void loadData() {
        List<KhachHang> khList = controller.getAllKhachHang();
        displayKhachHang(khList);
    }
    
    public void displayKhachHang(List<KhachHang> khList) {
        tableModel.setRowCount(0);
        
        if (khList != null) {
            for (KhachHang kh : khList) {
                Object[] row = {
                    kh.getMaKH(),
                    kh.getHoTen(),
                    kh.getCccd(),
                    kh.getSoDienThoai(),
                    kh.getEmail(),
                    kh.getGioiTinh(),
                    kh.getNgaySinh().toString(),
                    kh.getCapDoThanhVien(),
                    kh.getDiemLoyalty()
                };
                tableModel.addRow(row);
            }
        }
    }
    
    public void refreshTable() {
        loadData();
    }
    
    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm khách hàng", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 500);
        dialog.setLocationRelativeTo(this);
        
        // Form fields
        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        
        JTextField txtHoTen = new JTextField();
        JTextField txtCCCD = new JTextField();
        JTextField txtSDT = new JTextField();
        JTextField txtEmail = new JTextField();
        JComboBox<String> cboGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ", "Khác"});
        JTextField txtNgaySinh = new JTextField(); // Nên dùng DatePicker
        JTextField txtDiaChi = new JTextField();
        
        formPanel.add(new JLabel("Họ tên:"));
        formPanel.add(txtHoTen);
        formPanel.add(new JLabel("CCCD:"));
        formPanel.add(txtCCCD);
        formPanel.add(new JLabel("SĐT:"));
        formPanel.add(txtSDT);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(txtEmail);
        formPanel.add(new JLabel("Giới tính:"));
        formPanel.add(cboGioiTinh);
        formPanel.add(new JLabel("Ngày sinh (yyyy-mm-dd):"));
        formPanel.add(txtNgaySinh);
        formPanel.add(new JLabel("Địa chỉ:"));
        formPanel.add(txtDiaChi);
        
        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton btnSave = new JButton("Lưu");
        JButton btnCancel = new JButton("Hủy");
        
        btnSave.addActionListener(e -> {
            try {
                KhachHang kh = new KhachHang(
                    txtHoTen.getText(),
                    txtCCCD.getText(),
                    txtSDT.getText(),
                    txtEmail.getText(),
                    (String) cboGioiTinh.getSelectedItem(),
                    java.time.LocalDate.parse(txtNgaySinh.getText()),
                    txtDiaChi.getText()
                );
                controller.addKhachHang(kh);
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnCancel.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void showEditDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showError("Vui lòng chọn một khách hàng để chỉnh sửa");
            return;
        }
        
        int maKH = (int) tableModel.getValueAt(selectedRow, 0);
        // TODO: Implement edit dialog
        JOptionPane.showMessageDialog(this, "Chức năng chỉnh sửa đang phát triển...", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void deleteSelected() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showError("Vui lòng chọn một khách hàng để xóa");
            return;
        }
        
        int maKH = (int) tableModel.getValueAt(selectedRow, 0);
        controller.deleteKhachHang(maKH);
    }
    
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
    
    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }
}