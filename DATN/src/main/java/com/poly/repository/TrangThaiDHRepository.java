package com.poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.model.TrangThaiDH;
@Repository
public interface TrangThaiDHRepository extends JpaRepository<TrangThaiDH, Integer>{

}
