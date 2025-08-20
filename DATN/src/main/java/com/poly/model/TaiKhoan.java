package com.poly.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TaiKhoan")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaiKhoan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaTK")
    private Integer maTK;

    @Column(name = "TenDangNhap")
    private String tenDangNhap;

    @Column(name = "MatKhau")
    private String matKhau;

    @Column(name = "HoTen")
    private String hoTen;

    @Column(name = "GioiTinh")
    private Integer gioiTinh;

    @Column(name = "NgaySinh")
    private LocalDate ngaySinh;

    @Column(name = "SoDT")
    private String soDT;

    @Column(name = "Email")
    private String email;

    @Column(name = "Avatar")
    private String avatar;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MaVaiTro")
    @JsonBackReference // tránh vòng lặp với VaiTro
    private VaiTro vaiTro;

    @OneToOne(mappedBy = "taiKhoan", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    @JsonManagedReference // serialize một chiều GioHang
    private GioHang gioHang;

    @OneToMany(mappedBy = "taiKhoan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // serialize một chiều danhSachDiaChi
    private List<DiaChi> danhSachDiaChi;

    @OneToMany(mappedBy = "taiKhoan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // serialize một chiều thongBaos
    private List<ThongBao> thongBaos;

    public DiaChi getDiaChiMacDinh() {
        if (danhSachDiaChi != null) {
            for (DiaChi diaChi : danhSachDiaChi) {
                if (Boolean.TRUE.equals(diaChi.getMacDinh())) {
                    return diaChi;
                }
            }
        }
        return null;
    }
}
