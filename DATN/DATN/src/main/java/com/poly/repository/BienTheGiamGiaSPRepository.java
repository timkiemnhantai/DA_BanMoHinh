package com.poly.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.model.BienThe_GiamGiaSP;
import com.poly.model.BienTheGiamGiaSPId;
@Repository
public interface BienTheGiamGiaSPRepository extends JpaRepository<BienThe_GiamGiaSP, BienTheGiamGiaSPId> {


}
