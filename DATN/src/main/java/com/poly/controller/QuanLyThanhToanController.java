package com.poly.controller;

import com.poly.model.DonHang;
import com.poly.model.ThanhToan;
import com.poly.repository.DonHangRepository;
import com.poly.repository.ThanhToanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class QuanLyThanhToanController {

    @Autowired
    private ThanhToanRepository thanhToanRepository;

    @Autowired
    private DonHangRepository donHangRepository;

    @GetMapping("/QuanLyThanhToan")
    public String hienThiThanhToan(Model model) {
        List<ThanhToan> listThanhToan = thanhToanRepository.findAll();
        model.addAttribute("listThanhToan", listThanhToan);
        return "QuanLyThanhToan";
    }

    @PostMapping("/ThanhToan/update")
    @ResponseBody
    public String capNhatThanhToan(
            @RequestParam Integer maThanhToan,
            @RequestParam Integer maDH,
            @RequestParam BigDecimal soTien,
            @RequestParam String ngayThanhToan,
            @RequestParam String phuongThuc,
            @RequestParam String trangThai,
            @RequestParam String ghiChu
    ) {
        ThanhToan tt = thanhToanRepository.findById(maThanhToan).orElse(null);
        if (tt != null) {
            DonHang dh = donHangRepository.findById(maDH).orElse(null);
            if (dh != null) {
                tt.setDonHang(dh);
                tt.setSoTien(soTien);
                tt.setNgayThanhToan(LocalDateTime.parse(ngayThanhToan + "T00:00:00"));
                tt.setPhuongThucTT(phuongThuc);
                tt.setTrangThai(trangThai);
                tt.setGhiChuTT(ghiChu);
                thanhToanRepository.save(tt);
                return "success";
            }
        }
        return "fail";
    }

    @PostMapping("/ThanhToan/delete")
    @ResponseBody
    public String xoaThanhToan(@RequestParam Integer maThanhToan) {
        try {
            thanhToanRepository.deleteById(maThanhToan);
            return "success";
        } catch (Exception e) {
            return "fail";
        }
    }
}
