package com.poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.model.TrangThaiKH;
@Repository
public interface TrangThaiKHRepository extends JpaRepository<TrangThaiKH, Integer>{

}
