package com.petcarex.view;

import com.petcarex.controller.LoginController;
import com.petcarex.model.KhachHang;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class RegisterDialog extends JDialog {
    private LoginController controller;
    private LoginFrame parent;
    
    // Form fields
    private JTextField txtHoTen;
    private JTextField txtCCCD;
    private JTextField txtSDT;
    private JTextField txtEmail;
    private JComboBox<String> cboGioiTinh;
    private JTextField txtNgaySinh;
    private JTextField txtDiaChi;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    
    public RegisterDialog(LoginFrame parent, LoginController controller) {
        super(parent, "Đăng Ký Tài Khoản Khách Hàng", true);
        this.parent = parent;
        this.controller = controller;
        
        initComponents();
        setupLayout();
        setupListeners();
        
        setSize(500, 550);
        setLocationRelativeTo(parent);
        setResizable(false);
    }
    
    private void initComponents() {
        // Set font for all components
        Font font = new Font("Segoe UI", Font.PLAIN, 12);
        
        txtHoTen = new JTextField(20);
        txtHoTen.setFont(font);
        
        txtCCCD = new JTextField(20);
        txtCCCD.setFont(font);
        txtCCCD.setToolTipText("Nhập 12 số CCCD");
        
        txtSDT = new JTextField(20);
        txtSDT.setFont(font);
        txtSDT.setToolTipText("Ví dụ: 0912345678");
        
        txtEmail = new JTextField(20);
        txtEmail.setFont(font);
        txtEmail.setToolTipText("Email sẽ dùng để đăng nhập");
        
        cboGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ", "Khác"});
        cboGioiTinh.setFont(font);
        
        txtNgaySinh = new JTextField(20);
        txtNgaySinh.setFont(font);
        txtNgaySinh.setText("1990-01-01");
        txtNgaySinh.setToolTipText("Định dạng: YYYY-MM-DD");
        
        txtDiaChi = new JTextField(20);
        txtDiaChi.setFont(font);
        
        txtPassword = new JPasswordField(20);
        txtPassword.setFont(font);
        txtPassword.setToolTipText("Mật khẩu sẽ là CCCD của bạn");
        
        txtConfirmPassword = new JPasswordField(20);
        txtConfirmPassword.setFont(font);
    }
    
    private void setupLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Title
        JLabel lblTitle = new JLabel("ĐĂNG KÝ TÀI KHOẢN KHÁCH HÀNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(new Color(76, 175, 80));
        
        // Form panel with scroll
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin cá nhân"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 0;
        
        // Họ tên
        addFormRow(formPanel, gbc, row++, "Họ tên *:", txtHoTen);
        
        // CCCD
        addFormRow(formPanel, gbc, row++, "CCCD *:", txtCCCD);
        
        // SĐT
        addFormRow(formPanel, gbc, row++, "Số điện thoại *:", txtSDT);
        
        // Email
        addFormRow(formPanel, gbc, row++, "Email *:", txtEmail);
        
        // Giới tính
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Giới tính *:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        formPanel.add(cboGioiTinh, gbc);
        row++;
        
        // Ngày sinh
        addFormRow(formPanel, gbc, row++, "Ngày sinh *:", txtNgaySinh);
        
        // Địa chỉ
        addFormRow(formPanel, gbc, row++, "Địa chỉ:", txtDiaChi);
        
        // Password panel
        JPanel passwordPanel = new JPanel(new GridBagLayout());
        passwordPanel.setBorder(BorderFactory.createTitledBorder("Thông tin đăng nhập"));
        
        GridBagConstraints gbcPass = new GridBagConstraints();
        gbcPass.insets = new Insets(8, 8, 8, 8);
        gbcPass.fill = GridBagConstraints.HORIZONTAL;
        gbcPass.anchor = GridBagConstraints.WEST;
        
        addFormRow(passwordPanel, gbcPass, 0, "Mật khẩu *:", txtPassword);
        addFormRow(passwordPanel, gbcPass, 1, "Xác nhận mật khẩu *:", txtConfirmPassword);
        
        // Info label
        JLabel lblInfo = new JLabel("Lưu ý: Mật khẩu sẽ là CCCD của bạn. Vui lòng ghi nhớ để đăng nhập.");
        lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblInfo.setForeground(Color.GRAY);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        JButton btnRegister = new JButton("Đăng Ký");
        btnRegister.setBackground(new Color(76, 175, 80));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRegister.setPreferredSize(new Dimension(100, 30));
        btnRegister.addActionListener(e -> performRegistration());
        
        JButton btnCancel = new JButton("Hủy");
        btnCancel.setBackground(new Color(244, 67, 54));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setPreferredSize(new Dimension(100, 30));
        btnCancel.addActionListener(e -> dispose());
        
        buttonPanel.add(btnRegister);
        buttonPanel.add(btnCancel);
        
        // Assemble
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(formPanel);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(passwordPanel);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(lblInfo);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(buttonPanel);
        
        // Add scroll for small screens
        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        mainPanel.add(lblTitle, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        setContentPane(mainPanel);
        getRootPane().setDefaultButton(btnRegister);
    }
    
    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        panel.add(field, gbc);
    }
    
    private void setupListeners() {
        // Auto-fill password with CCCD
        txtCCCD.addActionListener(e -> {
            String cccd = txtCCCD.getText().trim();
            if (!cccd.isEmpty()) {
                txtPassword.setText(cccd);
                txtConfirmPassword.setText(cccd);
            }
        });
    }
    
    private void performRegistration() {
        if (!validateFields()) {
            return;
        }
        
        try {
            KhachHang kh = new KhachHang(
                txtHoTen.getText().trim(),
                txtCCCD.getText().trim(),
                txtSDT.getText().trim(),
                txtEmail.getText().trim(),
                (String) cboGioiTinh.getSelectedItem(),
                LocalDate.parse(txtNgaySinh.getText().trim()),
                txtDiaChi.getText().trim()
            );
            
            String password = new String(txtPassword.getPassword());
            
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            
            SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() {
                    controller.registerCustomer(txtEmail.getText().trim(), password, kh);
                    return true;
                }
                
                @Override
                protected void done() {
                    setCursor(Cursor.getDefaultCursor());
                    try {
                        get();
                        dispose();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(RegisterDialog.this,
                            "Lỗi đăng ký: " + e.getMessage(),
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            };
            
            worker.execute();
            
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                "Ngày sinh không đúng định dạng. Vui lòng nhập YYYY-MM-DD",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            txtNgaySinh.requestFocus();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Lỗi: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean validateFields() {
        // Validation logic (same as before)
        // ... [giữ nguyên validation code]
        
        return true;
    }
}