package com.poly.model;

import com.poly.util.SoDienThoaiUtils;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "DiaChi")
public class DiaChi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaDiaChi")
    private Integer maDiaChi;

    // ✅ Thêm 2 cột mới
    @Column(name = "HoTen")
    private String hoTen;

    @Column(name = "SoDienThoai")
    private String soDienThoai;
    
    @Column(name = "Tinh", nullable = true)
    private String tinh;

    @Column(name = "Quan", nullable = true)
    private String quan;

    @Column(name = "Phuong", nullable = true)
    private String phuong;

    @Column(name = "DiaChiChiTiet", nullable = true)
    private String diaChiChiTiet;

    @Column(name = "MacDinh")
    private Boolean macDinh = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaTK", nullable = false)
    private TaiKhoan taiKhoan;

    @Transient
    public String getDiaChiDayDu() {
        StringBuilder diaChi = new StringBuilder();

        if (diaChiChiTiet != null && !diaChiChiTiet.trim().isEmpty()) {
            diaChi.append(diaChiChiTiet).append(", ");
        }
        if (phuong != null && !phuong.trim().isEmpty()) {
            diaChi.append(phuong).append(", ");
        }
        if (quan != null && !quan.trim().isEmpty()) {
            diaChi.append(quan).append(", ");
        }
        if (tinh != null && !tinh.trim().isEmpty()) {
            diaChi.append(tinh);
        }

        return diaChi.toString().replaceAll(", $", ""); // Xoá dấu phẩy cuối nếu có
    }
    @Transient
    public String getSoDienThoaiQuocTe() {
        return SoDienThoaiUtils.chuyenDinhDangQuocTe(this.soDienThoai);
    }

}
