package com.poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.model.GiamGiaSP;
@Repository
public interface GiamGiaSPRepository extends JpaRepository<GiamGiaSP, Integer>{

}
