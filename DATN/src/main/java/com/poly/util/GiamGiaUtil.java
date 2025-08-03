package com.poly.util;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.poly.dto.GiaGiamDTO;
import com.poly.model.BienTheSanPham;
import com.poly.model.BienThe_GiamGiaSP;
import com.poly.model.GiamGiaSP;

@Component
public class GiamGiaUtil {
    public GiaGiamDTO tinhGiaSauGiam(BienTheSanPham bienThe) {
    	Integer phanTramGiam = null;
        Long giaGiam = null;
        BigDecimal giaSauGiam = bienThe.getGia();

        if (bienThe.getGiamGiaSPList() != null) {
            GiamGiaSP giamGiaSP = bienThe.getGiamGiaSPList().stream()
                .map(BienThe_GiamGiaSP::getGiamGiaSP)
                .filter(gg -> "Đang hoạt động".equalsIgnoreCase(gg.getTrangThai()) &&
                        gg.getThoiGianBatDau().isBefore(LocalDateTime.now()) &&
                        gg.getThoiGianKetThuc().isAfter(LocalDateTime.now()))
                .findFirst().orElse(null);

            if (giamGiaSP != null) {
                if (giamGiaSP.getPhanTramGiam() != null) {
                    phanTramGiam = giamGiaSP.getPhanTramGiam();
                    giaSauGiam = bienThe.getGia()
                        .multiply(BigDecimal.valueOf(1 - phanTramGiam / 100.0));
                } else if (giamGiaSP.getGiaGiam() != null) {
                    giaGiam = giamGiaSP.getGiaGiam();
                    giaSauGiam = bienThe.getGia()
                        .subtract(BigDecimal.valueOf(giaGiam));
                }
            }
        }

        return new GiaGiamDTO(phanTramGiam, giaGiam, giaSauGiam);
    }
}
