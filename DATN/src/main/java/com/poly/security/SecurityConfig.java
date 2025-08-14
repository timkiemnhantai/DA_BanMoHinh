package com.poly.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
//            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/register", "/css/**", "/js/**", "/img/**", "/home","/", "/AnhSanPham/**","/product","/product/search","/chi-tiet-san-pham/**","/forgot-password","/reset-password","/change-password","/doimatkhau","/ChinhSach","/websocket/**", "/ws/**","/xac-nhan-thanh-toan/**","/thong-bao/**","/uploads/**").permitAll()
                .requestMatchers("/QuanLySanPham/**", "/QuanLyDanhGia/**").hasAnyAuthority("Admin", "Nhân viên")
                .requestMatchers("/curd", "/QuanLyThanhToan","/QuanLyTaiKhoan").hasAuthority("Admin")

                .requestMatchers("/gio-hang/**","/thanh-toan/**","/Doimatkhau/**","/DiaChi/**","/TrangCaNhan/**","/DonHang/**","/Xac-nhan-nhan-hang/**","/danh-gia/**").hasAuthority("Khách hàng")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login") // dùng POST /login
                .defaultSuccessUrl("/redirect-by-role", true)
                .failureUrl("/login?error")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .exceptionHandling(ex -> ex.accessDeniedPage("/403"));
        http
        .exceptionHandling(ex -> ex
          .accessDeniedPage("/403")
        );
        return http.build();
    }
}
