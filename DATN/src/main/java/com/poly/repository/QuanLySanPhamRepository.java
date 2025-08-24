package com.poly.repository;

import com.poly.model.SanPham;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuanLySanPhamRepository extends JpaRepository<SanPham, Integer> {
	  // Tìm theo tên chứa keyword (không phân biệt hoa thường)
    List<SanPham> findByTenSPContainingIgnoreCase(String keyword);

    // Tìm theo loại
    List<SanPham> findByLoaiSanPham_MaLoaiSanPham(Integer maLoaiSanPham);

    // Tìm theo tên + loại
    List<SanPham> findByTenSPContainingIgnoreCaseAndLoaiSanPham_MaLoaiSanPham(String keyword, Integer maLoaiSanPham);
	
	
}
