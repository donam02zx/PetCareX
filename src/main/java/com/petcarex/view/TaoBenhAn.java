package com.petcarex.view;

import com.petcarex.dao.PhieuKhamDAO;
import com.petcarex.dao.ThuCungDAO;
import com.petcarex.model.PhieuKham;
import com.petcarex.model.ThuCung;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class TaoBenhAn extends JPanel {

    private JComboBox<ThuCung> cbPets;
    private final JTextArea taTrieuChung;
    private final JTextArea taChuanDoan;
    private final JTextArea taToaThuoc;
    private final JTextArea taGhiChu;
    private final JTextField tfNgayHen;

    private final PhieuKhamDAO phieuKhamDAO;
    private final int maBacSi;
    private final int maChiNhanh;

    public TaoBenhAn(int maBacSi, int maChiNhanh) {
        this.maBacSi = maBacSi;
        this.maChiNhanh = maChiNhanh;

        phieuKhamDAO = new PhieuKhamDAO();
        ThuCungDAO thuCungDAO = new ThuCungDAO();

        setLayout(new BorderLayout());
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        try {
            List<ThuCung> pets = thuCungDAO.findAll();
            cbPets = new JComboBox<>(pets.toArray(new ThuCung[0]));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải danh sách thú cưng: " + e.getMessage());
            cbPets = new JComboBox<>();
        }

        taTrieuChung = new JTextArea(3, 30);
        taChuanDoan = new JTextArea(3, 30);
        taToaThuoc = new JTextArea(3, 30);
        taGhiChu = new JTextArea(2, 30);
        tfNgayHen = new JTextField(10);

        JButton btnSave = new JButton("Lưu phiếu khám");

        int y = 0;
        gbc.gridx = 0; gbc.gridy = y; formPanel.add(new JLabel("Chọn thú cưng:"), gbc);
        gbc.gridx = 1; formPanel.add(cbPets, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y; formPanel.add(new JLabel("Triệu chứng:"), gbc);
        gbc.gridx = 1; formPanel.add(new JScrollPane(taTrieuChung), gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y; formPanel.add(new JLabel("Chẩn đoán:"), gbc);
        gbc.gridx = 1; formPanel.add(new JScrollPane(taChuanDoan), gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y; formPanel.add(new JLabel("Toa thuốc:"), gbc);
        gbc.gridx = 1; formPanel.add(new JScrollPane(taToaThuoc), gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y; formPanel.add(new JLabel("Ngày hẹn tái khám (yyyy-MM-dd):"), gbc);
        gbc.gridx = 1; formPanel.add(tfNgayHen, gbc);

        y++;
        gbc.gridx = 1; gbc.gridy = y; formPanel.add(btnSave, gbc);

        add(formPanel, BorderLayout.NORTH);

        btnSave.addActionListener(e -> savePhieuKham());
    }

    private void savePhieuKham() {
        ThuCung selectedPet = (ThuCung) cbPets.getSelectedItem();
        if (selectedPet == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thú cưng!");
            return;
        }

        PhieuKham pk = new PhieuKham();
        pk.setMaThuCung(selectedPet.getMaThuCung());
        pk.setMaBacSi(maBacSi);
        pk.setMaChiNhanh(maChiNhanh);
        pk.setNgayKham(LocalDateTime.now());
        pk.setTrieuChung(taTrieuChung.getText().trim());
        pk.setChuanDoan(taChuanDoan.getText().trim());
        pk.setToaThuoc(taToaThuoc.getText().trim());
        pk.setGhiChu(taGhiChu.getText().trim());

        String ngayHenText = tfNgayHen.getText().trim();
        if (!ngayHenText.isEmpty()) {
            try {
                pk.setNgayHenTaiKham(LocalDate.parse(ngayHenText));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ngày hẹn không hợp lệ! Dùng định dạng yyyy-MM-dd");
                return;
            }
        }

        try {
            phieuKhamDAO.create(pk);
            JOptionPane.showMessageDialog(this, "Lưu phiếu khám thành công!");
            // Optionally clear fields
            taTrieuChung.setText("");
            taChuanDoan.setText("");
            taToaThuoc.setText("");
            taGhiChu.setText("");
            tfNgayHen.setText("");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu phiếu khám: " + ex.getMessage());
        }
    }
}
