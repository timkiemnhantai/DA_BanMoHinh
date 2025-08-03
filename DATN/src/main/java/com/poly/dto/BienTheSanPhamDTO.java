package com.poly.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.poly.entity.AnhChiTiet;
import com.poly.entity.BienTheSanPham;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BienTheSanPhamDTO {
	private BienTheSanPham bienThe;
    private List<AnhChiTiet> dsAnhChiTiet;
    private Integer phanTramGiam;
    private Long giaGiam;
    private BigDecimal giaSauGiam;
}
