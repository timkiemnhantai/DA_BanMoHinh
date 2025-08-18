package com.poly.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "MoTaSanPham")
public class MoTaSanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaMoTa")
    private Integer maMoTa;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MaSP", nullable = false)
    @JsonBackReference // tránh vòng lặp JSON với SanPham
    private SanPham sanPham;

    @Column(name = "ChatLieu")
    private String chatLieu;

    @Column(name = "TrongLuong")
    private String trongLuong;

    @Column(name = "LoaiBaoHanh")
    private String loaiBaoHanh;

    @Column(name = "KichThuoc")
    private String kichThuoc;

    @Column(name = "GuiTu")
    private String guiTu;

    @Column(name = "TiLe")
    private String tiLe;

    @Column(name = "XuatXu")
    private String xuatXu;

    @Column(name = "TrinhDo")
    private String trinhDo;
}
