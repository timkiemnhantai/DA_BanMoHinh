package com.poly.model;


import java.time.LocalDateTime;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "GiamGiaSP")

@Data
//@ToString(exclude = "danhSachBienThe") 
@NoArgsConstructor
@AllArgsConstructor
public class GiamGiaSP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaGiamGia")
    private Integer maGiamGia;

    @Column(name = "TenGG")
    private String tenGG;

    @Column(name = "PhanTramGiam")
    private Integer phanTramGiam;

    @Column(name = "GiaGiam")
    private Long giaGiam;

    @Column(name = "ThoiGianBatDau")
    private LocalDateTime thoiGianBatDau;

    @Column(name = "ThoiGianKetThuc")
    private LocalDateTime thoiGianKetThuc;

    @Column(name = "TrangThai")
    private String trangThai;

//    @ManyToMany(mappedBy = "danhSachGiamGia")
//    private List<BienTheSanPham> danhSachBienThe;

}
