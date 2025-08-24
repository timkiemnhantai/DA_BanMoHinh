package com.poly.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JsonBackReference // tránh vòng lặp JSON với DanhGiaSP
    private DanhGiaSP danhGiaSP;

    @Column(name = "Url", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String url;

    @Column(name = "Loai", length = 10)
    private String loai; // "image" hoặc "video"
}
