package com.poly.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "LoaiSanPham")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoaiSanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaLoaiSanPham")
    private Integer maLoaiSanPham;

    @Column(name = "TenLoaiSanPham")
    private String tenLoaiSanPham;
    
    @Column(name = "TenLoaiKhongDau")
    private String tenLoaiKhongDau;
}
