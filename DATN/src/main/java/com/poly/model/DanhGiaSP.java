package com.poly.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

@Entity
@Table(name = "DanhGiaSP")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DanhGiaSP {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MaDG")
	private Integer maDG;

	@ManyToOne(optional = false,fetch = FetchType.EAGER)
	@JoinColumn(name = "MaSP")
	private SanPham sanPham;
	
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "MaCTSP", nullable = true) // <- Optional ở đây
    private BienTheSanPham bienTheSanPham;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "MaTK")
	private TaiKhoan taiKhoan;

	@Column(name = "SoSao")
	private Integer soSao;

	@Column(name = "BinhLuan")
	private String binhLuan;

	@Column(name = "NgayDang")
	private LocalDateTime ngayDang;
	@OneToMany(mappedBy = "danhGiaSP", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<DanhGiaMedia> mediaList = new ArrayList<>();

}
