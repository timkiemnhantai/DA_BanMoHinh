package com.poly.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonHangTrangThaiDTO {
    private Integer maDH;
    private String tenNguoiDung;
    private String tenTrangThai;
    private LocalDateTime ngayDat;
    private BigDecimal thanhTien;
}
