package com.poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.model.YeuThich;
import com.poly.model.YeuThichId;
@Repository
public interface YeuThichRepository extends JpaRepository<YeuThich, YeuThichId>{

}
