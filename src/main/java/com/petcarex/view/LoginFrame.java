package com.petcarex.view;

import com.petcarex.controller.LoginController;
import com.petcarex.model.KhachHang;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class LoginFrame extends JFrame {
    private LoginController controller;
    
    // UI Components
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cboRole;
    private JButton btnLogin;
    private JButton btnRegister;
    private JButton btnExit;
    private JLabel lblStatus;
    
    public LoginFrame() {
        controller = new LoginController();
        controller.setView(this);
        
        // Set Unicode font
        setUIFont();
        
        initComponents();
        setupLayout();
        setupListeners();
        
        setTitle("PetCareX - Đăng Nhập Hệ Thống");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500); // Tăng kích thước
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    // Method to set Unicode font for Vietnamese
    private void setUIFont() {
        try {
            // Try to use system font that supports Vietnamese
            Font vietnameseFont = null;
            String[] fontNames = {"Segoe UI", "Arial", "Tahoma", "Microsoft Sans Serif", "Dialog"};
            
            for (String fontName : fontNames) {
                Font font = new Font(fontName, Font.PLAIN, 12);
                if (font.canDisplay('Ắ') && font.canDisplay('ế')) {
                    vietnameseFont = font;
                    break;
                }
            }
            
            if (vietnameseFont != null) {
                UIManager.put("Button.font", vietnameseFont);
                UIManager.put("Label.font", vietnameseFont);
                UIManager.put("TextField.font", vietnameseFont);
                UIManager.put("PasswordField.font", vietnameseFont);
                UIManager.put("ComboBox.font", vietnameseFont);
                UIManager.put("TitledBorder.font", vietnameseFont);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void initComponents() {
        // Username field
        txtUsername = new JTextField(20);
        txtUsername.setToolTipText("Nhập email hoặc tên đăng nhập");
        
        // Password field
        txtPassword = new JPasswordField(20);
        txtPassword.setToolTipText("Nhập mật khẩu (CCCD)");
        
        // Role combo box
        String[] roles = {"Khách hàng", "Nhân viên", "Bác sĩ", "Quản lý"};
        cboRole = new JComboBox<>(roles);
        cboRole.setSelectedIndex(0);
        
        // Buttons
        btnLogin = new JButton("Đăng Nhập");
        btnLogin.setBackground(new Color(76, 175, 80));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        btnRegister = new JButton("Đăng Ký Tài Khoản");
        btnRegister.setBackground(new Color(33, 150, 243));
        btnRegister.setForeground(Color.WHITE);
        
        btnExit = new JButton("Thoát");
        btnExit.setBackground(new Color(244, 67, 54));
        btnExit.setForeground(Color.WHITE);
        
        // Status label
        lblStatus = new JLabel(" ", SwingConstants.CENTER);
        lblStatus.setForeground(Color.RED);
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    }
    
    private void setupLayout() {
        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.WHITE);
        
        // ===== HEADER PANEL =====
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitle = new JLabel("PETCAREX", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitle.setForeground(new Color(76, 175, 80));
        
        JLabel lblSubtitle = new JLabel("Hệ Thống Quản Lý Trung Tâm Chăm Sóc Thú Cưng", SwingConstants.CENTER);
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitle.setForeground(Color.GRAY);
        
        headerPanel.add(lblTitle);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(lblSubtitle);
        
        // ===== CONTENT PANEL =====
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        
        // ===== LEFT PANEL (Login Form) =====
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Đăng nhập hệ thống"
        ));
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Tên đăng nhập:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 1.0;
        formPanel.add(txtUsername, gbc);
        
        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Mật khẩu:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weightx = 1.0;
        formPanel.add(txtPassword, gbc);
        
        // Role
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Vai trò:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.weightx = 1.0;
        formPanel.add(cboRole, gbc);
        
        // Login button panel
        JPanel loginButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        loginButtonPanel.setBackground(Color.WHITE);
        loginButtonPanel.add(btnLogin);
        
        leftPanel.add(formPanel, BorderLayout.CENTER);
        leftPanel.add(loginButtonPanel, BorderLayout.SOUTH);
        
        // ===== RIGHT PANEL (Register Info) =====
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBackground(new Color(240, 245, 240));
        rightPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Thông tin đăng ký"
        ));
        
        // Register info text
        JTextArea txtRegisterInfo = new JTextArea();
        txtRegisterInfo.setText("Chưa có tài khoản?\n\n" +
                              "Nhấn nút 'Đăng Ký Tài Khoản' để:\n" +
                              "• Tạo tài khoản khách hàng\n" +
                              "• Quản lý thú cưng của bạn\n" +
                              "• Đặt lịch khám trực tuyến\n" +
                              "• Tích điểm loyalty\n\n" +
                              "Mật khẩu mặc định là CCCD của bạn");
        txtRegisterInfo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtRegisterInfo.setBackground(new Color(240, 245, 240));
        txtRegisterInfo.setEditable(false);
        txtRegisterInfo.setLineWrap(true);
        txtRegisterInfo.setWrapStyleWord(true);
        txtRegisterInfo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Register buttons panel
        JPanel registerButtonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        registerButtonPanel.setBackground(new Color(240, 245, 240));
        registerButtonPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        
        JPanel registerBtnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        registerBtnPanel.setBackground(new Color(240, 245, 240));
        registerBtnPanel.add(btnRegister);
        
        JPanel exitBtnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        exitBtnPanel.setBackground(new Color(240, 245, 240));
        exitBtnPanel.add(btnExit);
        
        registerButtonPanel.add(registerBtnPanel);
        registerButtonPanel.add(exitBtnPanel);
        
        rightPanel.add(txtRegisterInfo, BorderLayout.CENTER);
        rightPanel.add(registerButtonPanel, BorderLayout.SOUTH);
        
        // Add left and right panels to content panel
        contentPanel.add(leftPanel);
        contentPanel.add(rightPanel);
        
        // ===== STATUS PANEL =====
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(Color.WHITE);
        statusPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        statusPanel.add(lblStatus, BorderLayout.CENTER);
        
        // ===== ASSEMBLE MAIN PANEL =====
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(statusPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
        getRootPane().setDefaultButton(btnLogin);
    }
    
    private void setupListeners() {
        btnLogin.addActionListener(e -> performLogin());
        btnRegister.addActionListener(e -> showRegisterDialog());
        btnExit.addActionListener(e -> confirmExit());
        txtPassword.addActionListener(e -> performLogin());
    }
    
    private void performLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String role = (String) cboRole.getSelectedItem();
        
        if (username.isEmpty()) {
            showError("Vui lòng nhập tên đăng nhập");
            txtUsername.requestFocus();
            return;
        }
        
        if (password.isEmpty()) {
            showError("Vui lòng nhập mật khẩu");
            txtPassword.requestFocus();
            return;
        }
        
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        btnLogin.setEnabled(false);
        btnRegister.setEnabled(false);
        
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() {
                return controller.login(username, password, role);
            }
            
            @Override
            protected void done() {
                setCursor(Cursor.getDefaultCursor());
                btnLogin.setEnabled(true);
                btnRegister.setEnabled(true);
                
                try {
                    if (!get()) {
                        txtPassword.setText("");
                        txtPassword.requestFocus();
                    }
                } catch (Exception e) {
                    showError("Lỗi đăng nhập: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }
    
    private void showRegisterDialog() {
        RegisterDialog registerDialog = new RegisterDialog(this, controller);
        registerDialog.setVisible(true);
    }
    
    private void confirmExit() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc muốn thoát ứng dụng?",
            "Xác nhận thoát",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    
    public void showError(String message) {
        lblStatus.setText("❌ " + message);
        lblStatus.setForeground(Color.RED);
        
        Timer timer = new Timer(5000, e -> lblStatus.setText(" "));
        timer.setRepeats(false);
        timer.start();
        
        Toolkit.getDefaultToolkit().beep();
    }
    
    public void showSuccess(String message) {
        lblStatus.setText("✅ " + message);
        lblStatus.setForeground(new Color(0, 150, 0));
        
        Timer timer = new Timer(3000, e -> lblStatus.setText(" "));
        timer.setRepeats(false);
        timer.start();
    }
    
    public void clearForm() {
        txtUsername.setText("");
        txtPassword.setText("");
        txtUsername.requestFocus();
    }
    
    public static void main(String[] args) {
        // Force UTF-8 encoding
        System.setProperty("file.encoding", "UTF-8");
        
        // Set proper look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Set better font for Vietnamese
            Font font = new Font("Segoe UI", Font.PLAIN, 12);
            UIManager.put("Button.font", font);
            UIManager.put("Label.font", font);
            UIManager.put("TextField.font", font);
            UIManager.put("PasswordField.font", font);
            UIManager.put("ComboBox.font", font);
            UIManager.put("TextArea.font", font);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            LoginFrame frame = new LoginFrame();
            frame.setVisible(true);
        });
    }
}