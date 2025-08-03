package com.poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.entity.YeuThich;
import com.poly.entity.YeuThichId;
@Repository
public interface YeuThichRepository extends JpaRepository<YeuThich, YeuThichId>{

}
