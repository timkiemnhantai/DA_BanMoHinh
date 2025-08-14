package com.poly.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
