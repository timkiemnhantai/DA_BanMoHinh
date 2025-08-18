package com.poly.model;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "BienTheSanPham")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"giamGiaSPList", "chiTietDonHangs", "anhChiTietList"})
public class BienTheSanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaCTSP")
    private Integer maCTSP;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MaSP")
    @JsonBackReference // tránh vòng lặp JSON với SanPham
    private SanPham sanPham;

    @Column(name = "Gia")
    private BigDecimal gia;

    @Column(name = "MoTaChiTiet")
    private String moTaChiTiet;

    @Column(name = "SoLuongTonKho")
    private Integer soLuongTonKho;

    @Column(name = "PhienBan")
    private String phienBan;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MaTrangThaiKH") // <--- đây là tên cột trong database
    private TrangThaiKH trangThaiKH;

    
    @OneToMany(mappedBy = "bienTheSanPham", fetch = FetchType.LAZY)
    @JsonManagedReference // serialize 1 chiều với BienThe_GiamGiaSP
    private List<BienThe_GiamGiaSP> giamGiaSPList;
    
    @OneToMany(mappedBy = "bienTheSanPham", fetch = FetchType.EAGER)
    @JsonManagedReference // serialize 1 chiều với ChiTietDonHang
    private List<ChiTietDonHang> chiTietDonHangs;
    
    @OneToMany(mappedBy = "chiTietSanPham", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference // serialize 1 chiều với AnhChiTiet
    private List<AnhChiTiet> anhChiTietList;

    @Column(name = "SoLuongDatGiu")
    private Integer soLuongDatGiu = 0;
}
