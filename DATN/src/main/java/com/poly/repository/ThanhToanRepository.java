package com.poly.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.model.ThanhToan;
@Repository
public interface ThanhToanRepository extends JpaRepository<ThanhToan, Integer>{
    ThanhToan findByDonHang_MaDH(int maDH);
    Optional<ThanhToan> findByDonHang_MaDH(Integer maDH);
}
