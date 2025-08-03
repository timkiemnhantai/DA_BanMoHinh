package com.poly.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.poly.dto.ChiTietSanPhamDTO;
import com.poly.dto.DonHangDTO;
import com.poly.dto.GioHangDTO;
import com.poly.dto.SanPhamDTO;
import com.poly.entity.BienTheSanPham;
import com.poly.entity.ChiTietDonHang;
import com.poly.entity.DiaChi;
import com.poly.entity.DonHang;
import com.poly.entity.TaiKhoan;
import com.poly.entity.ThanhToan;
import com.poly.entity.ThongBao;
import com.poly.repository.BienTheSanPhamRepository;
import com.poly.repository.ChiTietDonHangRepository;
import com.poly.repository.DiaChiRepository;
import com.poly.repository.DonHangRepository;
import com.poly.repository.TaiKhoanRepository;
import com.poly.repository.ThanhToanRepository;
import com.poly.repository.TrangThaiDHRepository;
import com.poly.security.CustomUserDetails;
import com.poly.service.ChiTietGioHangService;
import com.poly.service.DiaChiService;
import com.poly.service.DonHangService;
import com.poly.service.GioHangService;
import com.poly.service.SanPhamService;
import com.poly.service.TaiKhoanService;
import com.poly.service.ThongBaoService;
import com.poly.util.PhiVanChuyenUtils;
import com.poly.util.SoDienThoaiUtils;


import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;



@Controller
public class HomeController {
	private static final String PHI_VAN_CHUYEN = "phiVanChuyen";
	private static final String ID2 = "id";
	@Autowired
	private SanPhamService sanphamService;
//	@Autowired
//	private ChiTietDonHangService chitietdonhangService;
	@Autowired
	private BienTheSanPhamRepository bienthesanphamRepository;
	@Autowired
	private DonHangRepository donhangRepository;
	@Autowired
	private ChiTietDonHangRepository chitietdonhangRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private ThongBaoService thongBaoService;
//	@Autowired
//	private AnhChiTietRepository anhChiTietRepository;
	@Autowired
	private ThanhToanRepository thanhToanRepo;
	@Autowired
	private ChiTietGioHangService chitietgiohangService;
	@Autowired
	private GioHangService giohangService;
	@Autowired
	private TrangThaiDHRepository trangThaiDHRepo;
	@Autowired
	private TaiKhoanService taiKhoanService;
	@Autowired
	private TaiKhoanRepository taiKhoanRepository;
	@Autowired
	private DiaChiRepository diaChiRepository;
	@Autowired
	private DiaChiService diaChiService;
	@Autowired DonHangService donHangService;
    @Autowired
    private WebSocketNotificationController webSocketNotificationController;
    
