package com.poly.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.multipart.MultipartFile;

import com.poly.dto.DonHang1DTO;
import com.poly.dto.DonHangDTO;
import com.poly.dto.SanPhamDTO;
import com.poly.model.AnhSanPham;
import com.poly.model.BienTheSanPham;
import com.poly.model.ChiTietDonHang;
import com.poly.model.DanhGiaSP;
import com.poly.model.DonHang;
import com.poly.model.LoaiSanPham;
import com.poly.model.SanPham;
import com.poly.model.TaiKhoan;
import com.poly.model.ThanhToan;
import com.poly.model.TrangThaiDH;
import com.poly.model.TrangThaiKH;
import com.poly.repository.ChiTietDonHangRepository;
import com.poly.repository.DanhGiaSPRepository;
import com.poly.repository.DonHangRepository;
import com.poly.repository.SanPhamRepository;
import com.poly.repository.TaiKhoanRepository;
import com.poly.repository.ThanhToanRepository;
import com.poly.repository.TrangThaiDHRepository;
import com.poly.service.FileManagerService;
import com.poly.service.LoaiSanPhamService;
import com.poly.service.QuanLySanPhamService;
import com.poly.service.SanPhamService;
import com.poly.service.TaiKhoanService;
import com.poly.service.TrangThaiKHService;

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

	@GetMapping("/QuanLyBanner")
	public String sqlBanner(Model model) {

		model.addAttribute("content","Admin_Staff/QuanLyBanner.html");
		
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
	private QuanLySanPhamService service;

	@Autowired
	private LoaiSanPhamService loaiSanPhamService;
	@Autowired
	private TrangThaiKHService trangThaiKHService;

	

	@GetMapping("/QuanLySanPham")
	public String qlSanPham(Model model,
	                        @RequestParam(value = "keyword", required = false) String keyword,
	                        @RequestParam(value = "loaiKeyword", required = false) Integer loaiKeyword) {

	    List<SanPham> dsSanPham = service.searchSanPham(keyword, loaiKeyword);
	    List<LoaiSanPham> dsLoai = loaiSanPhamService.getAll();
	    List<TrangThaiKH> dsTrangThai = trangThaiKHService.getAll();

	    model.addAttribute("dsSanPham", dsSanPham);
	    model.addAttribute("dsLoai", dsLoai);
	    model.addAttribute("dsTrangThai", dsTrangThai);

	    // Gửi lại giá trị tìm kiếm để giữ trên form
	    model.addAttribute("keyword", keyword);
	    model.addAttribute("loaiKeyword", loaiKeyword);

	    return "Admin_Staff/QuanLySanPham";
	}
	



	@PostMapping("/QuanLySanPham/add")
	public String themSanPham(@ModelAttribute SanPham sanPham,
	                          @RequestParam("gia") BigDecimal gia,
	                          @RequestParam("tonKho") Integer tonKho,
	                          @RequestParam("TrangThaiKH") Integer maTrangThaiKH,
	                          @RequestParam(value = "anhSP", required = false) MultipartFile anhSP) throws IOException {

	    // tạo biến thể mặc định
	    BienTheSanPham bienThe = new BienTheSanPham();
	    bienThe.setGia(gia);
	    bienThe.setSoLuongTonKho(tonKho);

	    // gắn trạng thái
	    TrangThaiKH trangThai = trangThaiKHService.getById(maTrangThaiKH);
	    bienThe.setTrangThaiKH(trangThai);

	    bienThe.setSanPham(sanPham);
	    sanPham.setBienTheSanPham(List.of(bienThe));

	    // nếu có ảnh thì lưu file thực tế
	    if (anhSP != null && !anhSP.isEmpty()) {
	        // dùng FileManagerService để lưu
	        FileManagerService fm = new FileManagerService();
	        List<String> filenames = fm.save("sanpham", new MultipartFile[]{anhSP});

	        if (!filenames.isEmpty()) {
	            AnhSanPham anh = new AnhSanPham();
	            anh.setSanPham(sanPham);
	            anh.setUrlAnhSP("/uploads/sanpham/" + filenames.get(0)); // link để hiển thị lại
	            sanPham.setAnhSanPham(List.of(anh));
	        }
	    }

	    service.saveSanPham(sanPham);
	    return "redirect:/QuanLySanPham";
	}



	@PostMapping("/QuanLySanPham/edit")
	public String suaSanPham(@RequestParam("maSP") Integer maSP,
	                         @RequestParam("tenSP") String tenSP,
	                         @RequestParam("thuongHieu") String thuongHieu,
	                         @RequestParam("gia") BigDecimal gia,
	                         @RequestParam("tonKho") Integer tonKho,
	                         @RequestParam("TrangThaiKH") Integer maTrangThaiKH,   // ✅ thêm tham số
	                         @RequestParam(value = "anhSP", required = false) MultipartFile anhSP) throws IOException {

	    SanPham sp = service.getSanPhamById(maSP);
	    if (sp != null) {
	        sp.setTenSP(tenSP);
	        sp.setThuongHieu(thuongHieu);

	        if (sp.getBienTheSanPham() != null && !sp.getBienTheSanPham().isEmpty()) {
	            BienTheSanPham bienThe = sp.getBienTheSanPham().get(0);
	            bienThe.setGia(gia);
	            bienThe.setSoLuongTonKho(tonKho);

	            // ✅ cập nhật trạng thái
	            TrangThaiKH trangThai = trangThaiKHService.getById(maTrangThaiKH);
	            bienThe.setTrangThaiKH(trangThai);
	        }

	        if (anhSP != null && !anhSP.isEmpty()) {
	            // lưu ảnh mới
	            FileManagerService fm = new FileManagerService();
	            List<String> filenames = fm.save("sanpham", new MultipartFile[]{anhSP});

	            if (!filenames.isEmpty()) {
	                AnhSanPham anh;
	                if (sp.getAnhSanPham() != null && !sp.getAnhSanPham().isEmpty()) {
	                    anh = sp.getAnhSanPham().get(0);
	                } else {
	                    anh = new AnhSanPham();
	                    anh.setSanPham(sp);
	                    sp.setAnhSanPham(List.of(anh));
	                }
	                // set lại URL để hiển thị
	                anh.setUrlAnhSP("/uploads/sanpham/" + filenames.get(0));
	            }
	        }


	        service.updateSanPham(sp);
	    }
	    return "redirect:/QuanLySanPham";
	}

	    
	    
	    @GetMapping("/QuanLySanPham/delete/{id}")
	    public String xoaSanPham(@PathVariable("id") Integer id) {
	        service.deleteSanPham(id);
	        return "redirect:/QuanLySanPham";
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


}
