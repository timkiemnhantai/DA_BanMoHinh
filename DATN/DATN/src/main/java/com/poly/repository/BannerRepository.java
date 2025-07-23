package com.poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.model.Banner;
@Repository
public interface BannerRepository extends JpaRepository<Banner, Integer>{

}
