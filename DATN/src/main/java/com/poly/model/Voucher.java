package com.poly.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Voucher")
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaVoucher")
    private Integer maVoucher;

    @Column(name = "MaCode", nullable = false, unique = true)
    private String maCode;

    @Column(name = "TenGiamGia")
    private String tenGiamGia;

    @Column(name = "PhanTramGiam", nullable = false)
    private Integer phanTramGiam;

    @Column(name = "SoLuong", nullable = false)
    private Integer soLuong;

    @Column(name = "DieuKien")
    private Double dieuKien;

    @Column(name = "NgayBatDau")
    private LocalDate ngayBatDau;

    @Column(name = "NgayKetThuc")
    private LocalDate ngayKetThuc;

    @Column(name = "MoTa", columnDefinition = "NVARCHAR(MAX)")
    private String moTa;

    @Column(name = "TrangThai")
    private Boolean trangThai;
}