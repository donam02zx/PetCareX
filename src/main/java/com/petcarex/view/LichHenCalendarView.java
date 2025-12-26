// file: com/petcarex/view/LichHenCalendarView.java
package com.petcarex.view;

import com.petcarex.controller.QuanLyLichHenController;
import com.petcarex.model.LichHen;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class LichHenCalendarView extends JPanel {
    private QuanLyLichHenController controller;
    private LocalDate currentDate;
    
    public LichHenCalendarView(QuanLyLichHenController controller) {
        this.controller = controller;
        this.currentDate = LocalDate.now();
        initComponents();
        setupLayout();
        loadCalendar();
    }
    
    private void initComponents() {
        // UI components will be initialized in setupLayout
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Navigation panel
        JPanel navPanel = new JPanel(new BorderLayout());
        
        JButton btnPrev = new JButton("◀ Tháng trước");
        JButton btnNext = new JButton("Tháng sau ▶");
        JLabel lblMonth = new JLabel(getMonthDisplay(), SwingConstants.CENTER);
        lblMonth.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        btnPrev.addActionListener(e -> {
            currentDate = currentDate.minusMonths(1);
            loadCalendar();
        });
        
        btnNext.addActionListener(e -> {
            currentDate = currentDate.plusMonths(1);
            loadCalendar();
        });
        
        navPanel.add(btnPrev, BorderLayout.WEST);
        navPanel.add(lblMonth, BorderLayout.CENTER);
        navPanel.add(btnNext, BorderLayout.EAST);
        
        add(navPanel, BorderLayout.NORTH);
    }
    
    private void loadCalendar() {
        // Remove old calendar if exists
        if (getComponentCount() > 1) {
            remove(1);
        }
        
        // Create calendar panel
        JPanel calendarPanel = createCalendarPanel();
        add(new JScrollPane(calendarPanel), BorderLayout.CENTER);
        
        revalidate();
        repaint();
    }
    
    private JPanel createCalendarPanel() {
        JPanel monthPanel = new JPanel(new GridLayout(0, 7, 5, 5));
        monthPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Day headers
        String[] dayNames = {"CN", "T2", "T3", "T4", "T5", "T6", "T7"};
        for (String day : dayNames) {
            JLabel lblDay = new JLabel(day, SwingConstants.CENTER);
            lblDay.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lblDay.setForeground(new Color(128, 128, 128));
            monthPanel.add(lblDay);
        }
        
        // Get appointments for the month
        LocalDateTime startOfMonth = currentDate.withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = currentDate.withDayOfMonth(
            currentDate.lengthOfMonth()).atTime(23, 59, 59);
        
        List<LichHen> appointments = controller.filterLichHen(
            startOfMonth, endOfMonth, null, null);
        
        // First day of month
        LocalDate firstDay = currentDate.withDayOfMonth(1);
        int dayOfWeek = firstDay.getDayOfWeek().getValue() % 7; // Sunday = 0
        
        // Empty cells for days before first day of month
        for (int i = 0; i < dayOfWeek; i++) {
            monthPanel.add(new JPanel());
        }
        
        // Days of month
        int daysInMonth = currentDate.lengthOfMonth();
        for (int day = 1; day <= daysInMonth; day++) {
            JPanel dayPanel = createDayPanel(day, appointments);
            monthPanel.add(dayPanel);
        }
        
        return monthPanel;
    }
    
    private JPanel createDayPanel(int day, List<LichHen> appointments) {
        JPanel dayPanel = new JPanel(new BorderLayout());
        dayPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        dayPanel.setPreferredSize(new Dimension(100, 80));
        
        // Day number
        JLabel lblDay = new JLabel(String.valueOf(day), SwingConstants.CENTER);
        lblDay.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Check if today
        LocalDate today = LocalDate.now();
        if (currentDate.getYear() == today.getYear() && 
            currentDate.getMonth() == today.getMonth() && 
            day == today.getDayOfMonth()) {
            lblDay.setForeground(Color.RED);
            dayPanel.setBackground(new Color(255, 240, 240));
        }
        
        // Appointment list
        JPanel apptPanel = new JPanel();
        apptPanel.setLayout(new BoxLayout(apptPanel, BoxLayout.Y_AXIS));
        apptPanel.setOpaque(false);
        
        // Filter appointments for this day
        LocalDate currentDay = currentDate.withDayOfMonth(day);
        for (LichHen lh : appointments) {
            if (lh.getThoiGian().toLocalDate().equals(currentDay)) {
                JLabel lblAppt = new JLabel(lh.getTenThuCung() + " - " + 
                    lh.getThoiGian().toLocalTime().format(
                        java.time.format.DateTimeFormatter.ofPattern("HH:mm")));
                lblAppt.setFont(new Font("Segoe UI", Font.PLAIN, 9));
                lblAppt.setForeground(getStatusColor(lh.getTrangThai()));
                apptPanel.add(lblAppt);
            }
        }
        
        dayPanel.add(lblDay, BorderLayout.NORTH);
        dayPanel.add(apptPanel, BorderLayout.CENTER);
        
        return dayPanel;
    }
    
    private Color getStatusColor(String status) {
        switch (status) {
            case "Chờ xác nhận": return new Color(255, 140, 0);
            case "Đã xác nhận": return new Color(0, 100, 255);
            case "Đã hoàn thành": return new Color(0, 128, 0);
            case "Đã hủy": return Color.RED;
            default: return Color.BLACK;
        }
    }
    
    private String getMonthDisplay() {
        return currentDate.format(java.time.format.DateTimeFormatter.ofPattern("MMMM yyyy"));
    }
}