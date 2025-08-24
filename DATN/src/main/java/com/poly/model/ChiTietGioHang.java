package com.poly.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ChiTietGioHang")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietGioHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaCTGH")
    private Integer maCTGH;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MaGH")
    @JsonBackReference // tránh vòng lặp JSON với GioHang
    private GioHang gioHang;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MaCTSP")
    @JsonBackReference // tránh vòng lặp JSON với BienTheSanPham
    private BienTheSanPham chiTietSanPham;

    @Column(name = "SoLuong")
    private Integer soLuong;

    @Column(name = "GiamGiaThucTe")
    private BigDecimal giamGiaThucTe;

    @Column(name = "GiaTienThucTe")
    private BigDecimal giaTienThucTe;

    @Column(name = "NgayThem")
    private LocalDateTime ngayThem;
}
