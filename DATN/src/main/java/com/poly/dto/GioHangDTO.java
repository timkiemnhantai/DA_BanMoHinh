package com.poly.dto;

import java.math.BigDecimal;

import com.poly.entity.BienTheSanPham;
import com.poly.entity.ChiTietGioHang;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GioHangDTO {
	private ChiTietGioHang chiTietGioHang;
	private BienTheSanPham bienThe;
	
    private Integer phanTramGiam;
    private Long giaGiam;
    private BigDecimal giaSauGiam;
    
    public boolean isDaHetGiamGia() {
        return phanTramGiam == null && giaGiam == null;
    }
}
