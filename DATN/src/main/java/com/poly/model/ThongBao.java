package com.poly.model;



import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ThongBao")
public class ThongBao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaThongBao")
    private Integer maThongBao;

    @Column(name = "TieuDe", nullable = false)
    private String tieuDe;

    @Column(name = "NoiDung", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String noiDung;

    @Column(name = "NgayTao")
    private LocalDateTime ngayTao;

    @Column(name = "LoaiThongBao")
    private String loaiThongBao;

    @Column(name = "UrlLienKet")
    private String urlLienKet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaNguoiGui")
    private TaiKhoan nguoiGui;
}
