package com.poly.security;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
public class PasswordGenerator {
    public static void main(String[] args) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "99999999";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("Mật khẩu đã mã hóa: " + encodedPassword);
    }
}
