package com.poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.entity.AnhSanPham;
@Repository
public interface AnhSanPhamRepository extends JpaRepository<AnhSanPham, Integer> {
    List<AnhSanPham> findBySanPham_MaSP(Integer maSP);
}
