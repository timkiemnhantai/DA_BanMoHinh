package com.poly.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poly.model.BienTheGiamGiaSPId;
import com.poly.model.BienThe_GiamGiaSP;

import jakarta.transaction.Transactional;
@Repository
public interface BienTheGiamGiaSPRepository extends JpaRepository<BienThe_GiamGiaSP, BienTheGiamGiaSPId> {

    @Modifying
    @Transactional
    @Query("delete from BienThe_GiamGiaSP b where b.giamGiaSP.maGiamGia = :maGiamGia")
    void deleteByGiamGiaId(Integer maGiamGia);
    // trả về danh sách MaCTSP đã được gắn cho mã giảm giá
    @Query("select b.id.maCTSP from BienThe_GiamGiaSP b where b.id.maGiamGia = :maGiamGia")
    List<Integer> findMaCtspByGiamGiaId(@Param("maGiamGia") Integer maGiamGia);
}
