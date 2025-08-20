package com.poly.model;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "SanPham")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"bienTheSanPham", "anhSanPham", "danhGiaList"})
public class SanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaSP")
    private Integer maSP;

    @Column(name = "TenSP")
    private String tenSP;

    @Column(name = "TenKhongDau")
    private String tenKhongDau;

    @Column(name = "MoTaChung")
    private String moTaChung;

    @Column(name = "ThuongHieu")
    private String thuongHieu;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MaLoaiSanPham")
    @JsonBackReference // tránh vòng lặp với LoaiSanPham
    private LoaiSanPham loaiSanPham;

    @OneToMany(mappedBy = "sanPham", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference // serialize BienTheSanPham một chiều
    private List<BienTheSanPham> bienTheSanPham;

    @OneToMany(mappedBy = "sanPham", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference // serialize AnhSanPham một chiều
    private List<AnhSanPham> anhSanPham;

    @CreationTimestamp
    @Column(name = "NgayTao", updatable = false)
    private LocalDateTime ngayTao;

    @OneToMany(mappedBy = "sanPham", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference // serialize danhGiaList một chiều
    private List<DanhGiaSP> danhGiaList;
}