	private final Pattern pattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z\\d])[\\S]{8,}$");
    
	@GetMapping("/home")
	public String home(Model model, HttpSession session) {
		// Mới nhất (mặc định)
		SanPhamDTO filterMoiNhat = new SanPhamDTO();
		List<SanPhamDTO> danhsachSP = sanphamService.locSanPham(filterMoiNhat);

		// Bán chạy
		SanPhamDTO filterBanChay = new SanPhamDTO();
		filterBanChay.setBanChay(true);
		List<SanPhamDTO> topBanChay = sanphamService.locSanPham(filterBanChay);
		// Giảm giá
		SanPhamDTO filterGiamGia = new SanPhamDTO();
		filterGiamGia.setGiamGia(true);
		List<SanPhamDTO> spgiamgia = sanphamService.locSanPham(filterGiamGia);
	
		// Truyền vào view
		model.addAttribute("danhsachSP", danhsachSP);
		model.addAttribute("topBanChay", topBanChay);
		model.addAttribute("spgiamgia", spgiamgia);
		model.addAttribute("content", "User/home.html");
		return "User/index";
	}
	@PostMapping("/thong-bao/{id}/da-doc")
	@ResponseBody
	public ResponseEntity<?> danhDauThongBaoDaDoc(@PathVariable Integer id) {
	    thongBaoService.danhDauDaDoc(id);
	    return ResponseEntity.ok().build();
	}



	@GetMapping("/403")
	public String accessDenied() {
		return "security/403";
	}

	@GetMapping("/product")
	public String product(@RequestParam(required = false) String keyword, @RequestParam(required = false) String loai,
			@RequestParam(required = false) String sapXep, @RequestParam(required = false) Boolean giamGia,
			Model model) {
		SanPhamDTO filter = new SanPhamDTO();
		filter.setKeyword(keyword);
		filter.setLoaiKeyword(loai);
		filter.setSapXep(sapXep);
		filter.setGiamGia(giamGia);
		List<SanPhamDTO> locsp = sanphamService.locSanPham(filter);

		model.addAttribute("tuKhoa", keyword);
		model.addAttribute("loaiDangChon", loai);
		model.addAttribute("sapXepDangChon", sapXep);
		model.addAttribute("danggiamgia", giamGia);
		model.addAttribute("danhsachSP", locsp);
		model.addAttribute("content", "User/product.html");
		return "User/index";
	}
	
	@GetMapping("/ChinhSach")
	public String getMethodName(Model model) {
		model.addAttribute("content", "User/chinhsach.html");
		return "User/index";
	}
		
	@GetMapping("/chi-tiet-san-pham/{id}")
	public String chiTietSanPham(@PathVariable(ID2) Integer id, Model model) {
		// Lấy SanPhamDTO (bao gồm cả giá sau giảm, tổng bán, điểm đánh giá)
		ChiTietSanPhamDTO chiTietSP = sanphamService.layChiTietSanPhamDTO(id);

		if (chiTietSP == null) {
			return "redirect:/404";
		}

		model.addAttribute("chiTietSP", chiTietSP); // gắn DTO chính
		model.addAttribute("dsAnhSP", chiTietSP.getDsAnhSanPham());
		model.addAttribute("dsAnhChiTiet", chiTietSP.getDsAnhChiTiet());

		// ảnh chi tiết
		model.addAttribute("chiTiet", chiTietSP.getDanhSachBienThe()); // biến thể đầu tiên (giá gốc)
		model.addAttribute("dsDanhGia", sanphamService.getDanhGiaBySanPham(id)); // danh sách đánh giá
		model.addAttribute("content", "User/ChiTietSanPham.html"); // nội dung chính

		return "User/index";
	}

	@GetMapping("/TrangCaNhan")
	public String TrangCaNhan(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
			TaiKhoan taiKhoan = userDetails.getTaiKhoan();
		    LocalDate ngaySinh = taiKhoan.getNgaySinh();
			model.addAttribute("tendn",taiKhoan.getTenDangNhap());
			model.addAttribute("avatar",taiKhoan.getAvatar());	
			model.addAttribute("ngaySinh", ngaySinh);
		    if (ngaySinh != null) {
		        model.addAttribute("ngaySinh", ngaySinh);
		        model.addAttribute("ngay", ngaySinh.getDayOfMonth());
		        model.addAttribute("thang", ngaySinh.getMonthValue());
		        model.addAttribute("nam", ngaySinh.getYear());
		    } else {
		        model.addAttribute("ngaySinh", "");
		        model.addAttribute("ngay", "");
		        model.addAttribute("thang", "");
		        model.addAttribute("nam", "");
		    }
			model.addAttribute("taikhoan",taiKhoan);	

		model.addAttribute("content","User/TrangCaNhan.html");
		return "User/index";
	}
	
	@PostMapping("/TrangCaNhan/CapNhat")
	public String capNhatThongTin(
			@RequestParam String tenDangNhap,			
	        @RequestParam String hoTen,
	        @RequestParam String email,
	        @RequestParam String soDT,
	        @RequestParam int gioiTinh,
	        @RequestParam String ngaySinh,
	        @AuthenticationPrincipal CustomUserDetails userDetails
	) {
	    // Lấy đối tượng đang đăng nhập

		TaiKhoan taiKhoan = userDetails.getTaiKhoan();

	    if (taiKhoan != null) {
	        taiKhoan.setTenDangNhap(tenDangNhap);
	        taiKhoan.setHoTen(hoTen);
	        taiKhoan.setEmail(email);
	        taiKhoan.setSoDT(soDT);
	        taiKhoan.setGioiTinh(gioiTinh);

	        try {
	            taiKhoan.setNgaySinh(LocalDate.parse(ngaySinh)); // định dạng yyyy-MM-dd
	        } catch (Exception e) {
	        	System.out.println("error Ngày sinh không hợp lệ.");
	            return "redirect:/TrangCaNhan";
	        }

	        taiKhoanService.saveTaiKhoan(taiKhoan);
	        System.out.println("success Cập nhật thành công!");
	    } else {
	    	System.out.print("error Không tìm thấy tài khoản.");
	    }

	    return "redirect:/TrangCaNhan";
	}

	@Transactional
	@GetMapping("/DiaChi")
	public String DiaChi(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
    	Integer maTK = userDetails.getTaiKhoan().getMaTK();
    	TaiKhoan taiKhoan = taiKhoanRepository.findById(maTK).orElse(null);
		model.addAttribute("tendn",taiKhoan.getTenDangNhap());
		model.addAttribute("avatar",taiKhoan.getAvatar());	
		List<DiaChi> dsDiaChi = diaChiRepository.findByTaiKhoan_MaTK(maTK);


		model.addAttribute("dsDiaChi", dsDiaChi);
		for (DiaChi d : dsDiaChi) {
		    String sdt = SoDienThoaiUtils.chuyenDinhDangQuocTe(d.getSoDienThoai());
		    d.setSoDienThoai(sdt);
		}

		model.addAttribute("dsDiaChi", dsDiaChi);

		model.addAttribute("content","User/DiaChi.html");
		return "User/index";
	}
	@PostMapping("/DiaChi/mac-dinh/{id}")
	public String thietLapMacDinh(@PathVariable("id") Integer idDiaChi,
	                              @AuthenticationPrincipal CustomUserDetails userDetails) {
	    Integer maTK = userDetails.getTaiKhoan().getMaTK();
	    TaiKhoan taiKhoan = taiKhoanRepository.findById(maTK).orElse(null);

	    if (taiKhoan == null) {
	        // Trường hợp lỗi, có thể chuyển hướng hoặc thông báo
	        return "redirect:/DiaChi?error=notfound";
	    }

	    List<DiaChi> diaChis = diaChiService.findByMaTaiKhoan(maTK);

	    for (DiaChi dc : diaChis) {
	        dc.setMacDinh(dc.getMaDiaChi().equals(idDiaChi));
	    }

	    diaChiRepository.saveAll(diaChis);

	    return "redirect:/DiaChi";
	}
	


	
	
	
    @GetMapping("/Doimatkhau")
    public String showForgotPasswordForm(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
    	Integer maTK = userDetails.getTaiKhoan().getMaTK();
    	TaiKhoan taiKhoan = taiKhoanRepository.findById(maTK).orElse(null);
		model.addAttribute("tendn",taiKhoan.getTenDangNhap());
		model.addAttribute("avatar",taiKhoan.getAvatar());		
    	model.addAttribute("content", "User/thaydoimatkhau.html"); 	
        return "User/index";
    }
    @PostMapping("/xacnhandoimatkhau")
    public String xacnhan_doimatkhau(
            @RequestParam String matKhau,
            @RequestParam String xacnhanmatkhau,
            Model model,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Integer maTK = userDetails.getTaiKhoan().getMaTK();
        TaiKhoan taiKhoan = taiKhoanRepository.findById(maTK).orElse(null);

        boolean hasError = false;

        if (matKhau == null || matKhau.trim().isEmpty()) {
            model.addAttribute("passwordError", "Mật khẩu không được để trống");
            hasError = true;
        }
		if (matKhau.length() < 8 || !pattern.matcher(matKhau).matches()) {
			model.addAttribute("passwordError", "Mật khẩu phải từ 8 ký tự, có chữ, số và ký hiệu.");
			hasError = true;
		}
        if (xacnhanmatkhau == null || !xacnhanmatkhau.equals(matKhau)) {
            model.addAttribute("confirmError", "Xác nhận mật khẩu không khớp.");
            hasError = true;
        }

        if (hasError) {
        	model.addAttribute("tendn", taiKhoan.getTenDangNhap());
        	model.addAttribute("avatar", taiKhoan.getAvatar());
        	model.addAttribute("content", "User/thaydoimatkhau.html");
        	return "User/index";
        }


        taiKhoan.setMatKhau(passwordEncoder.encode(matKhau));
        taiKhoanRepository.save(taiKhoan);

        return "redirect:/Doimatkhau";
    }

	

    @GetMapping("/DonHang")
    public String XemDonHang(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Integer maTK = userDetails.getTaiKhoan().getMaTK();
		model.addAttribute("tendn",userDetails.getTaiKhoan().getTenDangNhap());
		model.addAttribute("avatar",userDetails.getTaiKhoan().getAvatar());	
        // Gọi service để lấy danh sách DonHangDTO gồm DonHang và list ChiTietDonHang
        List<DonHangDTO> dsDonHang = donHangService.layDonHangVaChiTietTheoMaTK(maTK);

        model.addAttribute("dsDonHang", dsDonHang);
        
        model.addAttribute("content", "User/XemDonHang.html");
        return "User/index";
    }

	
	
	
	
	
	
	@GetMapping("/gio-hang")
	public String hienThiGioHang(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
			CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
			TaiKhoan taiKhoan = userDetails.getTaiKhoan();

//			System.out.println(">>> Phiên đăng nhập hợp lệ. Mã tài khoản: " + taiKhoan.getMaTK()); // TEST LOG

			List<GioHangDTO> gioHang = chitietgiohangService.layDanhSachGioHangDTO(taiKhoan.getMaTK());
			model.addAttribute("gioHang", gioHang);
		} else {
			System.out.println(">>> Không có phiên đăng nhập.");
		}
		model.addAttribute("content", "User/giohang.html");
		return "User/index";
	}

	@PostMapping("/gio-hang/cap-nhat-so-luong")
	@ResponseBody
	public ResponseEntity<?> capNhatSoLuong(@RequestBody Map<String, Object> data) {
	    try {
	        Integer maCTGH = Integer.parseInt(data.get("maCTGH").toString());
	        Integer soLuong = Integer.parseInt(data.get("soLuong").toString());

//	        System.out.println("Cập nhật: maCTGH = " + maCTGH + ", soLuong = " + soLuong);

	        boolean success = chitietgiohangService.capNhatSoLuong(maCTGH, soLuong);

	        if (success) {
	            return ResponseEntity.ok(Map.of("status", "ok"));
	        } else {
	            return ResponseEntity.status(404).body(Map.of("error", "Không tìm thấy mục cần cập nhật"));
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(500).body(Map.of("error", "Lỗi cập nhật"));
	    }
	}
	@PostMapping("/gio-hang/them")
	@ResponseBody
	public ResponseEntity<?> themVaoGio(@RequestBody Map<String, Object> data, RedirectAttributes redirectAttributes,
	                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
	    try {
	        TaiKhoan taiKhoan = userDetails.getTaiKhoan(); // ✅

	        Integer maCT = Integer.parseInt(data.get("maCT").toString());
	        Integer soLuong = Integer.parseInt(data.get("soLuong").toString());

	        boolean daThem = giohangService.themSanPhamVaoGio(taiKhoan, maCT, soLuong);

	        if (daThem) {
	        	 return ResponseEntity.ok(Map.of("message", "✅ Đã thêm vào giỏ hàng!"));
	        } else {
	            return ResponseEntity.badRequest().body(Map.of("error", "❌ Không thể thêm sản phẩm."));
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(500).body(Map.of("error", "Lỗi máy chủ"));
	    }
	}
	@PostMapping("/gio-hang/xoa")
	@ResponseBody
	public ResponseEntity<?> xoaSanPhamKhoiGio(@RequestBody Map<String, Integer> data,
	                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
	    try {
	        Integer maCTGH = data.get("maCTGH");
	        TaiKhoan taiKhoan = userDetails.getTaiKhoan(); // Lấy TaiKhoan đúng chuẩn
	        giohangService.xoaSanPham(maCTGH, taiKhoan);
	        return ResponseEntity.ok(Map.of("status", "deleted"));
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(500).body(Map.of("error", "Xóa thất bại"));
	    }
	}
	@Transactional
	@PostMapping("/thanh-toan")
	public String xuLyThanhToan(
	        @RequestParam("chonCTGH") List<Integer> maCTGHList,
	        Model model) {

	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
	    	Integer maTK = userDetails.getTaiKhoan().getMaTK();
	    	TaiKhoan taiKhoan = taiKhoanRepository.findById(maTK).orElse(null);

	    	if (taiKhoan == null) {
	    	    model.addAttribute("diaChi", "(Không tìm thấy tài khoản)");
	    	    return "User/index";
	    	}

	    	DiaChi diaChiMacDinh = taiKhoan.getDiaChiMacDinh();

	    	if (diaChiMacDinh == null) {
	    	    model.addAttribute("diaChi", "(Chưa có địa chỉ)");
	    	} else {
	    	    model.addAttribute("diaChi", diaChiMacDinh.getDiaChiDayDu());
		        String sdtGoc = diaChiMacDinh.getSoDienThoai();
		        String sdtDinhDang = SoDienThoaiUtils.chuyenDinhDangQuocTe(sdtGoc);
		        model.addAttribute("soDienThoai", sdtDinhDang);
		        model.addAttribute("hoTen", diaChiMacDinh.getHoTen());
		        System.out.print("name: " + diaChiMacDinh.getHoTen());
			    if (sdtDinhDang != null && !sdtDinhDang.trim().isEmpty()) {
			        model.addAttribute("soDienThoai", sdtDinhDang);
			    } else {
			        model.addAttribute("soDienThoai", "(Chưa có số điện thoại)");
			    }
			    if (diaChiMacDinh.getHoTen() != null && !diaChiMacDinh.getHoTen().trim().isEmpty()) {
			        model.addAttribute("hoTen", diaChiMacDinh.getHoTen());
			    } else {
			        model.addAttribute("hoTen", "(Chưa có họ và tên)");
			    }
	    	}
	        List<GioHangDTO> dsThanhToan = chitietgiohangService
	                .layGioHangDTOTheoDanhSachCTGH(maTK, maCTGHList);

	        int tongSoLuong = 0;
	        BigDecimal tongTien = BigDecimal.ZERO;

	        for (GioHangDTO sp : dsThanhToan) {
	            int soLuong = sp.getChiTietGioHang().getSoLuong();
	            BigDecimal gia = sp.getChiTietGioHang().getGiaTienThucTe();
	            tongSoLuong += soLuong;
	            tongTien = tongTien.add(gia.multiply(BigDecimal.valueOf(soLuong)));
	        }

	        PhiVanChuyenUtils.KetQuaVanChuyen ketQua = PhiVanChuyenUtils.tinhVanChuyen(diaChiMacDinh.getDiaChiDayDu(), tongTien.intValue());
	        int phiVanChuyen = ketQua.getPhiVanChuyen();
	        String ngayGiaoTu = ketQua.getNgayGiaoDuKienTu();
	        String ngayGiaoDen = ketQua.getNgayGiaoDuKienDen();

	        BigDecimal tongThanhToan = tongTien.add(BigDecimal.valueOf(phiVanChuyen));


	        model.addAttribute("dsThanhToan", dsThanhToan);
	        model.addAttribute("tongSoLuong", tongSoLuong);
	        model.addAttribute("tongTien", tongTien);
	        model.addAttribute("phiVanChuyen", phiVanChuyen);
	        model.addAttribute("tongThanhToan", tongThanhToan);
	        model.addAttribute("ngayGiaoTu", ngayGiaoTu);
	        model.addAttribute("ngayGiaoDen", ngayGiaoDen);
	    }

	    model.addAttribute("content", "User/thanhtoan.html");
	    return "User/index";
	}

	@PostMapping("/gio-hang/xoa-nhieu")
	@ResponseBody
	public ResponseEntity<?> xoaNhieuSanPham(@RequestBody Map<String, List<Integer>> payload) {
	    List<Integer> dsMaCTGH = payload.get("dsMaCTGH");

	    try {
	        chitietgiohangService.xoaNhieuTheoMa(dsMaCTGH); // bạn cần có service xử lý
	        return ResponseEntity.ok(Map.of("success", true));
	    } catch (Exception e) {
	        return ResponseEntity.badRequest().body(Map.of("success", false, "error", e.getMessage()));
	    }
	}
	@Transactional
	@PostMapping("/dat-hang")
	public String xuLyDatHang(
	        @RequestParam("maBienThe") List<Integer> maBienTheList,
	        @RequestParam("soLuong") List<Integer> soLuongList,
	        @RequestParam("donGia") List<BigDecimal> donGiaList,
	        @RequestParam("giaThucTe") List<BigDecimal> giaThucTeList,
	        @RequestParam("phuongThucThanhToan") String phuongThucThanhToan,
	        @RequestParam(PHI_VAN_CHUYEN) BigDecimal phiVanChuyen,
	        @RequestParam String ngayGiaoDuKien,
	        Model model,
	        @AuthenticationPrincipal CustomUserDetails userDetails) {

	    System.out.println(">>> Nhận được dữ liệu đặt hàng:");

	    // In dữ liệu kiểm tra
	    for (int i = 0; i < maBienTheList.size(); i++) {
	        System.out.println("Sản phẩm " + (i + 1));
	        System.out.println(" - Mã biến thể: " + maBienTheList.get(i));
	        System.out.println(" - Số lượng: " + soLuongList.get(i));
	        System.out.println(" - Đơn giá: " + donGiaList.get(i));
	        System.out.println(" - Giá thực tế: " + giaThucTeList.get(i));
	    
	    }
	    System.out.println(" - Ngày giao dự kiến: " + ngayGiaoDuKien);
	    // ✅ Bước 1: Lấy thông tin tài khoản từ session
    	Integer maTK = userDetails.getTaiKhoan().getMaTK();
    	TaiKhoan taiKhoan = taiKhoanRepository.findById(maTK).orElse(null);

    	DiaChi diaChiMacDinh = taiKhoan.getDiaChiMacDinh();

    	if (diaChiMacDinh == null) {
	        System.out.println("Không tìm thấy địa chỉ mặc định.");
	        return "redirect:/TrangCaNhan";
    	} else {
    	    model.addAttribute("diaChi", diaChiMacDinh.getDiaChiDayDu());
    	}
	    String diaChiDayDu = diaChiMacDinh.getDiaChiDayDu(); // ✅ Đây là String
	    // ✅ Bước 2: Tạo đơn hàng mới
	    DonHang donHang = new DonHang();
	    donHang.setTaiKhoan(taiKhoan);
	    donHang.setTrangThaiDH(trangThaiDHRepo.findById(1).orElse(null)); // 1 = Chờ xác nhận
	    donHang.setNgayDat(LocalDateTime.now());
	    donHang.setDiaChiGiaoHang(diaChiDayDu); // hoặc có thể lấy từ form
	    donHang.setPhiVanChuyen(phiVanChuyen); 
	    donHang.setNgayGiaoDuKien(ngayGiaoDuKien);

	    BigDecimal tongTienCTT = BigDecimal.ZERO;
	    BigDecimal tongGiamGia = BigDecimal.ZERO;

	    List<ChiTietDonHang> chiTietList = new ArrayList<>();

	    // ✅ Bước 3: Duyệt từng sản phẩm để tạo ChiTietDonHang
	    for (int i = 0; i < maBienTheList.size(); i++) {
	        Integer maBT = maBienTheList.get(i);
	        Integer sl = soLuongList.get(i);
	        BigDecimal donGia = donGiaList.get(i);
	        BigDecimal giaThucTe = giaThucTeList.get(i);

	        BigDecimal tienThucTe = giaThucTe.multiply(BigDecimal.valueOf(sl));
	        BigDecimal tienGoc = donGia.multiply(BigDecimal.valueOf(sl));
	        BigDecimal giamGia = donGia.subtract(giaThucTe).multiply(BigDecimal.valueOf(sl));

	        // Tính tổng cộng
	        tongTienCTT = tongTienCTT.add(tienGoc);
	        tongGiamGia = tongGiamGia.add(giamGia);

	        // Lấy biến thể sản phẩm
	        BienTheSanPham bienThe = bienthesanphamRepository.findById(maBT).orElse(null);
	        if (bienThe == null) continue;

	        // Tạo chi tiết đơn hàng
	        ChiTietDonHang ct = new ChiTietDonHang();
	        ct.setDonHang(donHang);
	        ct.setBienTheSanPham(bienThe);
	        ct.setSoLuongSP(sl);
	        ct.setDonGia(donGia);
	        ct.setGiamGiaThucTe(giamGia);
	        ct.setThanhTien(tienThucTe);

	        chiTietList.add(ct);
	    }

	    // ✅ Bước 4: Gán thông tin tổng vào đơn hàng
	    donHang.setTongTienCTT(tongTienCTT);
	    donHang.setGiamGiaThucTe(tongGiamGia);
	    donHang.setThanhTien(tongTienCTT.subtract(tongGiamGia).add(donHang.getPhiVanChuyen()));

	    // ✅ Bước 5: Lưu đơn hàng và chi tiết
	    donhangRepository.save(donHang);
	    chitietdonhangRepository.saveAll(chiTietList);

	    System.out.println(">>> ✅ Đặt hàng thành công. Đơn hàng mã: " + donHang.getMaDH());
	 // ✅ Bước 6: Tạo bản ghi thanh toán
	    ThanhToan thanhToan = new ThanhToan();
	    thanhToan.setDonHang(donHang);
	    thanhToan.setSoTien(donHang.getThanhTien());  // hoặc tongTienCTT.subtract(tongGiamGia).add(phiVanChuyen)
	    thanhToan.setPhuongThucTT(phuongThucThanhToan); // hoặc lấy từ form nếu có nhiều phương thức
	    thanhToan.setTrangThai("Chưa thanh toán"); // Mặc định trạng thái
	    thanhToan.setGhiChuTT("");

	    // ✅ Không set ngay ngày thanh toán (để rỗng, chờ cập nhật sau)
	    thanhToanRepo.save(thanhToan);

	    System.out.println(">>> ✅ Đã tạo bản ghi thanh toán mặc định cho đơn hàng " + donHang.getMaDH());

	    return "User/thanh-cong";
	}

	@PostMapping("/upload-avatar")
	public String uploadAvatar(@RequestParam("file") MultipartFile file,
	                           @AuthenticationPrincipal CustomUserDetails userDetails,
	                           HttpSession session,
	                           RedirectAttributes redirectAttributes) {
	    try {
	        // Lấy thông tin người dùng
	        Integer maTK = userDetails.getTaiKhoan().getMaTK();

	        // Tạo tên file ngẫu nhiên (UUID + đuôi gốc)
	        String originalFilename = file.getOriginalFilename();
	        if (originalFilename == null || originalFilename.isEmpty()) {
	            redirectAttributes.addFlashAttribute("error", "File không hợp lệ!");
	            return "redirect:/TrangCaNhan";
	        }

	        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
	        String randomFileName = UUID.randomUUID().toString() + extension;

	        // Đường dẫn lưu file (thư mục static/meme)
	        String uploadDir = new File("src/main/resources/static/avatar").getAbsolutePath();
	        File destFile = new File(uploadDir, randomFileName);
	        file.transferTo(destFile);

	        // Tạo URL truy cập
	        String url = "/avatar/" + randomFileName;

	        // Cập nhật avatar trong database
	        TaiKhoan taiKhoan = taiKhoanRepository.findById(maTK).orElse(null);
	        if (taiKhoan != null) {
	            taiKhoan.setAvatar(url);
	            taiKhoanRepository.save(taiKhoan);

	            // Cập nhật lại trong session (nếu có dùng để hiển thị)
	            userDetails.getTaiKhoan().setAvatar(url); // cập nhật thông tin trong phiên hiện tại
	            session.setAttribute("userDetails", userDetails); // nếu bạn dùng session lưu
	        }

	        redirectAttributes.addFlashAttribute("success", "Tải ảnh thành công!");
	    } catch (IOException e) {
	        e.printStackTrace();
	        redirectAttributes.addFlashAttribute("error", "Lỗi khi tải ảnh!");
	    }

	    return "redirect:/TrangCaNhan";
	}




	
	@GetMapping("/xac-nhan-nhan-hang/{maDH}")
    public ResponseEntity<String> xacNhanNhanHang(@PathVariable("maDH") int maDH) {
        try {
            donHangService.xacNhanNhanHang(maDH);
            return ResponseEntity.ok("Đơn hàng đã được xác nhận là đã nhận hàng.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }
	
	@GetMapping("/xac-nhan-da-thanh-toan/{maDH}")
	public ResponseEntity<?> xacNhanDaThanhToan(@PathVariable int maDH) {
	    DonHang dh = donHangService.capNhatTrangThaiThanhToan(maDH);
	    String noiDung = "Đơn hàng: DH" + maDH + " đã giao. Vui lòng kiểm tra và xác nhận nếu không có vấn đề!";

	    // 🌟 1. Lưu thông báo vào DB
	    Integer maTK = dh.getTaiKhoan().getMaTK(); // Lấy ra ID

	    TaiKhoan taiKhoan = new TaiKhoan(); // Tạo đối tượng rỗng
	    taiKhoan.setMaTK(maTK); // Chỉ cần set MaTK

	    ThongBao thongBao = new ThongBao();
	    thongBao.setTaiKhoan(taiKhoan);// ✅ Đã được attach vào context// Truyền đối tượng TaiKhoan, KHÔNG phải MaTK
	    thongBao.setNoiDung(noiDung);
	    thongBao.setUrl("/DonHang");
	    thongBao.setNgayTao(LocalDateTime.now());
	    thongBao.setDaDoc(false);

	    ThongBao daLuu = thongBaoService.taoThongBao(thongBao);




	    // 🌟 2. Gửi WebSocket tới client
	    webSocketNotificationController.guiThongBaoDonHang(dh.getTaiKhoan().getMaTK(), noiDung, daLuu.getMaThongBao(), daLuu.getUrl() );
	    System.out.println(">>> Gửi WebSocket tới /topic/user/" + dh.getTaiKhoan().getMaTK() + " với nội dung: " + noiDung);

	    return ResponseEntity.ok("Xác nhận thành công");
	}


	
	
	@ModelAttribute
	public void addCsrfToken(Model model, CsrfToken csrfToken) {
		model.addAttribute("_csrf", csrfToken);
	}

}
