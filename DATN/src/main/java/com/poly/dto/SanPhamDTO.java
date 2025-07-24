package com.poly.dto;

import java.math.BigDecimal;

import com.poly.model.SanPham;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SanPhamDTO {
    private SanPham sanPham;
    private Integer phanTramGiam;
    private Long giaGiam;
    private BigDecimal giaSauGiam;
    private Long tongSoLuongBan;

    // Tiêu chí lọc
    private String keyword;
    private String loaiKeyword;
    private String sapXep;
    private Boolean giamGia;
	private Boolean banChay;
    private Double diemTrungBinhDanhGia = 0.0;
    public SanPhamDTO(SanPham sp, Integer phanTramGiam, Long giaGiam, BigDecimal giaSauGiam, Long tongSoLuongBan, Double diemTrungBinhDanhGia) {
        this.sanPham = sp;
        this.phanTramGiam = phanTramGiam;
        this.giaGiam = giaGiam;
        this.giaSauGiam = giaSauGiam;
        this.tongSoLuongBan = tongSoLuongBan;
        this.diemTrungBinhDanhGia = diemTrungBinhDanhGia;
    }

}
