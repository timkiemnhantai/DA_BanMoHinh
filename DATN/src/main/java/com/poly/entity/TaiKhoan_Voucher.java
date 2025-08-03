package com.poly.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TaiKhoan_Voucher")
@IdClass(TaiKhoanVoucherId.class)
public class TaiKhoan_Voucher {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaTK")
    private TaiKhoan taiKhoan;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaVoucher")
    private Voucher voucher;

    @Column(name = "NgaySuDung")
    private LocalDateTime ngaySuDung;

    @Column(name = "TrangThaiSuDung")
    private Boolean trangThaiSuDung;
}

