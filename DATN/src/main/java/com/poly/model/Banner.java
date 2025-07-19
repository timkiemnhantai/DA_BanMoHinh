package com.poly.model;

import java.time.LocalDate;
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
@Table(name = "Banner")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Banner {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MaBanner")
	private Integer maBanner;

	@Column(name = "TieuDeBanner")
	private String tieuDeBanner;

	@Column(name = "AnhBanner")
	private String anhBanner;

	@Column(name = "Link")
	private String link;

	@Column(name = "LoaiBanner")
	private String loaiBanner;

	@Column(name = "NgayTao")
	private LocalDate ngayTao;

	@Column(name = "NgayKetThuc")
	private LocalDate ngayKetThuc;
}
