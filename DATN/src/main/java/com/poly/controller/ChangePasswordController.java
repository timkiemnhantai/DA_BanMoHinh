package com.poly.controller;

import com.poly.dto.ChangePasswordRequest;
import com.poly.service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ChangePasswordController {

    @Autowired
    private TaiKhoanService taiKhoanService;

    @GetMapping("/change-password")
    public String showChangePasswordForm(Model model) {
        model.addAttribute("changePasswordRequest", new ChangePasswordRequest());
        return "change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@ModelAttribute("changePasswordRequest") ChangePasswordRequest request, Model model) {
        String result = taiKhoanService.changePassword(request);

        if ("success".equals(result)) {
            model.addAttribute("success", "Đổi mật khẩu thành công!");
        } else {
            model.addAttribute("error", result);
        }

        return "change-password";
    }
}
