package com.poly.model;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ChiTietDonHang")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietDonHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaCTDH")
    private Integer maCTDH;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MaDH")
    @JsonBackReference // tránh vòng lặp JSON với DonHang
    private DonHang donHang;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MaCTSP")
    @JsonBackReference // tránh vòng lặp JSON với BienTheSanPham
    private BienTheSanPham bienTheSanPham;

    @Column(name = "SoLuongSP")
    private Integer soLuongSP;

    @Column(name = "DonGia")
    private BigDecimal donGia;

    @Column(name = "GiamGiaThucTe")
    private BigDecimal giamGiaThucTe;

    @Column(name = "ThanhTien")
    private BigDecimal thanhTien;

    @Column(name = "GhiChu")
    private String ghiChu;
}
