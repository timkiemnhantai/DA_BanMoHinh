package com.poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.model.DanhGiaSP;
@Repository
public interface DanhGiaSPRepository extends JpaRepository<DanhGiaSP, Integer> {
    List<DanhGiaSP> findBySanPham_MaSP(Integer maSP);
}
