package com.poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.entity.LoaiSanPham;
@Repository
public interface LoaiSanPhamRepository extends JpaRepository<LoaiSanPham, Integer>{

}
