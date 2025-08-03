package com.poly.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poly.model.BienTheSanPham;
import com.poly.model.ChiTietGioHang;
import com.poly.model.GioHang;


@Repository
public interface ChiTietGioHangRepository extends JpaRepository<ChiTietGioHang, Integer> {
	@Query("SELECT ctgh FROM ChiTietGioHang ctgh WHERE ctgh.gioHang.taiKhoan.maTK = :maTK ORDER BY ctgh.ngayThem DESC")
	List<ChiTietGioHang> findByMaTKOrderByNgayThemDesc(@Param("maTK") Integer maTK);

	Optional<ChiTietGioHang> findByGioHangAndChiTietSanPhamAndGiaTienThucTe(GioHang gioHang,
			BienTheSanPham chiTietSanPham, BigDecimal giaTienThucTe);

}
