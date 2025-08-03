package com.poly.controller;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.poly.model.TaiKhoan;
import com.poly.security.CustomUserDetails;
import com.poly.service.TaiKhoanService;

import com.poly.service.VaiTroService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    @Autowired
    private TaiKhoanService taikhoanService;

    @Autowired
    private VaiTroService vaitroService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Pattern pattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
	
    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        TaiKhoan user = (TaiKhoan) session.getAttribute("user");
        
        if (user != null) {
        	
            String role = user.getVaiTro().getTenVaiTro();
            return switch (role) {
                case "Admin" -> "redirect:/QuanLySanPham";
                case "Nhân viên" -> "redirect:/QuanLySanPham";
                default -> "redirect:/home";
            };
        }
        return "login"; 
    }

//
//    @PostMapping("/login")
//    public String login(
//            @RequestParam("username") String input,
//            @RequestParam("password") String password,
//            HttpSession session) {
//
//        TaiKhoan taiKhoan = taikhoanService.findByEmailOrLoginName(input);
//
//        if (taiKhoan != null && passwordEncoder.matches(password, taiKhoan.getMatKhau())) {
//            String role = taiKhoan.getVaiTro().getTenVaiTro();
//
//            session.setAttribute("user", taiKhoan);
//            session.setAttribute("role", role);
//
//            System.out.println("==> Đăng nhập thành công với vai trò: " + role);
//
//            // Điều hướng theo vai trò
//            return switch (role) {
//                case "Admin" -> "redirect:/curd";
//                case "Nhân viên" -> "redirect:/staff/dashboard";
//                case "Khách hàng" -> "redirect:/home";
//                default -> {
//                    System.out.println("==> Vai trò không hợp lệ: " + role);
//                    yield "redirect:/home";
//                }
//            };
//        }
//
//        session.setAttribute("error", "Sai thông tin đăng nhập!");
//        System.out.println("==> Đăng nhập thất bại. input: " + input);
//        return "redirect:/login";
//    }
    @GetMapping("/redirect-by-role")
    public String redirectByRole(Authentication auth, HttpSession session) {
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        TaiKhoan tk = userDetails.getTaiKhoan();
        
//        System.out.println("==> Đăng nhập thành công với tài khoản: " + tk.getTenDangNhap());
//        System.out.println("==> Mật khẩu (đã mã hóa trong DB): " + tk.getMatKhau());
//        System.out.println("==> Vai trò: " + tk.getVaiTro().getTenVaiTro());
        session.setAttribute("user", tk); // gán vào session để dùng trong giao diện
        session.setAttribute("role", tk.getVaiTro().getTenVaiTro());
        

        
        return switch (tk.getVaiTro().getTenVaiTro()) {
            case "Admin" -> "redirect:/QuanLySanPham";
            case "Nhân viên" -> "redirect:/QuanLySanPham";
            default -> "redirect:/home";
        };
    }
    @GetMapping("/register")
    public String showRegisterForm() {
        return "register"; // Trả về view register.html
    }

    @PostMapping("/register")
    public String register(
            @RequestParam("tenDangNhap") String tenDangNhap,
            @RequestParam("email") String email,
            @RequestParam("matKhau") String matKhau,
            @RequestParam("xacNhanMatKhau") String xacNhanMatKhau,
            RedirectAttributes redirectAttributes,
            Model model) {

        boolean hasError = false;


        // Kiểm tra rỗng
        if (tenDangNhap == null || tenDangNhap.trim().isEmpty()) {
            model.addAttribute("usernameError", "Tên đăng nhập không được để trống");
            hasError = true;
        }

        if (email == null || email.trim().isEmpty()) {
            model.addAttribute("emailError", "Email không được để trống");
            hasError = true;
        }

        if (matKhau == null || matKhau.trim().isEmpty()) {
            model.addAttribute("passwordError", "Mật khẩu không được để trống");
            hasError = true;
        }

        if (taikhoanService.existsByTenDangNhap(tenDangNhap)) {
            model.addAttribute("usernameError", "Tên đăng nhập đã tồn tại.");
            hasError = true;
        }
        
        if (taikhoanService.existsByEmail(email)) {
            model.addAttribute("emailError", "Email đã được sử dụng.");
            hasError = true;
        }
        if (matKhau.length() < 8 || !pattern.matcher(matKhau).matches()) {
            model.addAttribute("passwordError", "Mật khẩu phải từ 8 ký tự, có chữ, số và ký hiệu.");
            hasError = true;
        }

        if (!matKhau.equals(xacNhanMatKhau)) {
            model.addAttribute("confirmError", "Mật khẩu xác nhận không khớp.");
            hasError = true;
        }

        if (hasError) {
            model.addAttribute("tenDangNhap", tenDangNhap);
            model.addAttribute("email", email);
            return "register"; // quay lại form
        }

        // Lưu tài khoản mới
        TaiKhoan newUser = new TaiKhoan();
        newUser.setTenDangNhap(tenDangNhap);
        newUser.setEmail(email);
        newUser.setMatKhau(passwordEncoder.encode(matKhau));
        newUser.setVaiTro(vaitroService.findByTenVaiTro("Khách hàng"));

        taikhoanService.saveTaiKhoan(newUser);
        redirectAttributes.addFlashAttribute("successMessage", "Đăng ký thành công! Vui lòng đăng nhập.");
        return "redirect:/login?registerSuccess";
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        SecurityContextHolder.clearContext();
        return "redirect:/login?logoutSuccess";
    }
}
