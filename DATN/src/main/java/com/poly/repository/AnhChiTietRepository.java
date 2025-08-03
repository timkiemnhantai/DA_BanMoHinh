package com.poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.entity.AnhChiTiet;
@Repository
public interface AnhChiTietRepository extends JpaRepository<AnhChiTiet, Integer> {
    List<AnhChiTiet> findByChiTietSanPham_MaCTSP(Integer maCTSP);
}
