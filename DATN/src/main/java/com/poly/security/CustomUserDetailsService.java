package com.poly.security;

import com.poly.model.TaiKhoan;
import com.poly.repository.TaiKhoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TaiKhoan taiKhoan = taiKhoanRepository.findByEmailOrTenDangNhap(username, username);
        if (taiKhoan == null) {
            throw new UsernameNotFoundException("Tài khoản không tồn tại");
        }
        return new CustomUserDetails(taiKhoan);
    }
}
