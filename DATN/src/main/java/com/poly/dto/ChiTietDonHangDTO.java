package com.poly.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

import com.poly.model.ChiTietDonHang;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietDonHangDTO {
    private Integer maCTDH;
    private Integer maSP;      // từ BienTheSanPham -> SanPham
    private String tenSP;      // từ BienTheSanPham -> SanPham
    private Integer soLuongSP;
    private BigDecimal donGia;
    private BigDecimal giamGiaThucTe;
    private BigDecimal thanhTien;
    private String ghiChu;
    private String anhSP;
    // constructor từ entity
    public ChiTietDonHangDTO(ChiTietDonHang ct) {
        this.maCTDH = ct.getMaCTDH();
        this.soLuongSP = ct.getSoLuongSP();
        this.donGia = ct.getDonGia();
        this.giamGiaThucTe = ct.getGiamGiaThucTe();
        this.thanhTien = ct.getThanhTien();
        this.ghiChu = ct.getGhiChu();

        if (ct.getBienTheSanPham() != null && ct.getBienTheSanPham().getSanPham() != null) {
            this.maSP = ct.getBienTheSanPham().getSanPham().getMaSP();
            this.tenSP = ct.getBienTheSanPham().getSanPham().getTenSP();

            // Lấy ảnh từ biến thể trước
            if (ct.getBienTheSanPham().getAnhChiTietList() != null 
                && !ct.getBienTheSanPham().getAnhChiTietList().isEmpty()) {
                this.anhSP = ct.getBienTheSanPham().getAnhChiTietList().get(0).getUrlAnhCT();
            } 
            // Nếu không có thì lấy ảnh từ sản phẩm cha
            else if (ct.getBienTheSanPham().getSanPham().getAnhSanPham() != null 
                     && !ct.getBienTheSanPham().getSanPham().getAnhSanPham().isEmpty()) {
                this.anhSP = ct.getBienTheSanPham().getSanPham().getAnhSanPham().get(0).getUrlAnhSP();
            } 
            // Nếu cũng không có thì fallback ảnh mặc định
            else {
                this.anhSP = "/img/default.png";
            }
        }
    }

}


