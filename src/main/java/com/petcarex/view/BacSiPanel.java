package com.petcarex.view;

import javax.swing.*;
import java.awt.*;

public class BacSiPanel extends JPanel {
    private final JTabbedPane tabbedPane;

    public BacSiPanel(int maNV,int maChiNhanh) { // constructor with doctor ID
        setLayout(new BorderLayout()); // important!
        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER); // make the tabbedPane fill the panel

        setupBacSiTabs(maNV,maChiNhanh);
    }

    private void setupBacSiTabs(int maNV,int maChiNhanh) {
        // Hồ sơ thú cưng
        HoSoThuCungPanel hoSoPanel = new HoSoThuCungPanel();
        LichSuKhamPanel lichSuPanel = new LichSuKhamPanel();
        TraCuuThuoc thuocPanel = new TraCuuThuoc();
        TaoBenhAn taoBenhAnPanel = new TaoBenhAn(maNV, maChiNhanh);
        JScrollPane scrollHoSo = new JScrollPane(hoSoPanel);
        tabbedPane.addTab("📁 Hồ sơ thú cưng", null, scrollHoSo, "Tra cứu thông tin thú cưng");
        tabbedPane.addTab("📜 Lịch sử khám", new JScrollPane(lichSuPanel));
        tabbedPane.addTab("💊 Tra cứu thuốc", new JScrollPane(thuocPanel));
        JScrollPane scrollBenhAn = new JScrollPane(taoBenhAnPanel);
        tabbedPane.addTab("📝 Tạo bệnh án", null, scrollBenhAn, "Tạo phiếu khám và kê toa cho thú cưng");

        add(tabbedPane, BorderLayout.CENTER);
        // Add other tabs here...
        hoSoPanel.getPetTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = hoSoPanel.getPetTable().getSelectedRow();
                if (selectedRow >= 0) {
                    int maThuCung = (int) hoSoPanel.getPetTable().getValueAt(selectedRow, 0); // Assuming MaThuCung is in column 0
                    lichSuPanel.loadHistory(maThuCung);
                }
            }
        });
    }
}

