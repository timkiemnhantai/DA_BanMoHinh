package com.poly.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poly.model.GiamGiaSP;
import com.poly.repository.GiamGiaSPRepository;

@Service
public class GiamGiaSPService {
	
    private final GiamGiaSPRepository giamGiaSPRepository;

    public GiamGiaSPService(GiamGiaSPRepository giamGiaSPRepository) {
        this.giamGiaSPRepository = giamGiaSPRepository;
    }

    @Transactional
    public void capNhatTrangThaiGiamGia() {
        List<GiamGiaSP> danhSach = giamGiaSPRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        if (danhSach == null || danhSach.isEmpty()) {
            return;
        }

        List<GiamGiaSP> needSave = new ArrayList<>();

        for (GiamGiaSP giam : danhSach) {
            LocalDateTime batDau = giam.getThoiGianBatDau();
            LocalDateTime ketThuc = giam.getThoiGianKetThuc();

            String newStatus = null;

            if (batDau != null && ketThuc != null) {
                if (now.isBefore(batDau)) {
                    newStatus = "Sắp diễn ra";
                } else if (now.isAfter(ketThuc)) {
                    newStatus = "Kết thúc";
                } else {
                    newStatus = "Đang hoạt động";
                }
            } else if (batDau != null) {
                // only start time known
                newStatus = now.isBefore(batDau) ? "Sắp diễn ra" : "Đang hoạt động";
            } else if (ketThuc != null) {
                // only end time known
                newStatus = now.isAfter(ketThuc) ? "Kết thúc" : (giam.getTrangThai() != null ? giam.getTrangThai() : "Đang hoạt động");
            } else {
                // no times — giữ trạng thái hiện tại (không ép thay đổi)
                newStatus = giam.getTrangThai() != null ? giam.getTrangThai() : "Đang hoạt động";
            }

            String oldStatus = giam.getTrangThai();
            if (!Objects.equals(oldStatus, newStatus)) {
                giam.setTrangThai(newStatus);
                needSave.add(giam);
            }
        }

        if (!needSave.isEmpty()) {
            giamGiaSPRepository.saveAll(needSave);
        }
    }
    
}
