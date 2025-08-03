package com.poly.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TrangThaiKH")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrangThaiKH {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MaTrangThaiKH")
	private Integer maTrangThaiKH;

	@Column(name = "TenTrangThai")
	private String tenTrangThai;
}
