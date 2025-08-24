package com.poly.repository;



import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poly.model.DonHang;
@Repository

public interface DonHangRepository extends JpaRepository<DonHang, Integer>{
	List<DonHang> findByTaiKhoan_MaTKOrderByNgayDatDesc(int maTK);
    List<DonHang> findByTrangThaiDH_MaTTDH(int maTTDH);
    List<DonHang> findByTaiKhoan_MaTKAndTrangThaiDH_MaTTDHOrderByNgayDatDesc(int maTK, int maTTDH);
    List<DonHang> findByMaDHGreaterThanOrderByNgayDatDesc(Integer afterId);
    @Query("SELECT DISTINCT dh FROM DonHang dh " +
    	       "LEFT JOIN FETCH dh.chiTietDonHangs " +
    	       "WHERE dh.maDH > :afterId " +
    	       "ORDER BY dh.ngayDat DESC")
    	List<DonHang> findWithDetailsAfterId(@Param("afterId") Integer afterId);

}
