package com.poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.model.BaoLoi;

public interface BaoLoiRepository extends JpaRepository<BaoLoi, Integer>{
    List<BaoLoi> findByDonHang_MaDH(Integer maDH);
}
