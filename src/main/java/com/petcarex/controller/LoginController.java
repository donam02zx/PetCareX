package com.petcarex.controller;

import com.petcarex.service.AuthService;
import com.petcarex.service.KhachHangService;
import com.petcarex.view.LoginFrame;
import com.petcarex.view.MainFrame;
import com.petcarex.model.KhachHang;
import com.petcarex.model.NhanVien;

public class LoginController {
    private final AuthService authService;
    private final KhachHangService khachHangService;
    private LoginFrame loginFrame;
    
    public LoginController() {
        this.authService = new AuthService();
        this.khachHangService = new KhachHangService();
    }
    
    public void setView(LoginFrame frame) {
        this.loginFrame = frame;
    }
    
    public boolean login(String username, String password, String role) {
        try {
            // Authenticate user
            boolean success = authService.authenticate(username, password, role);
            
            if (success) {
                // Get user info
                Object userInfo = authService.getUserInfo(username, role);
                
                // Show success message
                loginFrame.showSuccess("Đăng nhập thành công! Đang chuyển hướng...");
                
                // Navigate to main frame after short delay
                javax.swing.Timer timer = new javax.swing.Timer(1000, e -> {
                    navigateToMainFrame(role, userInfo);
                });
                timer.setRepeats(false);
                timer.start();
                
                return true;
            } else {
                loginFrame.showError("Sai tên đăng nhập hoặc mật khẩu");
                return false;
            }
        } catch (Exception e) {
            loginFrame.showError("Lỗi đăng nhập: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public void registerCustomer(String username, String password, KhachHang kh) {
        try {
            // Thêm khách hàng vào database
            KhachHang newKH = khachHangService.themKhachHang(kh);
            
            // Trong hệ thống hiện tại, password được lưu là CCCD
            // Nếu cần thay đổi, bạn có thể cần thêm bảng users riêng
            
            if (newKH != null) {
                loginFrame.showSuccess("Đăng ký thành công! Mã KH: " + newKH.getMaKH() + 
                                     "\nVui lòng đăng nhập với email: " + newKH.getEmail() + 
                                     "\nMật khẩu: CCCD của bạn");
                loginFrame.clearForm();
            } else {
                loginFrame.showError("Đăng ký thất bại. Vui lòng thử lại.");
            }
        } catch (Exception e) {
            loginFrame.showError("Lỗi đăng ký: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void navigateToMainFrame(String role, Object userInfo) {
        loginFrame.dispose();
        
        // Open MainFrame with user info
        MainFrame mainFrame = new MainFrame(role, userInfo);
        mainFrame.setVisible(true);
    }
}