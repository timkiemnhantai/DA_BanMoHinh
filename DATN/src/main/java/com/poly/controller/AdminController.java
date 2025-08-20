package com.poly.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.poly.model.*;
import com.poly.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poly.dto.DonHangDTO;
import com.poly.dto.SanPhamDTO;
import com.poly.model.DanhGiaSP;
import com.poly.model.DonHang;
import com.poly.model.SanPham;
import com.poly.model.TaiKhoan;
import com.poly.model.ThanhToan;
import com.poly.repository.DanhGiaSPRepository;
import com.poly.repository.DonHangRepository;
import com.poly.repository.SanPhamRepository;
import com.poly.repository.TaiKhoanRepository;
import com.poly.repository.ThanhToanRepository;
import com.poly.repository.TrangThaiDHRepository;
import com.poly.service.SanPhamService;
import com.poly.service.TaiKhoanService;

import jakarta.transaction.Transactional;

@Controller
public class AdminController {
	@Autowired
	private TaiKhoanService taikhoanService;
	@Autowired
	private ThanhToanRepository thanhToanRepository;

	@Autowired
	private DonHangRepository donHangRepository;
	@Autowired
	private ChiTietDonHangRepository chiTietDonHangRepository;
	@Autowired
	private TrangThaiDHRepository trangThaiDHRepository;

	@GetMapping("/QuanLyTaiKhoan")
	public String qlTaiKhoan(Model model) {
		List<TaiKhoan> nhanVien = taikhoanService.layTaiKhoanTheoMaVaiTro(Arrays.asList(2));
		List<TaiKhoan> khachHang = taikhoanService.layTaiKhoanTheoMaVaiTro(Arrays.asList(3));
		model.addAttribute("nhanvien", nhanVien);
		model.addAttribute("khachhang", khachHang);
		model.addAttribute("content","Admin_Staff/QuanLyTaiKhoan.html");
		
		return "Admin_Staff/curd";
	}



	@GetMapping("/QuanLyThanhToan")
	public String hienThiThanhToan(Model model) {
		List<ThanhToan> listThanhToan = thanhToanRepository.findAll();
		model.addAttribute("listThanhToan", listThanhToan);
		model.addAttribute("content", "Admin_Staff/QuanLyThanhToan.html");
		return "Admin_Staff/curd";
	}

