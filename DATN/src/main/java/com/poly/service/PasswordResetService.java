package com.poly.service;



import jakarta.mail.MessagingException;

import java.util.List;

import com.poly.model.PasswordReset;
import com.poly.model.TaiKhoan;

public interface PasswordResetService {
    List<PasswordReset> findAll();
    PasswordReset findById(TaiKhoan taiKhoan);
    PasswordReset create(PasswordReset passwordReset);
    PasswordReset update(PasswordReset passwordReset);
    void delete(TaiKhoan taiKhoan);

    String generateOtp(String email) throws MessagingException;
    String verifyOtpAndResetPassword(String email, String otp, String newPassword);
    void deleteByUser(TaiKhoan taiKhoan);
	PasswordReset findById(TaiKhoan taiKhoan, String token);
	void delete(TaiKhoan taiKhoan, String token);
}

