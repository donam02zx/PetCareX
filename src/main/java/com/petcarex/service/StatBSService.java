package com.petcarex.service;

import com.petcarex.dao.StatBSDAO;
import java.sql.SQLException;

public class StatBSService {

    private final StatBSDAO statsDAO = new StatBSDAO();

    public int getTotalAppointments(int maNV) {
        try {
            return statsDAO.getTotalAppointmentsByDoctor(maNV);
        } catch (SQLException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
            return 0;
        }
    }

    public int getTotalInjections(int maNV) {
        try {
            return statsDAO.getTotalInjectionsByDoctor(maNV);
        } catch (SQLException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
            return 0;
        }
    }

    public String getTotalExpenses(int maNV) {
        try {
            double total = statsDAO.getTotalExpensesByDoctor(maNV);
            return String.format("%,.0f", total); // formatted with commas
        } catch (SQLException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
            return "0";
        }
    }
}
