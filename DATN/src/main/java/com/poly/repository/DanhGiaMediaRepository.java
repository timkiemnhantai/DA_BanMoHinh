package com.poly.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.poly.model.DanhGiaMedia;


@Repository
public interface DanhGiaMediaRepository extends JpaRepository<DanhGiaMedia, Integer>{

}
