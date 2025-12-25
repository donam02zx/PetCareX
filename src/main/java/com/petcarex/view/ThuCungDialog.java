package com.petcarex.view;

import com.petcarex.controller.ThuCungController;
import com.petcarex.model.ThuCung;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ThuCungDialog extends JDialog {
    private ThuCungController controller;
    private ThuCung thuCung;
    private boolean isEditMode;
    private boolean isNhanVien;
    
    // Form fields
    private JTextField txtTenThuCung;
    private JComboBox<String> cboLoai;
    private JTextField txtGiong;
    private JTextField txtNgaySinh;
    private JComboBox<String> cboGioiTinh;
    private JTextArea txtTinhTrang;
    private JTextField txtMaKH; // Chỉ hiển thị cho nhân viên
    private JTextField txtTenChu; // Chỉ hiển thị cho nhân viên
    
    public ThuCungDialog(JFrame parent, ThuCungController controller, ThuCung thuCung, boolean isNhanVien) {
        super(parent, true);
        this.controller = controller;
        this.thuCung = thuCung;
        this.isEditMode = (thuCung != null);
        this.isNhanVien = isNhanVien;
        
        setTitle(isEditMode ? "Chỉnh sửa thú cưng" : "Thêm thú cưng mới");
        initComponents();
        setupLayout();
        setupListeners();
        
        if (isEditMode) {
            loadData();
        }
        
        setSize(500, isNhanVien ? 550 : 500);
        setLocationRelativeTo(parent);
        setResizable(false);
    }
    
    private void initComponents() {
        Font font = new Font("Segoe UI", Font.PLAIN, 12);
        
        txtTenThuCung = new JTextField(20);
        txtTenThuCung.setFont(font);
        
        cboLoai = new JComboBox<>(new String[]{"Chó", "Mèo", "Chim", "Thỏ", "Hamster", "Bò sát", "Khác"});
        cboLoai.setFont(font);
        
        txtGiong = new JTextField(20);
        txtGiong.setFont(font);
        txtGiong.setToolTipText("Ví dụ: Poodle, Golden Retriever, Mèo Anh lông ngắn...");
        
        txtNgaySinh = new JTextField(20);
        txtNgaySinh.setFont(font);
        txtNgaySinh.setText(LocalDate.now().minusMonths(6).format(DateTimeFormatter.ISO_DATE));
        txtNgaySinh.setToolTipText("Định dạng: YYYY-MM-DD");
        
        cboGioiTinh = new JComboBox<>(new String[]{"Đực", "Cái", "Không xác định"});
        cboGioiTinh.setFont(font);
        
        txtTinhTrang = new JTextArea(3, 20);
        txtTinhTrang.setFont(font);
        txtTinhTrang.setLineWrap(true);
        txtTinhTrang.setWrapStyleWord(true);
        
        if (isNhanVien) {
            txtMaKH = new JTextField(10);
            txtMaKH.setFont(font);
            
            txtTenChu = new JTextField(20);
            txtTenChu.setFont(font);
            txtTenChu.setEditable(false);
        }
    }
    
    private void setupLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Title
        JLabel lblTitle = new JLabel(isEditMode ? "CHỈNH SỬA THÚ CƯNG" : "THÊM THÚ CƯNG MỚI", 
                                    SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(new Color(76, 175, 80));
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin thú cưng"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 0;
        
        // Tên thú cưng
        addFormRow(formPanel, gbc, row++, "Tên thú cưng *:", txtTenThuCung);
        
        // Loại
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Loài *:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        formPanel.add(cboLoai, gbc);
        row++;
        
        // Giống
        addFormRow(formPanel, gbc, row++, "Giống:", txtGiong);
        
        // Ngày sinh
        addFormRow(formPanel, gbc, row++, "Ngày sinh *:", txtNgaySinh);
        
        // Giới tính
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Giới tính *:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        formPanel.add(cboGioiTinh, gbc);
        row++;
        
        // Tình trạng sức khỏe
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Tình trạng sức khỏe:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(new JScrollPane(txtTinhTrang), gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        row++;
        
        // Thông tin chủ (chỉ cho nhân viên)
        if (isNhanVien) {
            JPanel ownerPanel = new JPanel(new GridBagLayout());
            ownerPanel.setBorder(BorderFactory.createTitledBorder("Thông tin chủ"));
            
            GridBagConstraints gbcOwner = new GridBagConstraints();
            gbcOwner.insets = new Insets(5, 5, 5, 5);
            gbcOwner.fill = GridBagConstraints.HORIZONTAL;
            gbcOwner.anchor = GridBagConstraints.WEST;
            
            int ownerRow = 0;
            addFormRow(ownerPanel, gbcOwner, ownerRow++, "Mã KH *:", txtMaKH);
            addFormRow(ownerPanel, gbcOwner, ownerRow++, "Tên chủ:", txtTenChu);
            
            formPanel.add(ownerPanel, gbc);
        }
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        JButton btnSave = new JButton(isEditMode ? "Cập nhật" : "Lưu");
        btnSave.setBackground(new Color(76, 175, 80));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSave.setPreferredSize(new Dimension(100, 30));
        btnSave.addActionListener(e -> saveThuCung());
        
        JButton btnCancel = new JButton("Hủy");
        btnCancel.setBackground(new Color(244, 67, 54));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setPreferredSize(new Dimension(100, 30));
        btnCancel.addActionListener(e -> dispose());
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        
        // Assemble
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(formPanel);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(buttonPanel);
        
        mainPanel.add(lblTitle, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        setContentPane(mainPanel);
        getRootPane().setDefaultButton(btnSave);
    }
    
    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        panel.add(field, gbc);
    }
    
    private void setupListeners() {
        // Auto-select date when focusing
        txtNgaySinh.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNgaySinh.selectAll();
            }
        });
    }
    
    private void loadData() {
        if (thuCung == null) return;
        
        txtTenThuCung.setText(thuCung.getTenThuCung());
        cboLoai.setSelectedItem(thuCung.getLoai());
        txtGiong.setText(thuCung.getGiong());
        
        if (thuCung.getNgaySinh() != null) {
            txtNgaySinh.setText(thuCung.getNgaySinh().format(DateTimeFormatter.ISO_DATE));
        }
        
        cboGioiTinh.setSelectedItem(thuCung.getGioiTinh());
        txtTinhTrang.setText(thuCung.getTinhTrangSucKhoe());
        
        if (isNhanVien) {
            txtMaKH.setText(String.valueOf(thuCung.getMaKH()));
            txtTenChu.setText(thuCung.getTenChu() != null ? thuCung.getTenChu() : "");
        }
    }
    
    private void saveThuCung() {
        if (!validateFields()) {
            return;
        }
        
        try {
            ThuCung tc;
            if (isEditMode) {
                tc = thuCung;
            } else {
                tc = new ThuCung();
            }
            
            tc.setTenThuCung(txtTenThuCung.getText().trim());
            tc.setLoai((String) cboLoai.getSelectedItem());
            tc.setGiong(txtGiong.getText().trim());
            tc.setNgaySinh(LocalDate.parse(txtNgaySinh.getText().trim()));
            tc.setGioiTinh((String) cboGioiTinh.getSelectedItem());
            tc.setTinhTrangSucKhoe(txtTinhTrang.getText().trim());
            
            if (isNhanVien) {
                int maKH = Integer.parseInt(txtMaKH.getText().trim());
                tc.setMaKH(maKH);
            }
            
            if (isEditMode) {
                controller.updateThuCung(tc);
            } else {
                controller.addThuCung(tc);
            }
            
            dispose();
            
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                "Ngày sinh không đúng định dạng. Vui lòng nhập YYYY-MM-DD",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            txtNgaySinh.requestFocus();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Mã KH phải là số",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            txtMaKH.requestFocus();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Lỗi: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean validateFields() {
        if (txtTenThuCung.getText().trim().isEmpty()) {
            showFieldError(txtTenThuCung, "Vui lòng nhập tên thú cưng");
            return false;
        }
        
        if (txtNgaySinh.getText().trim().isEmpty()) {
            showFieldError(txtNgaySinh, "Vui lòng nhập ngày sinh");
            return false;
        }
        
        try {
            LocalDate ngaySinh = LocalDate.parse(txtNgaySinh.getText().trim());
            if (ngaySinh.isAfter(LocalDate.now())) {
                showFieldError(txtNgaySinh, "Ngày sinh không thể ở tương lai");
                return false;
            }
        } catch (DateTimeParseException e) {
            showFieldError(txtNgaySinh, "Ngày sinh không đúng định dạng (YYYY-MM-DD)");
            return false;
        }
        
        if (isNhanVien) {
            if (txtMaKH.getText().trim().isEmpty()) {
                showFieldError(txtMaKH, "Vui lòng nhập mã khách hàng");
                return false;
            }
            
            try {
                int maKH = Integer.parseInt(txtMaKH.getText().trim());
                if (maKH <= 0) {
                    showFieldError(txtMaKH, "Mã KH phải là số dương");
                    return false;
                }
            } catch (NumberFormatException e) {
                showFieldError(txtMaKH, "Mã KH phải là số");
                return false;
            }
        }
        
        return true;
    }
    
    private void showFieldError(JTextField field, String message) {
        JOptionPane.showMessageDialog(this,
            message,
            "Lỗi nhập liệu",
            JOptionPane.ERROR_MESSAGE);
        field.requestFocus();
        field.selectAll();
    }
}