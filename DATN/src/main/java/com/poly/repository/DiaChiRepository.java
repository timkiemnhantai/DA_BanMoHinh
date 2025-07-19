package com.poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.poly.model.DiaChi;
@Repository
public interface DiaChiRepository extends JpaRepository<DiaChi, Integer> {
    List<DiaChi> findByTaiKhoan_MaTK(Integer maTK);
    
}
