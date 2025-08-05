package com.poly.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.poly.model.DanhGiaSP;
import com.poly.repository.DanhGiaSPRepository;
import com.poly.repository.SanPhamRepository;
import com.poly.repository.TaiKhoanRepository;

@Controller
@RequestMapping("/quan-ly-danh-gia") 
public class QuanLyDanhGiaController {

    @Autowired
    private DanhGiaSPRepository danhGiaSPRepository;

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;


    @GetMapping
    public String getDanhGiaPage(Model model) {
        List<DanhGiaSP> danhGiaList = danhGiaSPRepository.findAll();
        model.addAttribute("danhGiaList", danhGiaList);
        return "QuanLyDanhGia";
    }
    
 // Xử lý update
    @PostMapping("/update")
    public String updateDanhGia(@RequestParam("maDG") Integer maDG,
                                @RequestParam("maSP") Integer maSP,
                                @RequestParam("maTK") Integer maTK,
                                @RequestParam("soSao") Integer soSao,
                                @RequestParam("binhLuan") String binhLuan,
                                @RequestParam("ngayDang") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate ngayDang) {

        DanhGiaSP dg = danhGiaSPRepository.findById(maDG).orElse(null);
        if (dg != null) {
            dg.setSanPham(sanPhamRepository.findById(maSP).orElse(null));
            dg.setTaiKhoan(taiKhoanRepository.findById(maTK).orElse(null));
            dg.setSoSao(soSao);
            dg.setBinhLuan(binhLuan);
            dg.setNgayDang(ngayDang.atStartOfDay());
            danhGiaSPRepository.save(dg);
        }
        return "redirect:/quan-ly-danh-gia";
    }

    // Xử lý xóa
    @GetMapping("/delete/{id}")
    public String deleteDanhGia(@PathVariable("id") Integer id) {
        danhGiaSPRepository.deleteById(id);
        return "redirect:/quan-ly-danh-gia";
    }

    
    
    
    
}

