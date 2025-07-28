package com.poly.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.model.TaiKhoan;
@Repository
public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, Integer>{
	TaiKhoan findByEmailOrTenDangNhap(String email, String tenDangNhap);
	boolean existsByEmailOrTenDangNhap(String email, String tenDangNhap);
    List<TaiKhoan> findByVaiTro_MaVaiTroIn(List<Integer> vaiTroIds);
	Optional <TaiKhoan> findByEmail(String email);
	boolean existsByEmail(String email);
	boolean existsByTenDangNhap(String tenDangNhap);
}