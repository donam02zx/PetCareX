// file: com/petcarex/view/DatLichView.java
package com.petcarex.view;

import com.petcarex.controller.DatLichController;
import com.petcarex.model.LichHen;
import com.petcarex.model.ThuCung;
import com.petcarex.model.DichVu;
import com.petcarex.model.NhanVien;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DatLichView extends JPanel {
    private DatLichController controller;
    private boolean isKhachHang; // Phân biệt giao diện khách hàng/nhân viên
    
    // UI Components
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnDatLich, btnHuyLich, btnXacNhan;
    private JComboBox<String> cboLoaiLichHen;
    private JComboBox<ThuCung> cboThuCung;
    private JComboBox<DichVu> cboDichVu;
    private JComboBox<NhanVien> cboBacSi;
    private JComboBox<String> cboChiNhanh;
    private JSpinner spnNgayGio;
    private JTextArea txtGhiChu;
    
    public DatLichView(DatLichController controller, boolean isKhachHang) {
        this.controller = controller;
        this.isKhachHang = isKhachHang;
        initComponents();
        setupLayout();
        loadData();
    }
    
    private void initComponents() {
        // Form components
        cboLoaiLichHen = new JComboBox<>(new String[]{
            "Khám bệnh", "Tiêm phòng", "Spa & Grooming", "Khác"
        });
        cboLoaiLichHen.addActionListener(e -> onLoaiLichHenChanged());
        
        cboThuCung = new JComboBox<>();
        cboDichVu = new JComboBox<>();
        cboBacSi = new JComboBox<>();
        cboChiNhanh = new JComboBox<>(new String[]{
            "PetCareX Quận 1", "PetCareX Quận 7"
        });
        
        // Date and time picker
        SpinnerDateModel dateModel = new SpinnerDateModel();
        spnNgayGio = new JSpinner(dateModel);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(spnNgayGio, "dd/MM/yyyy HH:mm");
        spnNgayGio.setEditor(timeEditor);
        
        txtGhiChu = new JTextArea(3, 20);
        txtGhiChu.setLineWrap(true);
        txtGhiChu.setWrapStyleWord(true);
        
        // Buttons
        btnDatLich = new JButton("Đặt Lịch");
        btnDatLich.addActionListener(e -> showDatLichDialog());
        
        btnHuyLich = new JButton("Hủy Lịch");
        btnHuyLich.addActionListener(e -> huyLichHen());
        
        btnXacNhan = new JButton("Xác Nhận");
        btnXacNhan.addActionListener(e -> xacNhanLichHen());
        btnXacNhan.setVisible(!isKhachHang); // Chỉ hiển thị cho nhân viên
        
        // Table
        String[] columns = isKhachHang ? 
            new String[]{"Mã LH", "Thú cưng", "Loại", "Thời gian", "Chi nhánh", "Trạng thái"} :
            new String[]{"Mã LH", "Thú cưng", "Loại", "Thời gian", "Chi nhánh", "Trạng thái", "Ghi chú"};
        
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
        
        // Top panel: Quick form for booking (for customers)
        if (isKhachHang) {
            JPanel quickFormPanel = new JPanel(new GridBagLayout());
            quickFormPanel.setBorder(BorderFactory.createTitledBorder("Đặt lịch nhanh"));
            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            
            // Row 1
            gbc.gridx = 0; gbc.gridy = 0;
            quickFormPanel.add(new JLabel("Thú cưng:"), gbc);
            
            gbc.gridx = 1; gbc.gridwidth = 2;
            quickFormPanel.add(cboThuCung, gbc);
            
            // Row 2
            gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
            quickFormPanel.add(new JLabel("Loại lịch:"), gbc);
            
            gbc.gridx = 1; gbc.gridwidth = 2;
            quickFormPanel.add(cboLoaiLichHen, gbc);
            
            // Row 3
            gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
            quickFormPanel.add(new JLabel("Thời gian:"), gbc);
            
            gbc.gridx = 1; gbc.gridwidth = 2;
            quickFormPanel.add(spnNgayGio, gbc);
            
            // Row 4: Button
            gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 1;
            quickFormPanel.add(btnDatLich, gbc);
            
            add(quickFormPanel, BorderLayout.NORTH);
        }
        
        // Center panel: Table
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Bottom panel: Action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        if (isKhachHang) {
            actionPanel.add(btnHuyLich);
        } else {
            actionPanel.add(btnXacNhan);
            actionPanel.add(btnHuyLich);
        }
        
        // Add components
        add(scrollPane, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
    }
    
    private void loadData() {
        // Load combo boxes
        loadThuCung();
        loadDichVu();
        loadBacSi();
        
        // Load table data
        refreshTable();
    }
    
    private void loadThuCung() {
        if (controller != null) {
            List<ThuCung> dsThuCung = controller.getThuCungCuaKhachHang();
            if (dsThuCung != null) {
                cboThuCung.removeAllItems();
                for (ThuCung tc : dsThuCung) {
                    cboThuCung.addItem(tc);
                }
            }
        }
    }
    
    private void loadDichVu() {
        if (controller != null) {
            List<DichVu> dsDichVu = controller.getDichVu();
            if (dsDichVu != null) {
                cboDichVu.removeAllItems();
                for (DichVu dv : dsDichVu) {
                    cboDichVu.addItem(dv);
                }
            }
        }
    }
    
    private void loadBacSi() {
        if (controller != null) {
            List<NhanVien> dsBacSi = controller.getBacSi();
            if (dsBacSi != null) {
                cboBacSi.removeAllItems();
                for (NhanVien nv : dsBacSi) {
                    cboBacSi.addItem(nv);
                }
            }
        }
    }
    
    public void refreshTable() {
        tableModel.setRowCount(0);
        
        if (controller != null) {
            List<LichHen> dsLichHen = controller.getLichHenCuaKhachHang();
            if (dsLichHen != null) {
                for (LichHen lh : dsLichHen) {
                    Object[] row = isKhachHang ?
                        new Object[]{
                            lh.getMaLichHen(),
                            lh.getTenThuCung(),
                            lh.getLoaiLichHen(),
                            lh.getThoiGianDisplay(),
                            lh.getTenChiNhanh(),
                            lh.getTrangThai()
                        } :
                        new Object[]{
                            lh.getMaLichHen(),
                            lh.getTenThuCung(),
                            lh.getLoaiLichHen(),
                            lh.getThoiGianDisplay(),
                            lh.getTenChiNhanh(),
                            lh.getTrangThai(),
                            lh.getGhiChu()
                        };
                    tableModel.addRow(row);
                }
            }
        }
    }
    
    private void onLoaiLichHenChanged() {
        String loaiLichHen = (String) cboLoaiLichHen.getSelectedItem();
        // Có thể thêm logic xử lý khi thay đổi loại lịch
    }
    
    private void showDatLichDialog() {
        if (cboThuCung.getItemCount() == 0) {
            showError("Bạn chưa có thú cưng nào. Vui lòng thêm thú cưng trước.");
            return;
        }
        
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
            "Đặt lịch hẹn", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        
        // Form fields
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        
        JLabel lblThuCung = new JLabel("Thú cưng:");
        JLabel lblLoaiLich = new JLabel("Loại lịch:");
        JLabel lblDichVu = new JLabel("Dịch vụ:");
        JLabel lblBacSi = new JLabel("Bác sĩ:");
        JLabel lblChiNhanh = new JLabel("Chi nhánh:");
        JLabel lblThoiGian = new JLabel("Thời gian:");
        JLabel lblGhiChu = new JLabel("Ghi chú:");
        
        JComboBox<ThuCung> cboThuCungDialog = new JComboBox<>();
        List<ThuCung> dsThuCung = controller.getThuCungCuaKhachHang();
        if (dsThuCung != null) {
            for (ThuCung tc : dsThuCung) {
                cboThuCungDialog.addItem(tc);
            }
        }
        
        JComboBox<String> cboLoaiLichDialog = new JComboBox<>(new String[]{
            "Khám bệnh", "Tiêm phòng", "Spa & Grooming", "Khác"
        });
        
        JComboBox<DichVu> cboDichVuDialog = new JComboBox<>();
        List<DichVu> dsDichVu = controller.getDichVu();
        if (dsDichVu != null) {
            for (DichVu dv : dsDichVu) {
                cboDichVuDialog.addItem(dv);
            }
        }
        
        JComboBox<NhanVien> cboBacSiDialog = new JComboBox<>();
        List<NhanVien> dsBacSi = controller.getBacSi();
        if (dsBacSi != null) {
            for (NhanVien nv : dsBacSi) {
                cboBacSiDialog.addItem(nv);
            }
        }
        
        JComboBox<String> cboChiNhanhDialog = new JComboBox<>(new String[]{
            "PetCareX Quận 1", "PetCareX Quận 7"
        });
        
        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner spnNgayGioDialog = new JSpinner(dateModel);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(spnNgayGioDialog, "dd/MM/yyyy HH:mm");
        spnNgayGioDialog.setEditor(timeEditor);
        
        JTextArea txtGhiChuDialog = new JTextArea(3, 20);
        txtGhiChuDialog.setLineWrap(true);
        txtGhiChuDialog.setWrapStyleWord(true);
        JScrollPane scrollGhiChu = new JScrollPane(txtGhiChuDialog);
        
        formPanel.add(lblThuCung);
        formPanel.add(cboThuCungDialog);
        formPanel.add(lblLoaiLich);
        formPanel.add(cboLoaiLichDialog);
        formPanel.add(lblDichVu);
        formPanel.add(cboDichVuDialog);
        formPanel.add(lblBacSi);
        formPanel.add(cboBacSiDialog);
        formPanel.add(lblChiNhanh);
        formPanel.add(cboChiNhanhDialog);
        formPanel.add(lblThoiGian);
        formPanel.add(spnNgayGioDialog);
        formPanel.add(lblGhiChu);
        formPanel.add(scrollGhiChu);
        
        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton btnSave = new JButton("Đặt lịch");
        JButton btnCancel = new JButton("Hủy");
        
        btnSave.addActionListener(e -> {
            try {
                ThuCung selectedTC = (ThuCung) cboThuCungDialog.getSelectedItem();
                DichVu selectedDV = (DichVu) cboDichVuDialog.getSelectedItem();
                NhanVien selectedBS = (NhanVien) cboBacSiDialog.getSelectedItem();
                
                LichHen lichHen = new LichHen();
                lichHen.setMaThuCung(selectedTC.getMaThuCung());
                lichHen.setLoaiLichHen((String) cboLoaiLichDialog.getSelectedItem());
                
                // Convert Date to LocalDateTime
                java.util.Date selectedDate = (java.util.Date) spnNgayGioDialog.getValue();
                LocalDateTime thoiGian = selectedDate.toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDateTime();
                lichHen.setThoiGian(thoiGian);
                
                // Chi nhánh (tạm thời dùng index)
                int maChiNhanh = cboChiNhanhDialog.getSelectedIndex() + 1;
                lichHen.setMaChiNhanh(maChiNhanh);
                
                if (selectedBS != null) {
                    lichHen.setMaBacSi(selectedBS.getMaNV());
                }
                
                if (selectedDV != null) {
                    lichHen.setMaDichVu(selectedDV.getMaDichVu());
                }
                
                lichHen.setGhiChu(txtGhiChuDialog.getText());
                
                if (controller.datLichMoi(lichHen)) {
                    dialog.dispose();
                }
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi: " + ex.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnCancel.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void huyLichHen() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showError("Vui lòng chọn một lịch hẹn để hủy");
            return;
        }
        
        int maLichHen = (int) tableModel.getValueAt(selectedRow, 0);
        controller.huyLichHen(maLichHen);
    }
    
    private void xacNhanLichHen() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showError("Vui lòng chọn một lịch hẹn để xác nhận");
            return;
        }
        
        int maLichHen = (int) tableModel.getValueAt(selectedRow, 0);
        controller.xacNhanLichHen(maLichHen);
    }
    
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
    
    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }
}