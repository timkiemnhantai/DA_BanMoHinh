package com.poly.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PhanHoiDanhGia")
public class PhanHoiDanhGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaPhanHoi")
    private Integer maPhanHoi;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MaDanhGia")
    private DanhGiaSP danhGia;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MaNV")
    private TaiKhoan nhanVien;  // Đây là tài khoản có VaiTro là "Nhân viên"

    @Column(name = "NoiDung", columnDefinition = "NVARCHAR(MAX)")
    private String noiDung;

    @Column(name = "NgayPhanHoi")
    private LocalDateTime ngayPhanHoi;
}
