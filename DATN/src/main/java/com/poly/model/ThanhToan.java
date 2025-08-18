package com.poly.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ThanhToan")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThanhToan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaThanhToan")
    private Integer maThanhToan;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MaDH", unique = true)
    @JsonBackReference // tránh vòng lặp JSON với DonHang
    private DonHang donHang;

    @Column(name = "SoTien")
    private BigDecimal soTien;

    @Column(name = "NgayThanhToan")
    private LocalDateTime ngayThanhToan;

    @Column(name = "PhuongThucTT")
    private String phuongThucTT;

    @Column(name = "TrangThai")
    private String trangThai;

    @Column(name = "GhiChuTT")
    private String ghiChuTT;
}
