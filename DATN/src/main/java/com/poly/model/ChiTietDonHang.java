package com.poly.model;

import java.math.BigDecimal;

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
@Table(name = "ChiTietDonHang")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietDonHang {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MaCTDH")
	private Integer maCTDH;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "MaDH")
	private DonHang donHang;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "MaCTSP")
	private BienTheSanPham bienTheSanPham;

	@Column(name = "SoLuongSP")
	private Integer soLuongSP;

	@Column(name = "DonGia")
	private BigDecimal donGia;

	@Column(name = "GiamGiaThucTe")
	private BigDecimal giamGiaThucTe;

	@Column(name = "ThanhTien")
	private BigDecimal thanhTien;

	@Column(name = "GhiChu")
	private String ghiChu;
	
	
}
