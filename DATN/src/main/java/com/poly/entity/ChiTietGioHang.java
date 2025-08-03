package com.poly.entity;

import java.math.BigDecimal;
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
@Table(name = "ChiTietGioHang")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietGioHang {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MaCTGH")
	private Integer maCTGH;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "MaGH")
	private GioHang gioHang;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "MaCTSP")
	private BienTheSanPham chiTietSanPham;

	@Column(name = "SoLuong")
	private Integer soLuong;

	@Column(name = "GiamGiaThucTe")
	private BigDecimal giamGiaThucTe;

	@Column(name = "GiaTienThucTe")
	private BigDecimal giaTienThucTe;

	@Column(name = "NgayThem")
	private LocalDateTime ngayThem;
}
