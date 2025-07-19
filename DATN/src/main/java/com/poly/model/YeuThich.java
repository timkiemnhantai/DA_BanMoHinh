package com.poly.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "YeuThich")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(YeuThichId.class)
public class YeuThich {
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MaSP")
    private SanPham sanPham;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MaTK")
    private TaiKhoan taiKhoan;

    @Column(name = "NgayYeuThich")
    private LocalDateTime ngayYeuThich;
}