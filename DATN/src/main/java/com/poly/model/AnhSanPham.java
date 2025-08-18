package com.poly.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "AnhSanPham")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnhSanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaAnhSanPham")
    private Integer maAnhSanPham;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MaSP")
    @JsonBackReference // tránh vòng lặp khi serialize JSON
    private SanPham sanPham;

    @Column(name = "URLAnhSP")
    private String urlAnhSP;
}
