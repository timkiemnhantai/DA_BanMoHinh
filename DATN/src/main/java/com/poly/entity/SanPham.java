package com.poly.entity;


import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "SanPham")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"bienTheSanPham", "anhSanPham"})
public class SanPham {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MaSP")
	private Integer maSP;

	@Column(name = "TenSP")
	private String tenSP;

    @Column(name = "TenKhongDau")
    private String tenKhongDau;
	
	@Column(name = "MoTaChung")
	private String moTaChung;

	@Column(name = "ThuongHieu")
	private String thuongHieu;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "MaLoaiSanPham")
	private LoaiSanPham loaiSanPham;
	
    @OneToMany(mappedBy = "sanPham", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<BienTheSanPham> bienTheSanPham;
    @OneToMany(mappedBy = "sanPham", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<AnhSanPham> anhSanPham;
    
    @CreationTimestamp
    @Column(name = "NgayTao", updatable = false)
    private LocalDateTime ngayTao;
    
    @OneToMany(mappedBy = "sanPham", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<DanhGiaSP> danhGiaList;
}
