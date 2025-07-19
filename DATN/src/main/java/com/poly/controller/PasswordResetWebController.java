package com.poly.controller;

import jakarta.mail.MessagingException;
import com.poly.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PasswordResetWebController {

    @Autowired
    private PasswordResetService passwordResetService;

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "/security/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String requestOtp(@RequestParam String email, Model model) {
        try {
            String result = passwordResetService.generateOtp(email);
            model.addAttribute("message", result);
            model.addAttribute("email", email);
            return "/security/reset-password";
        } catch (MessagingException e) {
            model.addAttribute("error", "Lỗi khi gửi email: " + e.getMessage());
            return "/security/forgot-password";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "/security/forgot-password";
        }
    }
    

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String email,
                                @RequestParam String otp,
                                @RequestParam String newPassword,
                                Model model) {
        try {
            String result = passwordResetService.verifyOtpAndResetPassword(email, otp, newPassword);
            model.addAttribute("message", result);
            return "login";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("email", email);
            return "/security/reset-password";
        }
    }
}
