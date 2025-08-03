package com.poly.service;



import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import com.poly.entity.PasswordReset;
import com.poly.entity.TaiKhoan;
import com.poly.repository.PasswordResetRepository;
import com.poly.repository.TaiKhoanRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {
    @Autowired
    private PasswordResetRepository passwordResetRepository;

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public String generateOtp(String email) throws MessagingException {
        // Kiểm tra email tồn tại
        TaiKhoan taiKhoan  = taiKhoanRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email không tồn tại"));

        // Tạo mã OTP 6 số
        String otp = String.format("%06d", new Random().nextInt(999999));

        // Tính thời gian hết hạn (5 phút)
        Date expiryDate = new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5));

        // Xóa OTP cũ nếu tồn tại
        passwordResetRepository.deleteByTaiKhoan(taiKhoan);

        // Lưu OTP mới
        PasswordReset passwordReset = PasswordReset.builder()
                .taiKhoan(taiKhoan)
                .token(otp)
                .expiresAt(expiryDate)
                .build();
        passwordResetRepository.save(passwordReset);

        // Gửi email
        sendOtpEmail(email, otp);

        return "OTP đã được gửi đến email của bạn";
    }

    private void sendOtpEmail(String to, String otp) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject("Mã OTP để đặt lại mật khẩu");
        helper.setText(
                "<h3>Mã OTP của bạn</h3>" +
                        "<p>Mã OTP để đặt lại mật khẩu là: <b>" + otp + "</b></p>" +
                        "<p>Mã này có hiệu lực trong 5 phút.</p>",
                true
        );

        mailSender.send(message);
    }

    @Override
    @Transactional
    public String verifyOtpAndResetPassword(String email, String otp, String newPassword) {
        TaiKhoan taiKhoan = taiKhoanRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email không tồn tại"));

        PasswordReset passwordReset = passwordResetRepository.findByTaiKhoanAndToken(taiKhoan, otp)
                .orElseThrow(() -> new RuntimeException("OTP không tồn tại hoặc không hợp lệ"));

        // Kiểm tra thời gian hết hạn
        if (new Date().after(passwordReset.getExpiresAt())) {
            passwordResetRepository.deleteByTaiKhoan(taiKhoan);
            return "OTP đã hết hạn";
        }

        // Cập nhật mật khẩu mới
        taiKhoan.setMatKhau(passwordEncoder.encode(newPassword));
        taiKhoanRepository.save(taiKhoan);

        // Xóa OTP sau khi sử dụng
        passwordResetRepository.deleteByTaiKhoan(taiKhoan);

        return "Mật khẩu đã được đặt lại thành công";
    }

    @Override
    @Transactional
    public void deleteByUser(TaiKhoan taiKhoan) {
        passwordResetRepository.deleteByTaiKhoan(taiKhoan);
    }

    @Override
    public List<PasswordReset> findAll() {
        return passwordResetRepository.findAll();
    }

//    @Override
//    public PasswordReset findById(TaiKhoan taiKhoan) {
//        return passwordResetRepository.findById(taiKhoan).get();
//    }

    @Override
    public PasswordReset create(PasswordReset passwordReset) {
        return passwordResetRepository.save(passwordReset);
    }

    @Override
    public PasswordReset update(PasswordReset passwordReset) {
        return passwordResetRepository.save(passwordReset);
    }

	@Override
	public PasswordReset findById(TaiKhoan taiKhoan) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(TaiKhoan taiKhoan) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PasswordReset findById(TaiKhoan taiKhoan, String token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(TaiKhoan taiKhoan, String token) {
		// TODO Auto-generated method stub
		
	}

//    @Override
//    public void delete(TaiKhoan taiKhoan) {
//        passwordResetRepository.deleteById(taiKhoan);
//    }
}

