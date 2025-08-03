package com.poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.model.VaiTro;
@Repository
public interface VaiTroRepository extends JpaRepository<VaiTro, Integer>{
	VaiTro findByTenVaiTro(String tenVaiTro);
}
