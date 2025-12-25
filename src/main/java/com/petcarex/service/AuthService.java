package com.petcarex.service;

import com.petcarex.dao.NhanVienDAO;
import com.petcarex.dao.KhachHangDAO;
import com.petcarex.model.NhanVien;
import com.petcarex.model.KhachHang;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AuthService {
    private final Map<String, UserCredentials> userDatabase;
    private final NhanVienDAO nhanVienDAO;
    private final KhachHangDAO khachHangDAO;
    
    public AuthService() {
        this.userDatabase = new HashMap<>();
        this.nhanVienDAO = new NhanVienDAO();
        this.khachHangDAO = new KhachHangDAO();
        initializeUsers();
    }
    
    private void initializeUsers() {
        try {
            // Load nhân viên từ database
            for (NhanVien nv : nhanVienDAO.findAll()) {
                String username = nv.getEmail();
                if (username == null || username.trim().isEmpty()) {
                    username = "nv" + nv.getMaNV(); // Fallback
                }
                
                String password = nv.getCccd(); // Password là CCCD
                String role = getRoleFromChucVu(nv.getChucVu());
//                String role = "Nhân viên";
                
                userDatabase.put(username, new UserCredentials(username, password, role));
                System.out.println("Đã thêm NV: " + username + " - Role: " + role);
            }
            
            // Load khách hàng từ database
            for (KhachHang kh : khachHangDAO.findAll()) {
                String username = kh.getEmail();
                if (username == null || username.trim().isEmpty()) {
                    continue; // Bỏ qua nếu không có email
                }
                
                String password = kh.getCccd(); // Password là CCCD
                String role = "Khách hàng";
                
                userDatabase.put(username, new UserCredentials(username, password, role));
                System.out.println("Đã thêm KH: " + username + " - Role: " + role);
            }
            
        } catch (SQLException e) {
            System.err.println("Lỗi khi load users từ database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private String getRoleFromChucVu(String chucVu) {
        if (chucVu == null) return "Nhân viên";
        
        chucVu = chucVu.toLowerCase();
        
        if (chucVu.contains("quản lý")) {
            return "Quản lý";
        } else if (chucVu.contains("bác sĩ") || chucVu.contains("thú y")) {
            return "Bác sĩ";
        } else if (chucVu.contains("nhân viên")) {
            return "Nhân viên";
        } else {
            return "Nhân viên";
        }
    }
    
//    private String getRoleFromChucVu(String chucVu) {
//        if (chucVu == null) return "Nhân viên";
//        
//        switch (chucVu.toLowerCase()) {
//            case "bác sĩ thú y":
//                return "Bác sĩ";
//            case "quản lý chi nhánh":
//                return "Quản lý";
//            case "nhân viên bán hàng":
//            case "nhân viên tiếp tân":
//                return "Nhân viên";
//            default:
//                return "Nhân viên";
//        }
//    }
    
    public boolean authenticate(String username, String password, String role) {
        if (username == null || password == null || role == null) {
            return false;
        }
        
        // Kiểm tra trực tiếp từ database (nên dùng cách này thay vì cache)
        try {
            if (role.equals("Khách hàng")) {
                return authenticateKhachHang(username, password);
            } else {
                return authenticateNhanVien(username, password, role);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi xác thực: " + e.getMessage());
            return false;
        }
    }
    
    private boolean authenticateNhanVien(String username, String password, String role) throws SQLException {
        // Tìm nhân viên theo email
        for (NhanVien nv : nhanVienDAO.findAll()) {
            if (username.equals(nv.getEmail())) {
                // Kiểm tra password (CCCD)
                if (password.equals(nv.getCccd())) {
                    // Kiểm tra role
                    String nvRole = getRoleFromChucVu(nv.getChucVu());
                    return role.equals(nvRole);
                }
            }
        }
        return false;
    }
    
    private boolean authenticateKhachHang(String username, String password) throws SQLException {
        // Tìm khách hàng theo email
        for (KhachHang kh : khachHangDAO.findAll()) {
            if (username.equals(kh.getEmail())) {
                // Kiểm tra password (CCCD)
                return password.equals(kh.getCccd());
            }
        }
        return false;
    }
    
    public Object getUserInfo(String username, String role) throws SQLException {
        switch (role) {
            case "Nhân viên":
            case "Bác sĩ":
            case "Quản lý":
                // Tìm nhân viên theo email
                for (NhanVien nv : nhanVienDAO.findAll()) {
                    if (username.equals(nv.getEmail())) {
                        return nv;
                    }
                }
                return null;
                
            case "Khách hàng":
                // Tìm khách hàng theo email
                for (KhachHang kh : khachHangDAO.findAll()) {
                    if (username.equals(kh.getEmail())) {
                        return kh;
                    }
                }
                return null;
                
            default:
                return null;
        }
    }
    
    // Helper class để lưu thông tin user
    private static class UserCredentials {
        String username;
        String password;
        String role;
        
        UserCredentials(String username, String password, String role) {
            this.username = username;
            this.password = password;
            this.role = role;
        }
    }
    
    // Thêm method đăng ký khách hàng
    public boolean registerKhachHang(KhachHang kh, String password) {
        try {
            // Trong hệ thống hiện tại, chúng ta dùng CCCD làm password
            // Và email làm username
            // Vì vậy, khi đăng ký thành công, user có thể đăng nhập bằng email + CCCD
            
            // Thêm vào userDatabase cache
            userDatabase.put(kh.getEmail(), 
                new UserCredentials(kh.getEmail(), kh.getCccd(), "Khách hàng"));
            
            System.out.println("Đã đăng ký khách hàng: " + kh.getEmail());
            return true;
            
        } catch (Exception e) {
            System.err.println("Lỗi khi đăng ký: " + e.getMessage());
            return false;
        }
    }
}