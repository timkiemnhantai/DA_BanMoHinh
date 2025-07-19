package com.poly.model;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "BienTheSanPham")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "giamGiaSPList")
public class BienTheSanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaCTSP")
    private Integer maCTSP;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MaSP")
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
    @JoinColumn(name = "MaTrangThaiKH")
    private TrangThaiKH trangThaiKH;
    
    @OneToMany(mappedBy = "bienTheSanPham", fetch = FetchType.LAZY)
    private List<BienThe_GiamGiaSP> giamGiaSPList;
    
    @OneToMany(mappedBy = "bienTheSanPham", fetch = FetchType.EAGER)
    private List<ChiTietDonHang> chiTietDonHangs;
    
    @OneToMany(mappedBy = "chiTietSanPham", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<AnhChiTiet> anhChiTietList;
//    @ManyToMany
//    @JoinTable(
//        name = "BienThe_GiamGiaSP", // tên bảng trung gian
//        joinColumns = @JoinColumn(name = "MaCTSP"),
//        inverseJoinColumns = @JoinColumn(name = "MaGiamGia")
//    )
//    private List<GiamGiaSP> danhSachGiamGia;


}
