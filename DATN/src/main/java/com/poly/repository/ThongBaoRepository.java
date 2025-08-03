package com.poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.entity.ThongBao;

@Repository
public interface ThongBaoRepository extends JpaRepository<ThongBao, Integer> {
	List<ThongBao> findByTaiKhoan_MaTKAndDaDocFalseOrderByNgayTaoDesc(Integer maTK);

}
