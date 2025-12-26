// file: com/petcarex/service/LichHenService.java
package com.petcarex.service;

import com.petcarex.dao.LichHenDAO;
import com.petcarex.dao.ThuCungDAO;
import com.petcarex.dao.NhanVienDAO;
import com.petcarex.dao.DichVuDAO;
import com.petcarex.model.LichHen;
import com.petcarex.model.ThuCung;
import com.petcarex.model.NhanVien;
import com.petcarex.model.DichVu;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class LichHenService {
    private final LichHenDAO lichHenDAO;
    private final ThuCungDAO thuCungDAO;
    private final NhanVienDAO nhanVienDAO;
    private final DichVuDAO dichVuDAO;
    
    public LichHenService() {
        this.lichHenDAO = new LichHenDAO();
        this.thuCungDAO = new ThuCungDAO();
        this.nhanVienDAO = new NhanVienDAO();
        this.dichVuDAO = new DichVuDAO();
    }
    
    public LichHen datLich(LichHen lichHen) throws Exception {
        validateLichHen(lichHen);
        checkTimeSlotAvailability(lichHen);
        
        lichHenDAO.create(lichHen);
        
        // Lấy lịch hẹn vừa tạo
        List<LichHen> ds = lichHenDAO.findByThuCung(lichHen.getMaThuCung());
        for (LichHen lh : ds) {
            if (lh.getThoiGian().equals(lichHen.getThoiGian()) &&
                lh.getLoaiLichHen().equals(lichHen.getLoaiLichHen())) {
                return lh;
            }
        }
        return null;
    }
    
    public LichHen timLichHen(int maLichHen) throws SQLException {
        return lichHenDAO.read(maLichHen);
    }
    
    public List<LichHen> layLichHenCuaKhachHang(int maKH) throws SQLException {
        return lichHenDAO.findByKhachHang(maKH);
    }
    
    public List<LichHen> layLichHenCuaThuCung(int maThuCung) throws SQLException {
        return lichHenDAO.findByThuCung(maThuCung);
    }
    
    public List<LichHen> layLichHenTheoChiNhanh(int maChiNhanh) throws SQLException {
        return lichHenDAO.findByChiNhanh(maChiNhanh);
    }
    
    public List<LichHen> layLichHenTheoBacSi(int maBacSi) throws SQLException {
        return lichHenDAO.findByBacSi(maBacSi);
    }
    
    public List<LichHen> layLichHenTheoTrangThai(String trangThai) throws SQLException {
        return lichHenDAO.findByTrangThai(trangThai);
    }
    
    public List<LichHen> layLichHenTuNgayDenNgay(LocalDateTime from, LocalDateTime to) throws SQLException {
        return lichHenDAO.findByDateRange(from, to);
    }
    
    public void capNhatLichHen(LichHen lichHen) throws Exception {
        validateLichHen(lichHen);
        lichHenDAO.update(lichHen);
    }
    
    public void huyLichHen(int maLichHen) throws SQLException {
        lichHenDAO.cancelAppointment(maLichHen);
    }
    
    public void xacNhanLichHen(int maLichHen) throws SQLException {
        lichHenDAO.confirmAppointment(maLichHen);
    }
    
    public void hoanThanhLichHen(int maLichHen) throws SQLException {
        lichHenDAO.completeAppointment(maLichHen);
    }
    
//    public void xoaLichHen(int maLichHen) throws SQLException {
//        lichHenDAO.delete(maLichHen);
//    }
    
    // Thêm vào LichHenService.java
    public void xoaLichHen(int maLichHen) throws SQLException {
        LichHen lichHen = lichHenDAO.read(maLichHen);
        if (lichHen != null) {
            // Chỉ xóa nếu đã hoàn thành hoặc đã hủy
            if ("Đã hoàn thành".equals(lichHen.getTrangThai()) || 
                "Đã hủy".equals(lichHen.getTrangThai())) {
                lichHenDAO.delete(maLichHen);
            } else {
                throw new SQLException("Chỉ có thể xóa lịch hẹn đã hoàn thành hoặc đã hủy");
            }
        }
    }
    
    // Lấy danh sách bác sĩ khả dụng
    public List<NhanVien> layBacSiKhaDung(int maChiNhanh, LocalDateTime thoiGian) throws SQLException {
        List<NhanVien> tatCaBacSi = nhanVienDAO.findByChucVu("Bác sĩ thú y");
        List<NhanVien> bacSiTrongChiNhanh = nhanVienDAO.findByChiNhanh(maChiNhanh);
        
        // Filter bác sĩ có lịch trùng
        List<LichHen> lichHenTrung = lichHenDAO.findByDateRange(
            thoiGian.minusHours(1), 
            thoiGian.plusHours(1)
        );
        
        return bacSiTrongChiNhanh.stream()
            .filter(bacSi -> !coLichTrung(bacSi.getMaNV(), lichHenTrung))
            .collect(java.util.stream.Collectors.toList());
    }
    
    private boolean coLichTrung(int maBacSi, List<LichHen> lichHenList) {
        for (LichHen lh : lichHenList) {
            if (lh.getMaBacSi() != null && lh.getMaBacSi() == maBacSi) {
                return true;
            }
        }
        return false;
    }
    
    private void validateLichHen(LichHen lichHen) throws Exception {
        if (lichHen.getMaThuCung() <= 0) {
            throw new Exception("Vui lòng chọn thú cưng");
        }
        
        if (lichHen.getLoaiLichHen() == null || lichHen.getLoaiLichHen().isEmpty()) {
            throw new Exception("Vui lòng chọn loại lịch hẹn");
        }
        
        if (lichHen.getThoiGian() == null) {
            throw new Exception("Vui lòng chọn thời gian");
        }
        
        if (lichHen.getThoiGian().isBefore(LocalDateTime.now())) {
            throw new Exception("Thời gian không được trong quá khứ");
        }
        
        // Kiểm tra thú cưng tồn tại
        ThuCung thuCung = thuCungDAO.read(lichHen.getMaThuCung());
        if (thuCung == null) {
            throw new Exception("Thú cưng không tồn tại");
        }
    }
    
    private void checkTimeSlotAvailability(LichHen lichHen) throws Exception {
        // Kiểm tra xem thú cưng đã có lịch hẹn cùng thời điểm chưa
        List<LichHen> lichHenThuCung = lichHenDAO.findByThuCung(lichHen.getMaThuCung());
        
        for (LichHen lh : lichHenThuCung) {
            if (lh.getThoiGian().equals(lichHen.getThoiGian()) && 
                !"Đã hủy".equals(lh.getTrangThai())) {
                throw new Exception("Thú cưng đã có lịch hẹn vào thời gian này");
            }
        }
        
        // Kiểm tra bác sĩ nếu có chọn
        if (lichHen.getMaBacSi() != null) {
            List<LichHen> lichHenBacSi = lichHenDAO.findByBacSi(lichHen.getMaBacSi());
            for (LichHen lh : lichHenBacSi) {
                if (lh.getThoiGian().equals(lichHen.getThoiGian()) && 
                    !"Đã hủy".equals(lh.getTrangThai())) {
                    throw new Exception("Bác sĩ đã có lịch hẹn vào thời gian này");
                }
            }
        }
    }
}