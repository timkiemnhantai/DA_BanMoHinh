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

@Entity
@Table(name = "AnhChiTiet")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnhChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaAnhChiTiet")
    private Integer maAnhChiTiet;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MaCTSP")
    private BienTheSanPham chiTietSanPham;

    @Column(name = "URLAnhCT")
    private String urlAnhCT;
}
