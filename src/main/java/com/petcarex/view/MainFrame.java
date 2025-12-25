package com.petcarex.view;

import com.petcarex.model.KhachHang;
import com.petcarex.model.NhanVien;
import com.petcarex.controller.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.time.format.DateTimeFormatter;

public class MainFrame extends JFrame {
    private String userRole;
    private Object userInfo;
    
    // UI Components
    private JTabbedPane tabbedPane;
    private JLabel lblWelcome;
    private JLabel lblUserInfo;
    
    // Controllers
    private KhachHangController khController;
    
    public MainFrame(String role, Object userInfo) {
        this.userRole = role;
        this.userInfo = userInfo;
        
        initializeControllers();
        initComponents();
        setupLayout();
        setupPermissions();
        
        setTitle("PetCareX - " + getWelcomeTitle());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1200, 700));
        setLocationRelativeTo(null);
        
        // Add resize listener to fix layout issues
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Revalidate layout when window is resized
                revalidate();
                repaint();
            }
        });
    }
    
    private void initializeControllers() {
        khController = new KhachHangController();
    }
    
    private String getWelcomeTitle() {
        String name = "Người dùng";
        
        if (userInfo instanceof KhachHang) {
            name = ((KhachHang) userInfo).getHoTen();
        } else if (userInfo instanceof NhanVien) {
            name = ((NhanVien) userInfo).getHoTen();
        }
        
        return "Xin chào " + name + " (" + userRole + ")";
    }
    
    private void initComponents() {
        // Welcome labels
        lblWelcome = new JLabel(getWelcomeTitle(), SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblWelcome.setForeground(new Color(76, 175, 80));
        
        lblUserInfo = new JLabel("", SwingConstants.CENTER);
        lblUserInfo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUserInfo.setForeground(Color.DARK_GRAY);
        
        updateUserInfo();
        
        // Tabbed pane for different modules
        tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }
    
    private void updateUserInfo() {
        if (userInfo instanceof KhachHang) {
            KhachHang kh = (KhachHang) userInfo;
            String info = String.format("Mã KH: %d | Điểm loyalty: %d | Cấp độ: %s | Ngày đăng ký: %s",
                kh.getMaKH(), kh.getDiemLoyalty(), kh.getCapDoThanhVien(),
                kh.getNgayDangKy().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            lblUserInfo.setText(info);
            
        } else if (userInfo instanceof NhanVien) {
            NhanVien nv = (NhanVien) userInfo;
            String info = String.format("Mã NV: %d | Chức vụ: %s | Mức lương: %,.0f VND",
                nv.getMaNV(), nv.getChucVu(), nv.getLuongCoBan());
            lblUserInfo.setText(info);
        }
    }
    
    private void setupLayout() {
        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Tạo các phần riêng biệt
        JPanel headerPanel = createHeaderPanel();
        JPanel userInfoPanel = createUserInfoPanel();
        
        // Tạo panel trên cùng chứa header và user info
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(headerPanel, BorderLayout.NORTH);
        topPanel.add(userInfoPanel, BorderLayout.CENTER);
        
        // Tạo panel chính chứa tabbedPane
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // Thêm tất cả vào main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        setContentPane(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        
        // Left side: Logo and title
        JPanel leftHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftHeader.setBackground(Color.WHITE);
        
        // Sử dụng emoji thay vì icon file
        JLabel lblLogo = new JLabel("🐾");
        lblLogo.setFont(new Font("Segoe UI", Font.PLAIN, 36));
        
        JLabel lblAppTitle = new JLabel("PETCAREX");
        lblAppTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblAppTitle.setForeground(new Color(76, 175, 80));
        
        leftHeader.add(lblLogo);
        leftHeader.add(Box.createHorizontalStrut(10));
        leftHeader.add(lblAppTitle);
        
        // Right side: User info and logout
        JPanel rightHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightHeader.setBackground(Color.WHITE);
        
        JButton btnLogout = new JButton("Đăng Xuất");
        btnLogout.addActionListener(e -> logout());
        
        rightHeader.add(btnLogout);
        
        // Add to header
        headerPanel.add(leftHeader, BorderLayout.WEST);
        headerPanel.add(rightHeader, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createUserInfoPanel() {
        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.setBackground(new Color(240, 245, 240));
        userPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Panel chứa thông tin chào mừng và thông tin người dùng
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(new Color(240, 245, 240));
        
        JPanel welcomePanel = new JPanel(new GridLayout(2, 1));
        welcomePanel.setBackground(new Color(240, 245, 240));
        welcomePanel.add(lblWelcome);
        welcomePanel.add(lblUserInfo);
        
        infoPanel.add(welcomePanel, BorderLayout.CENTER);
        
        // Logout button on the right
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.setBackground(new Color(240, 245, 240));
        JButton btnQuickLogout = new JButton("Đăng xuất");
        btnQuickLogout.addActionListener(e -> logout());
        logoutPanel.add(btnQuickLogout);
        infoPanel.add(logoutPanel, BorderLayout.EAST);
        
        userPanel.add(infoPanel, BorderLayout.CENTER);
        
        return userPanel;
    }
    
    private void setupPermissions() {
        // Clear all tabs first
        tabbedPane.removeAll();
        
        // Add Dashboard tab FIRST for all users
        addDashboardTab();
        
        // Then add role-specific tabs
        switch (userRole) {
            case "Khách hàng":
                setupKhachHangTabs();
                break;
            case "Nhân viên":
                setupNhanVienTabs();
                break;
            case "Bác sĩ":
                setupBacSiTabs();
                break;
            case "Quản lý":
                setupQuanLyTabs();
                break;
            default:
                setupDefaultTabs();
        }
    }
    
    private void addDashboardTab() {
        JPanel dashboardPanel = createDashboardPanel();
        // Wrap in scroll pane for better layout management
        JScrollPane scrollPane = new JScrollPane(dashboardPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        tabbedPane.insertTab("📊 Dashboard", null, scrollPane, "Trang chủ tổng quan", 0);
        tabbedPane.setSelectedIndex(0);
    }
    
    private JPanel createDashboardPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(245, 247, 250));
        
        // Statistics panel with fixed size
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        statsPanel.setBackground(new Color(245, 247, 250));
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        statsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Create stat cards
        statsPanel.add(createStatCard("📋 Tổng lượt khám", "12", "Lần", new Color(66, 133, 244)));
        statsPanel.add(createStatCard("💊 Mũi tiêm", "8", "Mũi", new Color(219, 68, 55)));
        statsPanel.add(createStatCard("💰 Tổng chi tiêu", "2,450,000", "VND", new Color(244, 180, 0)));
        statsPanel.add(createStatCard("⭐ Điểm thưởng", "150", "Điểm", new Color(15, 157, 88)));
        
        // Quick actions panel
        JPanel quickActionsPanel = new JPanel();
        quickActionsPanel.setLayout(new BoxLayout(quickActionsPanel, BoxLayout.Y_AXIS));
        quickActionsPanel.setBackground(new Color(245, 247, 250));
        quickActionsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Thao tác nhanh"),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        quickActionsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        quickActionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Quick actions buttons panel
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        buttonsPanel.setBackground(new Color(245, 247, 250));
        buttonsPanel.setMaximumSize(new Dimension(800, 80));
        
        buttonsPanel.add(createQuickActionButton("📅 Đặt lịch khám", "Đặt lịch khám cho thú cưng"));
        buttonsPanel.add(createQuickActionButton("💉 Đặt lịch tiêm", "Đặt lịch tiêm phòng"));
        buttonsPanel.add(createQuickActionButton("🛒 Mua sản phẩm", "Mua sản phẩm cho thú cưng"));
        
        quickActionsPanel.add(buttonsPanel);
        
        // Add components to content panel
        contentPanel.add(statsPanel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(quickActionsPanel);
        
        // Add padding panel to center content
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(245, 247, 250));
        centerPanel.add(contentPanel, BorderLayout.NORTH);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        return mainPanel;
    }
    
    private JPanel createStatCard(String title, String value, String unit, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setPreferredSize(new Dimension(250, 120));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblTitle.setForeground(Color.GRAY);
        
        JPanel valuePanel = new JPanel(new BorderLayout());
        valuePanel.setBackground(Color.WHITE);
        
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblValue.setForeground(color);
        
        JLabel lblUnit = new JLabel(unit);
        lblUnit.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUnit.setForeground(Color.GRAY);
        
        valuePanel.add(lblValue, BorderLayout.CENTER);
        valuePanel.add(lblUnit, BorderLayout.EAST);
        
        card.add(lblTitle, BorderLayout.NORTH);
        card.add(valuePanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JButton createQuickActionButton(String text, String tooltip) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        button.setToolTipText(tooltip);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setBackground(new Color(240, 245, 240));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        button.setFocusPainted(false);
        
        button.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                "Chức năng: " + text + "\nĐang được phát triển...",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        return button;
    }
    
    // ===== PHẦN QUẢN LÝ THÚ CƯNG =====
    
    private JPanel createKhachHangThuCungPanel() {
        if (userInfo instanceof KhachHang) {
            KhachHang kh = (KhachHang) userInfo;
            ThuCungController controller = new ThuCungController(kh.getMaKH());
            return controller.getViewPanel();
        }
        return new JPanel(new BorderLayout());
    }
    
    private JPanel createNhanVienThuCungPanel() {
        ThuCungController controller = new ThuCungController(true);
        return controller.getViewPanel();
    }
    
    private void setupKhachHangTabs() {
        // Thú cưng của tôi
        JPanel thuCungPanel = createKhachHangThuCungPanel();
        JScrollPane scrollPane1 = new JScrollPane(thuCungPanel);
        tabbedPane.addTab("🐶 Thú cưng", null, scrollPane1, "Quản lý thú cưng của tôi");
        
        // Lịch sử khám
        JPanel lichSuPanel = createLichSuPanel();
        JScrollPane scrollPane2 = new JScrollPane(lichSuPanel);
        tabbedPane.addTab("📋 Lịch sử", null, scrollPane2, "Xem lịch sử khám bệnh");
        
        // Đặt lịch
        JPanel datLichPanel = createDatLichPanel();
        JScrollPane scrollPane3 = new JScrollPane(datLichPanel);
        tabbedPane.addTab("📅 Đặt lịch", null, scrollPane3, "Đặt lịch khám/tiêm");
        
        // Mua hàng
        JPanel muaHangPanel = createMuaHangPanel();
        JScrollPane scrollPane4 = new JScrollPane(muaHangPanel);
        tabbedPane.addTab("🛒 Mua hàng", null, scrollPane4, "Mua sản phẩm cho thú cưng");
    }
    
    private void setupNhanVienTabs() {
        // Quản lý khách hàng
        JPanel khachHangPanel = createNhanVienKhachHangPanel();
        JScrollPane scrollPane1 = new JScrollPane(khachHangPanel);
        tabbedPane.addTab("👥 Khách hàng", null, scrollPane1, "Quản lý khách hàng");
        
        // Quản lý thú cưng (cho nhân viên)
        JPanel thuCungPanel = createNhanVienThuCungPanel();
        JScrollPane scrollPane2 = new JScrollPane(thuCungPanel);
        tabbedPane.addTab("🐕 Thú cưng", null, scrollPane2, "Quản lý thú cưng");
        
        // Bán hàng
        JPanel banHangPanel = createBanHangPanel();
        JScrollPane scrollPane3 = new JScrollPane(banHangPanel);
        tabbedPane.addTab("💰 Bán hàng", null, scrollPane3, "Bán sản phẩm & dịch vụ");
        
        // Quản lý cuộc hẹn
        JPanel appointmentPanel = createAppointmentPanel();
        JScrollPane scrollPane4 = new JScrollPane(appointmentPanel);
        tabbedPane.addTab("📅 Cuộc hẹn", null, scrollPane4, "Quản lý lịch hẹn");
    }
    
    private void setupBacSiTabs() {
        // Khám bệnh
        JPanel khamBenhPanel = createKhamBenhPanel();
        JScrollPane scrollPane1 = new JScrollPane(khamBenhPanel);
        tabbedPane.addTab("🏥 Khám bệnh", null, scrollPane1, "Khám và chẩn đoán");
        
        // Tiêm phòng
        JPanel tiemPhongPanel = createTiemPhongPanel();
        JScrollPane scrollPane2 = new JScrollPane(tiemPhongPanel);
        tabbedPane.addTab("💉 Tiêm phòng", null, scrollPane2, "Tiêm phòng cho thú cưng");
        
        // Hồ sơ thú cưng
        JPanel hoSoPanel = createHoSoPanel();
        JScrollPane scrollPane3 = new JScrollPane(hoSoPanel);
        tabbedPane.addTab("📁 Hồ sơ", null, scrollPane3, "Xem hồ sơ thú cưng");
    }
    
    private void setupQuanLyTabs() {
        // Báo cáo
        JPanel baoCaoPanel = createBaoCaoPanel();
        JScrollPane scrollPane1 = new JScrollPane(baoCaoPanel);
        tabbedPane.addTab("📈 Báo cáo", null, scrollPane1, "Báo cáo doanh thu & thống kê");
        
        // Quản lý nhân viên
        JPanel nhanVienPanel = createQuanLyNhanVienPanel();
        JScrollPane scrollPane2 = new JScrollPane(nhanVienPanel);
        tabbedPane.addTab("👨‍💼 Nhân viên", null, scrollPane2, "Quản lý nhân sự");
        
        // Quản lý kho
        JPanel khoPanel = createKhoPanel();
        JScrollPane scrollPane3 = new JScrollPane(khoPanel);
        tabbedPane.addTab("📦 Kho hàng", null, scrollPane3, "Quản lý tồn kho");
        
        // Cấu hình hệ thống
        JPanel configPanel = createConfigPanel();
        JScrollPane scrollPane4 = new JScrollPane(configPanel);
        tabbedPane.addTab("⚙️ Cấu hình", null, scrollPane4, "Cấu hình hệ thống");
    }
    
    private void setupDefaultTabs() {
        // For unknown roles
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.add(new JLabel("Vai trò không xác định. Vui lòng liên hệ quản trị viên.", SwingConstants.CENTER), BorderLayout.CENTER);
        JScrollPane scrollPane = new JScrollPane(infoPanel);
        tabbedPane.addTab("Thông tin", scrollPane);
    }
    
    // ===== CÁC PANEL KHÁC =====
    
    private JPanel createNhanVienKhachHangPanel() {
        if (khController != null) {
            return khController.getViewPanel();
        } else {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(Color.WHITE);
            panel.add(new JLabel("Chức năng quản lý khách hàng - Đang phát triển...", SwingConstants.CENTER), BorderLayout.CENTER);
            return panel;
        }
    }
    
    private JPanel createLichSuPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.add(new JLabel("Lịch sử khám bệnh - Đang phát triển...", SwingConstants.CENTER), BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createDatLichPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.add(new JLabel("Đặt lịch hẹn - Đang phát triển...", SwingConstants.CENTER), BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createMuaHangPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.add(new JLabel("Mua sản phẩm - Đang phát triển...", SwingConstants.CENTER), BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createBanHangPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.add(new JLabel("Bán hàng - Đang phát triển...", SwingConstants.CENTER), BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createAppointmentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.add(new JLabel("Quản lý cuộc hẹn - Đang phát triển...", SwingConstants.CENTER), BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createKhamBenhPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.add(new JLabel("Khám bệnh - Đang phát triển...", SwingConstants.CENTER), BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createTiemPhongPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.add(new JLabel("Tiêm phòng - Đang phát triển...", SwingConstants.CENTER), BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createHoSoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.add(new JLabel("Hồ sơ thú cưng - Đang phát triển...", SwingConstants.CENTER), BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createBaoCaoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.add(new JLabel("Báo cáo - Đang phát triển...", SwingConstants.CENTER), BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createQuanLyNhanVienPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.add(new JLabel("Quản lý nhân viên - Đang phát triển...", SwingConstants.CENTER), BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createKhoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.add(new JLabel("Kho hàng - Đang phát triển...", SwingConstants.CENTER), BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createConfigPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.add(new JLabel("Cấu hình hệ thống - Đang phát triển...", SwingConstants.CENTER), BorderLayout.CENTER);
        return panel;
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc muốn đăng xuất?",
            "Xác nhận đăng xuất",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame().setVisible(true);
        }
    }
}