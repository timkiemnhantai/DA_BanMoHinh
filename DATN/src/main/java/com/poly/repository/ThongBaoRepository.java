package com.poly.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.poly.model.ThongBao;

import jakarta.transaction.Transactional;

@Repository
public interface ThongBaoRepository extends JpaRepository<ThongBao, Integer> {
	List<ThongBao> findByTaiKhoan_MaTKAndDaDocFalseOrderByNgayTaoDesc(Integer maTK);
    // lấy tất cả cho user, có thể truyền Pageable để sort: unread (daDoc ASC -> false trước), ngayTao DESC
    Page<ThongBao> findByTaiKhoan_MaTK(Integer maTK, Pageable pageable);

    long countByTaiKhoan_MaTKAndDaDocFalse(Integer maTK);
    @Modifying
    @Transactional
    @Query("update ThongBao t set t.daDoc = true where t.taiKhoan.maTK = :maTK and t.daDoc = false")
    int markAllAsReadByMaTK(Integer maTK);
    void deleteByTaiKhoan_MaTK(Integer maTK);
}
