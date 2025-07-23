package com.poly.dto;

import java.math.BigDecimal;

import com.poly.model.BienTheSanPham;
import com.poly.model.ChiTietGioHang;


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
}
