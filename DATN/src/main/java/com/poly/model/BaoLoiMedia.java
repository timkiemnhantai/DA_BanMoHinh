package com.poly.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "BaoLoiMedia")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaoLoiMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaMedia")
    private Integer maMedia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaBaoLoi", nullable = false)
    private BaoLoi baoLoi;

    @Column(name = "Url", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String url;

    @Column(name = "Loai", length = 10)
    private String loai; // "image" hoặc "video"
}
