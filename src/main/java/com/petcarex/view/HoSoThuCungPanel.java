package com.petcarex.view;

import com.petcarex.dao.ThuCungDAO;
import com.petcarex.model.ThuCung;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class HoSoThuCungPanel extends JPanel {
    private final JTable petTable;
    private final DefaultTableModel tableModel;
    private final JTextField searchField;
    private final ThuCungDAO thuCungDAO;

    public HoSoThuCungPanel() {
        thuCungDAO = new ThuCungDAO();
        setLayout(new BorderLayout());

        // Search bar
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchField = new JTextField();
        JButton searchButton = new JButton("Tìm kiếm");
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        add(searchPanel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(new Object[]{"Mã", "Tên thú cưng", "Loài", "Chủ nuôi"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // non-editable
            }
        };
        petTable = new JTable(tableModel);
        petTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(petTable);
        add(scrollPane, BorderLayout.CENTER);

        // Load initial data
        loadPets("");

        // Search action
        searchButton.addActionListener(e -> loadPets(searchField.getText().trim()));

        // Row selection listener
        petTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && petTable.getSelectedRow() != -1) {
                int selectedRow = petTable.getSelectedRow();
                int petId = (int) tableModel.getValueAt(selectedRow, 0);
                showPetHistory(petId);
            }
        });
    }

    private void loadPets(String searchTerm) {
        try {
            List<ThuCung> pets;
            if (searchTerm.isEmpty()) {
                pets = thuCungDAO.findAll();
            } else {
                pets = thuCungDAO.searchByName(searchTerm);
            }

            tableModel.setRowCount(0); // clear table
            for (ThuCung pet : pets) {
                tableModel.addRow(new Object[]{
                        pet.getMaThuCung(),
                        pet.getTenThuCung(),
                        pet.getLoai(),
                        pet.getTenChu()
                });
            }
        } catch (SQLException ex) {
            //noinspection CallToPrintStackTrace
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu thú cưng: " + ex.getMessage());
        }
    }
    private void showPetHistory(int petId) {
        // Create history panel for the selected pet
        LichSuKhamPanel historyPanel = new LichSuKhamPanel();
        historyPanel.loadHistory(petId);

        // Display in a new JFrame
        JFrame historyFrame = new JFrame("Lịch sử khám - Thú cưng #" + petId);
        historyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        historyFrame.setSize(800, 400);
        historyFrame.add(historyPanel);
        historyFrame.setLocationRelativeTo(this);
        historyFrame.setVisible(true);
    }
    public JTable getPetTable() {
        return petTable;
    }
}
