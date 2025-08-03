package com.poly.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TrangThaiDH")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrangThaiDH {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaTTDH")
    private Integer maTTDH;

    @Column(name = "TenTTDH")
    private String tenTTDH;
}
