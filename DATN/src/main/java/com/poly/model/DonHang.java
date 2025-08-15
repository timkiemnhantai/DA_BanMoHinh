package com.poly.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "DonHang")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonHang {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MaDH")
	private Integer maDH;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "MaTK")
	private TaiKhoan taiKhoan;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "MaTTDH")
	private TrangThaiDH trangThaiDH;

    @Column(name = "HoTen", nullable = false)
    private String hoTen;

    @Column(name = "SoDienThoai", nullable = false)
    private String soDienThoai;

	@Column(name = "DiaChiGiaoHang")
	private String diaChiGiaoHang;

	@Column(name = "NgayDat")
	private LocalDateTime ngayDat;

	@Column(name = "NgayGiaoDuKien")
	private String ngayGiaoDuKien;

	@Column(name = "NgayGiaoThucTe")
	private LocalDateTime ngayGiaoThucTe;

	@Column(name = "TongTienCTT")
	private BigDecimal tongTienCTT;

	@Column(name = "GiamGiaThucTe")
	private BigDecimal giamGiaThucTe;

	@Column(name = "PhiVanChuyen")
	private BigDecimal phiVanChuyen;

	@Column(name = "ThanhTien")
	private BigDecimal thanhTien;

	@Column(name = "GhiChu")
	private String ghiChu;
	
	@OneToMany(mappedBy = "donHang", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<ChiTietDonHang> chiTietDonHangs;
	@OneToOne(mappedBy = "donHang", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private ThanhToan thanhToan;

	
}
