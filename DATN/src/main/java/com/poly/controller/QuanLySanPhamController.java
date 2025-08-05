package com.poly.controller;

import com.poly.model.BienTheSanPham;
import com.poly.model.SanPham;
import com.poly.model.TrangThaiKH;
import com.poly.service.FileManagerService;
import com.poly.service.LoaiSanPhamService;
import com.poly.service.SanPhamService;
import com.poly.service.TrangThaiKHService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping("/QuanLySanPham")
public class QuanLySanPhamController {

    @Autowired
    private SanPhamService sanPhamService;

    @Autowired
    private LoaiSanPhamService loaiSanPhamService;

    @Autowired
    private TrangThaiKHService trangThaiKHService;

    @Autowired
    private FileManagerService fileManagerService;

    @GetMapping
    public String hienThiDanhSach(Model model) {
        List<SanPham> dsSP = sanPhamService.getAll();
        model.addAttribute("danhSachSanPham", dsSP);
        return "QuanLySanPham";
    }

    @GetMapping("/create")
    public String hienThiFormThem(Model model) {
        model.addAttribute("sanPham", new SanPham());
        model.addAttribute("dsLoaiSanPham", loaiSanPhamService.getAllLoaiSanPham());
        model.addAttribute("dsTrangThaiKH", trangThaiKHService.getAll());
        return "formSanPham";
    }

    @PostMapping("/create")
    public String themSanPham(@ModelAttribute("sanPham") SanPham sanPham,
                              @RequestParam("giaMacDinh") BigDecimal gia,
                              @RequestParam("soLuongTonKhoMacDinh") Integer soLuong,
                              @RequestParam("maTrangThaiKHMacDinh") Integer maTrangThai,
                              @RequestParam("fileAnh") MultipartFile fileAnh) throws IOException {

        sanPham.setTenKhongDau(com.poly.util.BoDau.removeVietnameseTone(sanPham.getTenSP()).toLowerCase());

        BienTheSanPham bienThe = new BienTheSanPham();
        bienThe.setGia(gia);
        bienThe.setSoLuongTonKho(soLuong);
        bienThe.setTrangThaiKH(new TrangThaiKH(maTrangThai, null));
        bienThe.setSanPham(sanPham);

        sanPham.setBienTheSanPham(List.of(bienThe));

        String urlAnh = null;
        if (!fileAnh.isEmpty()) {
            List<String> files = fileManagerService.save("AnhSanPham", new MultipartFile[]{fileAnh});
            urlAnh = "/AnhSanPham/" + files.get(0); // lấy tên file đầu tiên
        }

        sanPhamService.saveSanPhamFull(sanPham, fileAnh, urlAnh, null);
        return "redirect:/QuanLySanPham";
    }

    @GetMapping("/edit/{id}")
    public String hienThiFormSua(@PathVariable("id") Integer id, Model model) {
        Optional<SanPham> sanPhamOptional = sanPhamService.getAnhSanPhamById(id);

        if (sanPhamOptional.isPresent()) {
            SanPham sanPham = sanPhamOptional.get();

            // Lấy biến thể mặc định (nếu có)
            BienTheSanPham bienTheMacDinh = null;
            if (sanPham.getBienTheSanPham() != null && !sanPham.getBienTheSanPham().isEmpty()) {
                bienTheMacDinh = sanPham.getBienTheSanPham().get(0); // giả sử chỉ có 1 biến thể mặc định
            }

            // Truyền sản phẩm và danh sách các thuộc tính cần thiết xuống view
            model.addAttribute("sanPham", sanPham);
            model.addAttribute("dsLoaiSanPham", loaiSanPhamService.getAllLoaiSanPham());
            model.addAttribute("dsTrangThaiKH", trangThaiKHService.getAll());

            // Gửi thêm các thuộc tính riêng lẻ cho form
            if (bienTheMacDinh != null) {
                model.addAttribute("giaMacDinh", bienTheMacDinh.getGia());
                model.addAttribute("soLuongTonKhoMacDinh", bienTheMacDinh.getSoLuongTonKho());
                model.addAttribute("maTrangThaiKHMacDinh", bienTheMacDinh.getTrangThaiKH().getMaTrangThaiKH());
            } else {
                model.addAttribute("giaMacDinh", null);
                model.addAttribute("soLuongTonKhoMacDinh", null);
                model.addAttribute("maTrangThaiKHMacDinh", null);
            }

            return "formSanPham";
        } else {
            return "redirect:/QuanLySanPham";
        }
    }


    
    
    @PostMapping("/update")
    public String capNhatSanPham(@ModelAttribute("sanPham") SanPham sanPham,
                                 @RequestParam("giaMacDinh") BigDecimal gia,
                                 @RequestParam("soLuongTonKhoMacDinh") Integer soLuong,
                                 @RequestParam("maTrangThaiKHMacDinh") Integer maTrangThai,
                                 @RequestParam("fileAnh") MultipartFile fileAnh) throws IOException {

        sanPham.setTenKhongDau(com.poly.util.BoDau.removeVietnameseTone(sanPham.getTenSP()).toLowerCase());

        BienTheSanPham bienThe = new BienTheSanPham();
        bienThe.setGia(gia);
        bienThe.setSoLuongTonKho(soLuong);
        bienThe.setTrangThaiKH(new TrangThaiKH(maTrangThai, null));
        bienThe.setSanPham(sanPham);

        sanPham.setBienTheSanPham(List.of(bienThe));

        String urlAnh = null;
        if (!fileAnh.isEmpty()) {
            List<String> files = fileManagerService.save("AnhSanPham", new MultipartFile[]{fileAnh});
            urlAnh = "/AnhSanPham/" + files.get(0);
        }

        sanPhamService.updateSanPhamFull(sanPham.getMaSP(), sanPham, fileAnh, urlAnh, null);
        return "redirect:/QuanLySanPham";
    }

    @GetMapping("/delete/{id}")
    public String xoaSanPham(@PathVariable("id") Integer id) {
        sanPhamService.deleteAnhSanPham(id);
        return "redirect:/QuanLySanPham";
    }
}
