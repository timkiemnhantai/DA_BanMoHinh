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

import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "GioHang")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GioHang {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MaGH")
	private Integer maGH;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "MaTK") // đây là khóa ngoại trỏ đến bảng TaiKhoan
	private TaiKhoan taiKhoan;

	@Column(name = "TongTien")
	private BigDecimal tongTien;

	@Column(name = "TrangThaiGH")
	private String trangThaiGH;

	@Column(name = "NgayTao")
	private LocalDateTime ngayTao;
}
