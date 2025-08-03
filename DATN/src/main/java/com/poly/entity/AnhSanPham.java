package com.poly.entity;

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
@Table(name = "AnhSanPham")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnhSanPham {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MaAnhSanPham")
	private Integer maAnhSanPham;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "MaSP")
	private SanPham sanPham;

	@Column(name = "URLAnhSP")
	private String urlAnhSP;
}
