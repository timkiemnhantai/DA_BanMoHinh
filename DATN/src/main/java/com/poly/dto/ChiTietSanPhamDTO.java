package com.poly.dto;

import java.math.BigDecimal;
import java.util.List;

import com.poly.model.AnhChiTiet;
import com.poly.model.AnhSanPham;
import com.poly.model.BienTheSanPham;
import com.poly.model.SanPham;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietSanPhamDTO {
    private SanPham sanPham;
    private BienTheSanPham bienThe;
    private List<AnhChiTiet> dsAnhChiTiet;
    private List<AnhSanPham> dsAnhSanPham; // 👉 Thêm danh sách ảnh sản phẩm chính
    private List<BienTheSanPhamDTO> danhSachBienThe;
    private Integer phanTramGiam;
    private Long giaGiam;
    private BigDecimal giaSauGiam ;
    private Long tongSoLuongBan;
    private Double diemTrungBinh; // trung bình đánh giá (nếu dùng)
}

