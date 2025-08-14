package com.poly.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "DanhGiaMedia")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DanhGiaMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MediaID")
    private Integer mediaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaDG", nullable = false)
    private DanhGiaSP danhGiaSP; // Liên kết với bảng DanhGiaSP

    @Column(name = "Url", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String url;

    @Column(name = "Loai", length = 10)
    private String loai; // "image" hoặc "video"
}
