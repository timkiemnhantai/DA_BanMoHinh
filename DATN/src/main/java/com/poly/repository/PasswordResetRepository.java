package com.poly.repository;

import com.poly.model.PasswordReset;
import com.poly.model.PasswordResetId;
import com.poly.model.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordReset, PasswordResetId> {
    Optional<PasswordReset> findByTaiKhoanAndToken(TaiKhoan taiKhoan, String token);
    Optional<PasswordReset> findByTaiKhoan(TaiKhoan taiKhoan);
    void deleteByTaiKhoan(TaiKhoan taiKhoan);
}

