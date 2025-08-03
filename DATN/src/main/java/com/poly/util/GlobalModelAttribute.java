package com.poly.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.poly.entity.TaiKhoan;
import com.poly.entity.ThongBao;
import com.poly.service.ThongBaoService;

import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalModelAttribute {

    @Autowired
    private HttpSession session;

    @Autowired
    private ThongBaoService thongBaoService;

    @ModelAttribute
    public void addCommonAttributes(Model model) {
        TaiKhoan tk = (TaiKhoan) session.getAttribute("user");
        if (tk != null) {
            List<ThongBao> dsThongBaoChuaDoc = thongBaoService.layThongBaoChuaDocTheoMaTK(tk.getMaTK());
            model.addAttribute("dsThongBaoChuaDoc", dsThongBaoChuaDoc);
        }
    }
}