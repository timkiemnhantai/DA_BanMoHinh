package com.poly.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.entity.GioHang;
import com.poly.entity.TaiKhoan;
@Repository
public interface GioHangRepository extends JpaRepository<GioHang, Integer>{
    Optional<GioHang> findByTaiKhoan(TaiKhoan taiKhoan);
}
