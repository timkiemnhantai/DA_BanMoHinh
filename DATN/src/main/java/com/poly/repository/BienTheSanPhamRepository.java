package com.poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poly.model.BienTheSanPham;
@Repository
public interface BienTheSanPhamRepository extends JpaRepository<BienTheSanPham, Integer>{
    List<BienTheSanPham> findBySanPham_MaSP(Integer maSP);
    @Query("""
    	    SELECT bt FROM BienTheSanPham bt
    	    LEFT JOIN FETCH bt.giamGiaSPList
    	    WHERE bt.sanPham.maSP = :maSP
    	""")
    List<BienTheSanPham> findBySanPham_MaSP_FetchGiamGia(@Param("maSP") Integer maSP);
}
