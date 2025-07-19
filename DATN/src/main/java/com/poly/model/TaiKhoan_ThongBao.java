package com.poly.model;



import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TaiKhoan_ThongBao")
@IdClass(TaiKhoanThongBaoId.class)
public class TaiKhoan_ThongBao {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaTK")
    private TaiKhoan taiKhoan;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaThongBao")
    private ThongBao thongBao;

    @Column(name = "DaDoc")
    private Boolean daDoc;

    @Column(name = "NgayNhan")
    private LocalDateTime ngayNhan;
}

