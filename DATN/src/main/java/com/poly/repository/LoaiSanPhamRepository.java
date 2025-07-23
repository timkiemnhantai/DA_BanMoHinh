package com.poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.model.LoaiSanPham;
@Repository
public interface LoaiSanPhamRepository extends JpaRepository<LoaiSanPham, Integer>{

}
