package com.poly.repository;



import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.model.DonHang;
@Repository

public interface DonHangRepository extends JpaRepository<DonHang, Integer>{
	List<DonHang> findByTaiKhoan_MaTKOrderByNgayDatDesc(int maTK);
    List<DonHang> findByTrangThaiDH_MaTTDH(int maTTDH);
    List<DonHang> findByTaiKhoan_MaTKAndTrangThaiDH_MaTTDHOrderByNgayDatDesc(int maTK, int maTTDH);

}
