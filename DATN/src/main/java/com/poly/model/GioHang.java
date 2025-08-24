package com.poly.model;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "GioHang")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GioHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaGH")
    private Integer maGH;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MaTK") // khóa ngoại trỏ đến TaiKhoan
    @JsonManagedReference // serialize một chiều với TaiKhoan
    private TaiKhoan taiKhoan;

    @Column(name = "TrangThaiGH")
    private String trangThaiGH;

    @Column(name = "NgayTao")
    private LocalDateTime ngayTao;
}
