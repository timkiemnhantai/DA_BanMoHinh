package com.poly.dto;
import java.math.BigDecimal;
import java.util.List;
import com.poly.model.ChiTietDonHang;
import com.poly.model.DonHang;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonHang1DTO {
    private Integer maDH;
    private String hoTen;
    private String diaChiGiaoHang;
    private String soDienThoai;
    private String phuongThucVanChuyen;
    private String ghiChu;
    private BigDecimal thanhTien;
    private Integer trangThaiId;
    private String tenTrangThai;

    // chi tiết sản phẩm
    private List<ChiTietDonHangDTO> chiTietDonHangs;

    // constructor tiện lợi từ entity
    public DonHang1DTO(DonHang dh, List<ChiTietDonHang> chiTietDonHangs) {
        this.maDH = dh.getMaDH();
        this.hoTen = dh.getHoTen();
        this.diaChiGiaoHang = dh.getDiaChiGiaoHang();
        this.soDienThoai = dh.getSoDienThoai();
        this.phuongThucVanChuyen = dh.getPhuongThucVanChuyen();
        this.ghiChu = dh.getGhiChu();
        this.thanhTien = dh.getThanhTien();
        this.trangThaiId = dh.getTrangThaiDH().getMaTTDH();
        this.tenTrangThai = dh.getTrangThaiDH().getTenTTDH();

        this.chiTietDonHangs = chiTietDonHangs.stream()
                .map(ChiTietDonHangDTO::new)
                .toList();
    }
}