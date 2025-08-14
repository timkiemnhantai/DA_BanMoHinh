package com.poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.model.MoTaSanPham;

public interface MoTaSanPhamRepository extends JpaRepository<MoTaSanPham, Integer> {
    MoTaSanPham findBySanPham_MaSP(Integer maSP);
}
