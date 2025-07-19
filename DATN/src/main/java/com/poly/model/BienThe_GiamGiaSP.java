package com.poly.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "BienThe_GiamGiaSP")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BienThe_GiamGiaSP {

    @EmbeddedId
    private BienTheGiamGiaSPId id;

    @ManyToOne
    @MapsId("maCTSP")
    @JoinColumn(name = "MaCTSP")
    @ToString.Exclude
    private BienTheSanPham bienTheSanPham;

    @ManyToOne
    @MapsId("maGiamGia")
    @JoinColumn(name = "MaGiamGia")
    @ToString.Exclude
    private GiamGiaSP giamGiaSP;
}


