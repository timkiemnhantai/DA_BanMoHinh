package com.poly.model;

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
@Table(name = "ThanhToan")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThanhToan {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MaThanhToan")
	private Integer maThanhToan;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "MaDH")
	private DonHang donHang;

	@Column(name = "SoTien")
	private BigDecimal soTien;

	@Column(name = "NgayThanhToan")
	private LocalDateTime ngayThanhToan;

	@Column(name = "PhuongThucTT")
	private String phuongThucTT;

	@Column(name = "TrangThai")
	private String trangThai;

	@Column(name = "GhiChuTT")
	private String ghiChuTT;
}
