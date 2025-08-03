package com.poly.entity;

import java.time.LocalDateTime;

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
}
