package com.poly.model;

import java.time.LocalDate;
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
@Table(name = "TaiKhoan")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaiKhoan {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MaTK")
	private Integer maTK;

	@Column(name = "TenDangNhap")
	private String tenDangNhap;

	@Column(name = "MatKhau")
	private String matKhau;

	@Column(name = "HoTen")
	private String hoTen;

	@Column(name = "GioiTinh")
	private Integer gioiTinh;

	@Column(name = "NgaySinh")
	private LocalDate ngaySinh;

	@Column(name = "SoDT")
	private String soDT;

	@Column(name = "Email")
	private String email;


	@Column(name = "Avatar")
	private String avatar;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "MaVaiTro")
	private VaiTro vaiTro;
	
	@OneToOne(mappedBy = "taiKhoan", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
	private GioHang gioHang;
	
	@OneToMany(mappedBy = "taiKhoan", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DiaChi> danhSachDiaChi;

	public DiaChi getDiaChiMacDinh() {
	    if (danhSachDiaChi != null) {
	        for (DiaChi diaChi : danhSachDiaChi) {
	            if (Boolean.TRUE.equals(diaChi.getMacDinh())) {
	                return diaChi;
	            }
	        }
	    }
	    return null;
	}
	@OneToMany(mappedBy = "taiKhoan", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ThongBao> thongBaos;

	
}
