package com.poly.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.poly.model.SanPham;
@Repository
public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {

	
}
