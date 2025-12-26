// file: com/petcarex/view/ThanhToanView.java
package com.petcarex.view;

import com.petcarex.controller.ThanhToanController;
import com.petcarex.model.HoaDon;
import com.petcarex.model.KhachHang;
import com.petcarex.model.ThanhToanRequest;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ThanhToanView extends JPanel {
    private ThanhToanController controller;
    
    // UI Components
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> cboTrangThai;
    private JButton btnThanhToan, btnChiTiet, btnHuy, btnRefresh;
    private JLabel lblThongTinKH, lblTongTien;
    
    public ThanhToanView(ThanhToanController controller) {
        this.controller = controller;
        initComponents();
        setupLayout();
        loadData();
    }
    
    private void initComponents() {
        // Filter
        cboTrangThai = new JComboBox<>(new String[]{"Chưa thanh toán", "Đã thanh toán", "Tất cả"});
        cboTrangThai.addActionListener(e -> filterTable());
        
        // Buttons
        btnRefresh = new JButton("Làm mới");
        btnRefresh.addActionListener(e -> loadData());
        
        btnThanhToan = new JButton("Thanh toán");
        btnThanhToan.addActionListener(e -> showThanhToanDialog());
        
        btnChiTiet = new JButton("Xem chi tiết");
        btnChiTiet.addActionListener(e -> showChiTietDialog());
        
        btnHuy = new JButton("Hủy hóa đơn");
        btnHuy.addActionListener(e -> huyHoaDon());
        
        // Labels
        lblThongTinKH = new JLabel("Chọn hóa đơn để xem thông tin");
        lblThongTinKH.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        lblTongTien = new JLabel("Tổng tiền: 0 VND");
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Table
        String[] columns = {
            "Mã HĐ", "Khách hàng", "Nhân viên", "Ngày lập", 
            "Tổng tiền", "Hình thức TT", "Trạng thái"
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
        
        // Add selection listener
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateSelectedHoaDon();
            }
        });
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Top panel: Filter and buttons
        JPanel topPanel = new JPanel(new BorderLayout());
        
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Trạng thái:"));
        filterPanel.add(cboTrangThai);
        filterPanel.add(btnRefresh);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnThanhToan);
        buttonPanel.add(btnChiTiet);
        buttonPanel.add(btnHuy);
        
        topPanel.add(filterPanel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Center panel: Table
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Bottom panel: Customer info and total
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.add(lblThongTinKH);
        infoPanel.add(lblTongTien);
        
        bottomPanel.add(infoPanel, BorderLayout.CENTER);
        
        // Add components
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    public void loadData() {
        String trangThai = (String) cboTrangThai.getSelectedItem();
        java.util.List<HoaDon> hoaDonList = null;
        
        if ("Chưa thanh toán".equals(trangThai)) {
            hoaDonList = controller.getHoaDonChuaThanhToan();
        } else if ("Đã thanh toán".equals(trangThai)) {
            hoaDonList = controller.getHoaDonDaThanhToan();
        } else {
            // Load all
            hoaDonList = controller.getHoaDonChuaThanhToan();
            // TODO: Merge with paid invoices
        }
        
        displayHoaDon(hoaDonList);
    }
    
    public void displayHoaDon(java.util.List<HoaDon> hoaDonList) {
        tableModel.setRowCount(0);
        
        if (hoaDonList != null) {
            for (HoaDon hd : hoaDonList) {
                Object[] row = {
                    hd.getMaHoaDon(),
                    hd.getTenKhachHang(),
                    hd.getTenNhanVien(),
                    hd.getNgayLap().toLocalDate().toString(),
                    String.format("%,.0f VND", hd.getTongTien()),
                    hd.getHinhThucThanhToan(),
                    hd.getTrangThai()
                };
                tableModel.addRow(row);
            }
        }
    }
    
    private void updateSelectedHoaDon() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int maHoaDon = (int) tableModel.getValueAt(selectedRow, 0);
            HoaDon hd = controller.getHoaDonChiTiet(maHoaDon);
            
            if (hd != null) {
                KhachHang kh = controller.getKhachHang(hd.getMaKH());
                if (kh != null) {
                    lblThongTinKH.setText(String.format(
                        "KH: %s (Mã: %d) | Cấp độ: %s | Điểm: %d",
                        kh.getHoTen(), kh.getMaKH(), kh.getCapDoThanhVien(), kh.getDiemLoyalty()
                    ));
                }
                
                lblTongTien.setText(String.format("Tổng tiền: %,.0f VND", hd.getTongTien()));
            }
        }
    }
    
    private void showThanhToanDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showError("Vui lòng chọn một hóa đơn để thanh toán");
            return;
        }
        
        int maHoaDon = (int) tableModel.getValueAt(selectedRow, 0);
        HoaDon hd = controller.getHoaDonChiTiet(maHoaDon);
        
        if (hd == null) {
            showError("Không thể tải thông tin hóa đơn");
            return;
        }
        
        if ("Đã thanh toán".equals(hd.getTrangThai())) {
            showError("Hóa đơn đã được thanh toán");
            return;
        }
        
        KhachHang kh = controller.getKhachHang(hd.getMaKH());
        if (kh == null) {
            showError("Không thể tải thông tin khách hàng");
            return;
        }
        
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thanh toán hóa đơn", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblMaHD = new JLabel("Mã hóa đơn: " + hd.getMaHoaDon());
        JLabel lblKhachHang = new JLabel("Khách hàng: " + kh.getHoTen());
        JLabel lblTongTienHD = new JLabel(String.format("Tổng tiền: %,.0f VND", hd.getTongTien()));
        JLabel lblDiemCoTheDung = new JLabel(String.format("Điểm có thể dùng: %d (1 điểm = 1,000 VND)", kh.getDiemLoyalty()));
        
        JTextField txtDiemSuDung = new JTextField("0");
        JComboBox<String> cboHinhThucTT = new JComboBox<>(new String[]{
            "Tiền mặt", "Chuyển khoản", "Thẻ", "Ví điện tử"
        });
        
        formPanel.add(lblMaHD);
        formPanel.add(lblKhachHang);
        formPanel.add(lblTongTienHD);
        formPanel.add(lblDiemCoTheDung);
        formPanel.add(new JLabel("Số điểm sử dụng:"));
        formPanel.add(txtDiemSuDung);
        formPanel.add(new JLabel("Hình thức thanh toán:"));
        formPanel.add(cboHinhThucTT);
        
        // Calculation panel
        JPanel calcPanel = new JPanel(new BorderLayout());
        calcPanel.setBorder(BorderFactory.createTitledBorder("Tính toán"));
        
        JTextArea txtTinhToan = new JTextArea(4, 30);
        txtTinhToan.setEditable(false);
        txtTinhToan.setText(calculatePaymentDetails(hd, kh, 0));
        
        JButton btnTinhLai = new JButton("Tính lại");
        btnTinhLai.addActionListener(e -> {
            try {
                int diemSuDung = Integer.parseInt(txtDiemSuDung.getText());
                txtTinhToan.setText(calculatePaymentDetails(hd, kh, diemSuDung));
            } catch (NumberFormatException ex) {
                txtTinhToan.setText("Số điểm không hợp lệ!");
            }
        });
        
        calcPanel.add(new JScrollPane(txtTinhToan), BorderLayout.CENTER);
        calcPanel.add(btnTinhLai, BorderLayout.SOUTH);
        
        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton btnSave = new JButton("Xác nhận thanh toán");
        JButton btnCancel = new JButton("Hủy");
        
        btnSave.addActionListener(e -> {
            try {
                ThanhToanRequest request = new ThanhToanRequest();
                request.setMaHoaDon(maHoaDon);
                request.setHinhThucThanhToan((String) cboHinhThucTT.getSelectedItem());
                request.setDiemSuDung(Integer.parseInt(txtDiemSuDung.getText()));
                
                if (controller.thucHienThanhToan(request)) {
                    dialog.dispose();
                }
            } catch (NumberFormatException ex) {
                showError("Số điểm không hợp lệ!");
            } catch (Exception ex) {
                showError("Lỗi: " + ex.getMessage());
            }
        });
        
        btnCancel.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        
        // Add components to dialog
        dialog.add(formPanel, BorderLayout.NORTH);
        dialog.add(calcPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private String calculatePaymentDetails(HoaDon hd, KhachHang kh, int diemSuDung) {
        StringBuilder sb = new StringBuilder();
        
        // Tính chiết khấu thành viên
        double chietKhauPercent = 0;
        if ("VIP".equals(kh.getCapDoThanhVien())) {
            chietKhauPercent = 10;
        } else if ("Thân thiết".equals(kh.getCapDoThanhVien())) {
            chietKhauPercent = 5;
        }
        
        double chietKhauAmount = hd.getTongTien() * chietKhauPercent / 100;
        double tienSauChietKhau = hd.getTongTien() - chietKhauAmount;
        
        // Tính giảm giá từ điểm
        double giamTuDiem = diemSuDung * 1000;
        double tienSauDiem = tienSauChietKhau - giamTuDiem;
        
        // Tính điểm cộng dự kiến
        int diemCongDuKien = (int) Math.floor(tienSauDiem / 10000);
        
        sb.append(String.format("Tổng tiền hàng: %,.0f VND\n", hd.getTongTien()));
        sb.append(String.format("Chiết khấu %s (%d%%): -%,.0f VND\n", 
            kh.getCapDoThanhVien(), (int)chietKhauPercent, chietKhauAmount));
        sb.append(String.format("Giảm từ điểm (%d điểm): -%,.0f VND\n", 
            diemSuDung, giamTuDiem));
        sb.append(String.format("Thành tiền: %,.0f VND\n", Math.max(tienSauDiem, 0)));
        sb.append(String.format("Điểm cộng dự kiến: %d điểm", diemCongDuKien));
        
        return sb.toString();
    }
    
    private void showChiTietDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showError("Vui lòng chọn một hóa đơn để xem chi tiết");
            return;
        }
        
        int maHoaDon = (int) tableModel.getValueAt(selectedRow, 0);
        HoaDon hd = controller.getHoaDonChiTiet(maHoaDon);
        
        if (hd == null) {
            showError("Không thể tải thông tin hóa đơn");
            return;
        }
        
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chi tiết hóa đơn", true);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(this);
        
        JTextArea txtChiTiet = new JTextArea();
        txtChiTiet.setEditable(false);
        txtChiTiet.setText(formatHoaDonDetails(hd));
        txtChiTiet.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(txtChiTiet);
        dialog.add(scrollPane);
        dialog.setVisible(true);
    }
    
    private String formatHoaDonDetails(HoaDon hd) {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("           PETCAREX - HÓA ĐƠN           \n");
        sb.append("========================================\n\n");
        sb.append(String.format("Mã hóa đơn: HD%06d\n", hd.getMaHoaDon()));
        sb.append(String.format("Ngày lập: %s\n", hd.getNgayLap()));
        sb.append(String.format("Khách hàng: %s\n", hd.getTenKhachHang()));
        sb.append(String.format("Nhân viên: %s\n", hd.getTenNhanVien()));
        sb.append(String.format("Chi nhánh: %s\n\n", hd.getTenChiNhanh()));
        
        sb.append("----------------------------------------\n");
        sb.append(String.format("%-30s %10s\n", "Tổng tiền hàng", String.format("%,.0f VND", hd.getTongTienHang())));
        sb.append(String.format("%-30s %10s\n", "Chiết khấu thành viên", String.format("-%,.0f VND", hd.getTongTienHang() * hd.getChietKhauThanhVien() / 100)));
        sb.append(String.format("%-30s %10s\n", "Chiết khấu khác", String.format("-%,.0f VND", hd.getChietKhauKhac())));
        sb.append(String.format("%-30s %10s\n", "Điểm sử dụng", String.format("-%d điểm", hd.getDiemSuDung())));
        sb.append("----------------------------------------\n");
        sb.append(String.format("%-30s %10s\n", "TỔNG CỘNG", String.format("%,.0f VND", hd.getTongTien())));
        sb.append("----------------------------------------\n\n");
        
        sb.append(String.format("Hình thức thanh toán: %s\n", hd.getHinhThucThanhToan()));
        sb.append(String.format("Trạng thái: %s\n", hd.getTrangThai()));
        sb.append(String.format("Điểm cộng: %d điểm\n", hd.getDiemCong()));
        
        sb.append("\n========================================\n");
        sb.append("          CẢM ƠN QUÝ KHÁCH!           \n");
        sb.append("========================================\n");
        
        return sb.toString();
    }
    
    private void huyHoaDon() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showError("Vui lòng chọn một hóa đơn để hủy");
            return;
        }
        
        int maHoaDon = (int) tableModel.getValueAt(selectedRow, 0);
        controller.huyHoaDon(maHoaDon);
    }
    
    private void filterTable() {
        loadData();
    }
    
    public void refreshTable() {
        loadData();
    }
    
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
    
    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }
}