package com.petcarex.controller;

import com.petcarex.service.StatBSService;

import java.util.HashMap;
import java.util.Map;

public class StatBSController {

    private final StatBSService statsService = new StatBSService();

    public Map<String, String> getStatsByDoctor(int maNV) {
        Map<String, String> stats = new HashMap<>();
        stats.put("appointments", String.valueOf(statsService.getTotalAppointments(maNV)));
        stats.put("injections", String.valueOf(statsService.getTotalInjections(maNV)));
        stats.put("expenses", statsService.getTotalExpenses(maNV));
        return stats;
    }
}
