package com.poly.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.entity.BienTheGiamGiaSPId;
import com.poly.entity.BienThe_GiamGiaSP;
@Repository
public interface BienTheGiamGiaSPRepository extends JpaRepository<BienThe_GiamGiaSP, BienTheGiamGiaSPId> {


}