	@PostMapping("/ThanhToan/update")
	@ResponseBody
	public String capNhatThanhToan(@RequestParam Integer maThanhToan, @RequestParam Integer maDH,
			@RequestParam BigDecimal soTien, @RequestParam String ngayThanhToan, @RequestParam String phuongThuc,
			@RequestParam String trangThai, @RequestParam String ghiChu) {
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
	

    @Autowired
    private SanPhamService sanPhamService;

    // Hiển thị danh sách sản phẩm
    @GetMapping("/QuanLySanPham")
    public String hienThiQuanLySanPham(Model model) {
        List<SanPhamDTO> danhSachSanPham = sanPhamService.locSanPham(new SanPhamDTO());
        model.addAttribute("danhSachSanPham", danhSachSanPham);
        model.addAttribute("content", "Admin_Staff/QuanLySanPham.html");
        return "Admin_Staff/curd";
    }
    
    @GetMapping("/QuanLySanPham/create")
    public String showCreateForm(Model model) {
        model.addAttribute("sanPham", new SanPham());
        return "Admin_Staff/formSanPham"; // Tạo file Thymeleaf riêng
    }

    @PostMapping("/QuanLySanPham/create")
    public String createSanPham(@ModelAttribute("sanPham") SanPham sanPham) {
        sanPhamService.saveAnhSanPham(sanPham);
        return "redirect:/QuanLySanPham";
    }

    @GetMapping("/QuanLySanPham/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        SanPham sp = sanPhamService.getAnhSanPhamById(id).orElseThrow(() -> new IllegalArgumentException("Id không tồn tại: " + id));
        model.addAttribute("sanPham", sp);
        return "Admin_Staff/formSanPham";
    }

    @PostMapping("/QuanLySanPham/edit/{id}")
    public String updateSanPham(@PathVariable("id") Integer id, @ModelAttribute("sanPham") SanPham sanPham) {
        sanPhamService.update(id, sanPham);
        return "redirect:/QuanLySanPham";
    }
    @GetMapping("/QuanLySanPham/delete/{id}")
    public String deleteSanPham(@PathVariable("id") Integer id) {
        sanPhamService.deleteAnhSanPham(id);
        return "redirect:/QuanLySanPham";
    }
    
    @GetMapping("/QuanLySanPham/search")
    public String timKiemSanPham(@RequestParam(value = "keyword", required = false) String keyword,
                                 @RequestParam(value = "loaiKeyword", required = false) String loaiKeyword,
                                 @RequestParam(value = "sapXep", required = false) String sapXep,
                                 @RequestParam(value = "giamGia", required = false) Boolean giamGia,
                                 Model model) {

        SanPhamDTO filter = new SanPhamDTO();
        filter.setKeyword(keyword);
        filter.setLoaiKeyword(loaiKeyword);
        filter.setSapXep(sapXep);
        filter.setGiamGia(giamGia);

        List<SanPhamDTO> danhSachSanPham = sanPhamService.locSanPham(filter);

        model.addAttribute("danhSachSanPham", danhSachSanPham);
        model.addAttribute("keyword", keyword);
        model.addAttribute("loaiKeyword", loaiKeyword);
        model.addAttribute("sapXep", sapXep);
        model.addAttribute("giamGia", giamGia);

        return "Admin_Staff/QuanLySanPham";
    }

    @Autowired
    private DanhGiaSPRepository danhGiaSPRepository;

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;


    @GetMapping("/QuanLyDanhGia")
    public String getDanhGiaPage(Model model) {
        List<DanhGiaSP> danhGiaList = danhGiaSPRepository.findAll();
        model.addAttribute("danhGiaList", danhGiaList);
        model.addAttribute("content", "Admin_Staff/QuanLyDanhGia.html");
        return "Admin_Staff/curd";
    }
    
 // Xử lý update
    @PostMapping("/QuanLyDanhGia/update")
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
    @GetMapping("/QuanLyDanhGia/delete/{id}")
    public String deleteDanhGia(@PathVariable("id") Integer id) {
        danhGiaSPRepository.deleteById(id);
        return "Admin_Staff/curd";
    }

    
    @GetMapping("/QuanLyDonHang")
    public String qlDonHang(Model model) {
        List<DonHang> dsDonHang = donHangRepository.findAll(Sort.by(Sort.Direction.DESC, "ngayDat"));
        List<DonHangDTO> dsDonHangDTO = dsDonHang.stream().map(dh -> {
            List<ChiTietDonHang> chiTietDonHangs = chiTietDonHangRepository.findByDonHang(dh);
            return new DonHangDTO(dh, chiTietDonHangs);
        }).toList();
        if (!dsDonHangDTO.isEmpty()) {
            System.out.println(">>> productsDataJson sample: " + dsDonHangDTO.get(0).getProductsDataJson());
        }
        // Đếm số lượng theo trạng thái
        long tatCa = dsDonHang.size();
        long choXN = dsDonHang.stream().filter(dh -> "Chờ xác nhận".equals(dh.getTrangThaiDH().getTenTTDH())).count();
        long daXN = dsDonHang.stream().filter(dh -> "Đã xác nhận".equals(dh.getTrangThaiDH().getTenTTDH())).count();
        long dangGiao = dsDonHang.stream().filter(dh -> "Đang giao hàng".equals(dh.getTrangThaiDH().getTenTTDH())).count();
        long daGiao = dsDonHang.stream().filter(dh -> "Đã giao hàng".equals(dh.getTrangThaiDH().getTenTTDH())).count();
        long hoanThanh = dsDonHang.stream().filter(dh -> "Giao hàng thành công".equals(dh.getTrangThaiDH().getTenTTDH())).count();
        long daHuy = dsDonHang.stream().filter(dh -> "Đã hủy".equals(dh.getTrangThaiDH().getTenTTDH())).count();
        long hoanTra = dsDonHang.stream().filter(dh -> "Hoàn trả".equals(dh.getTrangThaiDH().getTenTTDH())).count();
        long yeuCauHuy = dsDonHang.stream().filter(dh -> "Yêu cầu hủy".equals(dh.getTrangThaiDH().getTenTTDH())).count();

        model.addAttribute("dsDonHang", dsDonHangDTO);
        model.addAttribute("countTatCa", tatCa);
        model.addAttribute("countChoXN", choXN);
        model.addAttribute("countDaXN", daXN);
        model.addAttribute("countDangGiao", dangGiao);
        model.addAttribute("countDaGiao", daGiao);
        model.addAttribute("countHoanThanh", hoanThanh);
        model.addAttribute("countDaHuy", daHuy);
        model.addAttribute("countHoanTra", hoanTra);
        model.addAttribute("countYeuCauHuy", yeuCauHuy);

        model.addAttribute("content", "Admin_Staff/QuanLyDonHang.html");
        return "Admin_Staff/curd";
    }

    @PostMapping("/don-hang/{id}/update-status")
    @ResponseBody
    public Map<String, String> updateTrangThai(@PathVariable("id") Integer id,
                                               @RequestParam(value = "yeuCauHuy", required = false) Boolean yeuCauHuy) {

        DonHang dh = donHangRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        int currentStatusId = dh.getTrangThaiDH().getMaTTDH();
        int newStatusId = currentStatusId;
        String newStatus = dh.getTrangThaiDH().getTenTTDH();

        if (Boolean.TRUE.equals(yeuCauHuy)) {
            newStatusId = 8; // Yêu cầu hủy
        } else {
            switch (currentStatusId) {
                case 1: newStatusId = 2; break;
                case 2: newStatusId = 3; break;
                case 3: newStatusId = 4; break;
                case 8: newStatusId = 5; break;
            }
        }

        if (newStatusId != currentStatusId) {
            TrangThaiDH newTrangThai = trangThaiDHRepository.findById(newStatusId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy trạng thái"));
            dh.setTrangThaiDH(newTrangThai);
            donHangRepository.save(dh);
            newStatus = newTrangThai.getTenTTDH();
        }

        Map<String, String> response = new HashMap<>();
        response.put("newStatus", newStatus);
        response.put("newStatusId", String.valueOf(newStatusId));
        return response;
    }
    @GetMapping("/don-hang/latest")
    @ResponseBody
    @Transactional
    public List<DonHang1DTO> getLatestOrders(@RequestParam("afterId") Integer afterId) {
        List<DonHang> newOrders = donHangRepository.findByMaDHGreaterThanOrderByNgayDatDesc(afterId);

        return newOrders.stream()
            .map(dh -> new DonHang1DTO(dh, dh.getChiTietDonHangs()))
            .toList();
    }
    @GetMapping("/don-hang/{maDH}/chi-tiet")
    public ResponseEntity<?> getChiTietDonHang(@PathVariable int maDH) {
        DonHang dh = donHangRepository.findById(maDH).orElseThrow();
        List<ChiTietDonHang> chiTiet = chiTietDonHangRepository.findByDonHang(dh);

        return ResponseEntity.ok(chiTiet.stream().map(ct -> Map.of(
            "tenSP", ct.getBienTheSanPham().getSanPham().getTenSP(),
            "hinhAnh", ct.getBienTheSanPham().getAnhChiTietList().get(0).getUrlAnhCT(),
            "soLuong", ct.getSoLuongSP(),
            "donGia", ct.getDonGia(),
            "thanhTien", ct.getThanhTien()
        )));
    }












    @Autowired
    private BannerService bannerService;

    // Hiển thị danh sách Banner
    @GetMapping("/QuanLyBanner")
    public String hienThiQuanLyBanner(Model model) {
        List<Banner> danhSachBanner = bannerService.getAllBanners();
        model.addAttribute("danhSachBanner", danhSachBanner);

        // 👇 thêm dòng này để fix lỗi
        model.addAttribute("banner", new Banner());

        model.addAttribute("content", "Admin_Staff/QuanLyBanner.html");
        return "Admin_Staff/curd";
    }


    // Hiển thị form tạo Banner
    @GetMapping("/QuanLyBanner/create")
    public String showCreateBannerForm(Model model) {
        model.addAttribute("banner", new Banner());
        return "Admin_Staff/formBanner";
    }

    // Lưu banner mới
    @PostMapping("/QuanLyBanner/create")
    public String createBanner(@ModelAttribute("banner") Banner banner) {
        bannerService.createBanner(banner);
        return "redirect:/QuanLyBanner";
    }

    // Hiển thị form sửa Banner
    @GetMapping("/QuanLyBanner/edit/{id}")
    public String showEditBannerForm(@PathVariable("id") Integer id, Model model) {
        Banner banner = bannerService.getBannerById(id);
        model.addAttribute("banner", banner);
        return "Admin_Staff/formBanner";
    }

    // Cập nhật Banner
    @PostMapping("/QuanLyBanner/edit/{id}")
    public String updateBanner(@PathVariable("id") Integer id, @ModelAttribute("banner") Banner banner) {
        bannerService.updateBanner(id, banner);
        return "redirect:/QuanLyBanner";
    }

    // Xóa Banner
    @GetMapping("/QuanLyBanner/delete/{id}")
    public String deleteBanner(@PathVariable("id") Integer id) {
        bannerService.deleteBanner(id);
        return "redirect:/QuanLyBanner";
    }

}
