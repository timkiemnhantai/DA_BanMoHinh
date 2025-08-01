package com.poly.model;

import java.time.LocalDateTime;

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
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ThongBao")
public class ThongBao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaThongBao")
    private Integer maThongBao;

    @Column(name = "TieuDe")
    private String tieuDe;

    @Column(name = "NoiDung", columnDefinition = "NVARCHAR(MAX)")
    private String noiDung;

    @Column(name = "Url")
    private String url;
    
    @Column(name = "DaDoc")
    private Boolean daDoc = false;

    @Column(name = "ThoiGianTao")
    private LocalDateTime ngayTao = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MaTK")
    private TaiKhoan taiKhoan;
}
