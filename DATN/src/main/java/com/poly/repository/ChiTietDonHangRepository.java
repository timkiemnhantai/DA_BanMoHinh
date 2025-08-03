package com.poly.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.poly.entity.ChiTietDonHang;
import com.poly.entity.DonHang;
@Repository
public interface ChiTietDonHangRepository extends JpaRepository<ChiTietDonHang, Integer> {

    List<ChiTietDonHang> findByDonHang_MaDH(Integer maDH);
    List<ChiTietDonHang> findByDonHang(DonHang donHang);
}
