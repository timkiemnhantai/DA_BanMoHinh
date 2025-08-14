package com.poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poly.model.DanhGiaSP;
@Repository
public interface DanhGiaSPRepository extends JpaRepository<DanhGiaSP, Integer> {
    List<DanhGiaSP> findBySanPham_MaSP(Integer maSP);
    @Query("SELECT DISTINCT dg FROM DanhGiaSP dg " +
    	       "LEFT JOIN FETCH dg.mediaList " +
    	       "WHERE dg.sanPham.maSP = :maSP " +
    	       "ORDER BY dg.ngayDang DESC")
    	List<DanhGiaSP> findDanhGiaWithMediaBySanPham(@Param("maSP") Integer maSP);



}
