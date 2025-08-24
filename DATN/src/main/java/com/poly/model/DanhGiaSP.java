package com.poly.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "DanhGiaSP")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DanhGiaSP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaDG")
    private Integer maDG;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "MaSP")
    @JsonBackReference // tránh vòng lặp với SanPham
    private SanPham sanPham;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "MaCTSP", nullable = true)
    @JsonBackReference // tránh vòng lặp với BienTheSanPham
    private BienTheSanPham bienTheSanPham;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MaTK")
    @JsonBackReference // tránh vòng lặp với TaiKhoan
    private TaiKhoan taiKhoan;

    @Column(name = "SoSao")
    private Integer soSao;

    @Column(name = "BinhLuan")
    private String binhLuan;

    @Column(name = "NgayDang")
    private LocalDateTime ngayDang;

    @OneToMany(mappedBy = "danhGiaSP", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference // serialize mediaList an toàn
    private List<DanhGiaMedia> mediaList = new ArrayList<>();
}
