package com.poly.service;

import java.time.LocalDateTime;
import java.util.List;

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

        for (GiamGiaSP giam : danhSach) {
            LocalDateTime batDau = giam.getThoiGianBatDau();
            LocalDateTime ketThuc = giam.getThoiGianKetThuc();

            if (batDau != null && ketThuc != null) {
                if (now.isBefore(batDau)) {
                    giam.setTrangThai("Sắp diễn ra");
                } else if (now.isAfter(ketThuc)) {
                    giam.setTrangThai("Kết thúc");
                } else {
                    giam.setTrangThai("Đang hoạt động");
                }
            }
        }

        giamGiaSPRepository.saveAll(danhSach);
    }
}
