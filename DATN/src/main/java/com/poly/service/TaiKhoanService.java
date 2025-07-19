package com.poly.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.poly.dto.ChangePasswordRequest;
import com.poly.model.TaiKhoan;
import com.poly.repository.TaiKhoanRepository;

@Service
public class TaiKhoanService {
	@Autowired
	private TaiKhoanRepository taikhoanRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	

    public List<TaiKhoan> layTaiKhoanTheoMaVaiTro(List<Integer> maVaiTro) {
        return taikhoanRepository.findByVaiTro_MaVaiTroIn(maVaiTro);
    }
//	public List<TaiKhoan> getALL(){
//		return taikhoanRepository.findAll();
//	}
		
	public TaiKhoan saveTaiKhoan(TaiKhoan taiKhoan) {
		return taikhoanRepository.save(taiKhoan);
	}
	
    public TaiKhoan findByEmailOrTenDangNhap(String input) {
        return taikhoanRepository.findByEmailOrTenDangNhap(input, input);
    }
    public boolean existsByEmailOrTenDangNhap(String email,String tenDangNhap) {
        return taikhoanRepository.existsByEmailOrTenDangNhap(email, tenDangNhap);

    }
    public String changePassword(ChangePasswordRequest request) {
        // Bước 1: Tìm tài khoản theo email
        TaiKhoan taiKhoan = taikhoanRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (taiKhoan == null) {
            return "Tài khoản không tồn tại.";
        }

        // Bước 2: Kiểm tra mật khẩu cũ
        if (!passwordEncoder.matches(request.getOldPassword(), taiKhoan.getMatKhau())) {
            return "Mật khẩu cũ không đúng.";
        }

        // Bước 3: Kiểm tra xác nhận mật khẩu mới
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return "Xác nhận mật khẩu không khớp.";
        }

        // Bước 4: Cập nhật mật khẩu mới
        taiKhoan.setMatKhau(passwordEncoder.encode(request.getNewPassword()));
        taikhoanRepository.save(taiKhoan);

        return "success";
    }
    
}
