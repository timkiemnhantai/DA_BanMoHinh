package com.poly.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
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
    @JsonBackReference // tránh vòng lặp khi serialize JSON
    private BienTheSanPham chiTietSanPham;

    @Column(name = "URLAnhCT")
    private String urlAnhCT;
}
