package com.poly.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "DonHang")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaDH")
    private Integer maDH;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MaTK")
    @JsonBackReference // tránh vòng lặp với TaiKhoan
    private TaiKhoan taiKhoan;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MaTTDH")
    @JsonBackReference // tránh vòng lặp với TrangThaiDH
    private TrangThaiDH trangThaiDH;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MaVoucher")
    @JsonBackReference
    private Voucher voucher;

    
    @Column(name = "HoTen", nullable = false)
    private String hoTen;

    @Column(name = "SoDienThoai", nullable = false)
    private String soDienThoai;

    @Column(name = "DiaChiGiaoHang")
    private String diaChiGiaoHang;

    @Column(name = "PhuongThucVanChuyen", nullable = false)
    private String phuongThucVanChuyen;

    @Column(name = "NgayDat")
    private LocalDateTime ngayDat;

    @Column(name = "NgayGiaoDuKien")
    private String ngayGiaoDuKien;

    @Column(name = "NgayGiaoThucTe")
    private LocalDateTime ngayGiaoThucTe;

    @Column(name = "NgayXacNhanNhanHang")
    private LocalDateTime ngayXacNhanNhanHang;

    
    @Column(name = "TongTienCTT")
    private BigDecimal tongTienCTT;

    @Column(name = "GiamGiaThucTe")
    private BigDecimal giamGiaThucTe;

    @Column(name = "PhiVanChuyen")
    private BigDecimal phiVanChuyen;

    @Column(name = "ThanhTien")
    private BigDecimal thanhTien;

    @Column(name = "GhiChu")
    private String ghiChu;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MaTTDHTruoc")
    @JsonBackReference
    private TrangThaiDH trangThaiTruoc;

    
    @OneToMany(mappedBy = "donHang", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference // serialize chiTietDonHangs an toàn
    private List<ChiTietDonHang> chiTietDonHangs;

    @OneToOne(mappedBy = "donHang", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference // serialize thanhToan an toàn
    private ThanhToan thanhToan;
}
