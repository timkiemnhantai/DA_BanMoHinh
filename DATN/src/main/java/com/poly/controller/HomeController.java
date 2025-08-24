package com.poly.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

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
import com.poly.model.BienTheSanPham;
import com.poly.model.ChiTietDonHang;
import com.poly.model.ChiTietGioHang;
import com.poly.model.DanhGiaMedia;
import com.poly.model.DanhGiaSP;
import com.poly.model.DiaChi;
import com.poly.model.DonHang;
import com.poly.model.TaiKhoan;
import com.poly.model.ThanhToan;
import com.poly.model.ThongBao;
import com.poly.repository.BienTheSanPhamRepository;
import com.poly.repository.ChiTietDonHangRepository;
import com.poly.repository.DanhGiaMediaRepository;
import com.poly.repository.DanhGiaSPRepository;
import com.poly.repository.DiaChiRepository;
import com.poly.repository.DonHangRepository;
import com.poly.repository.SanPhamRepository;
import com.poly.repository.TaiKhoanRepository;
import com.poly.repository.ThanhToanRepository;
import com.poly.repository.TrangThaiDHRepository;
import com.poly.security.CustomUserDetails;
import com.poly.service.BaoLoiService;
import com.poly.service.BienTheSanPhamService;
import com.poly.service.ChiTietGioHangService;
import com.poly.service.DiaChiService;
import com.poly.service.DonHangService;
import com.poly.service.GioHangService;
import com.poly.service.OrderTokenService;
import com.poly.service.SanPhamService;
import com.poly.service.TaiKhoanService;
import com.poly.service.ThanhToanService;
import com.poly.service.ThongBaoService;
import com.poly.util.PhiVanChuyenUtils;
import com.poly.util.SoDienThoaiUtils;
import com.poly.util.ThongTinDatHangValidator;
import com.poly.util.ThongTinDatHangValidator.ThongTinDatHangResult;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;



@Controller
public class HomeController {
	private static final String PHUONG_THUC_SHIPPING = "phuongThucShipping";
	private static final String PHI_VAN_CHUYEN2 = "phiVanChuyen";
	private static final String PHUONG_THUC_THANH_TOAN = "phuongThucThanhToan";
	private static final String ID2 = "id";
	@Autowired
	private SanPhamService sanphamService;
	@Autowired
	private SanPhamRepository sanPhamRepository;
	@Autowired
	private BienTheSanPhamRepository bienthesanphamRepository;
	@Autowired
	private BienTheSanPhamService bienTheSanPhamService;
	@Autowired
	private DonHangRepository donhangRepository;
	@Autowired
	private ChiTietDonHangRepository chitietdonhangRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private ThongBaoService thongBaoService;
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
	@Autowired
	private DonHangService donHangService;
	@Autowired
	private DonHangRepository donHangRepository;
    @Autowired
    private WebSocketNotificationController webSocketNotificationController;
	@Autowired
    private OrderTokenService tokenService;
	@Autowired
	private BienTheSanPhamRepository bienTheSanPhamRepository;
	@Autowired
	private DanhGiaSPRepository danhGiaSPRepository;
	@Autowired 
	private DanhGiaMediaRepository danhGiaMediaRepository;
    @Autowired
    private TrangThaiDHRepository trangThaiRepository;
    @Autowired
    private ThanhToanService thanhToanService;
    @Autowired
    private BaoLoiService baoLoiService;
	private final Pattern pattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z\\d])[\\S]{8,}$");
    
	@GetMapping({"/home", "/"})
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
	    List<DanhGiaSP> dsDanhGia = chiTietSP.getDanhSachDanhGia();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
		model.addAttribute("chiTietSP", chiTietSP); // gắn DTO chính
		model.addAttribute("dsAnhSP", chiTietSP.getDsAnhSanPham());
		model.addAttribute("dsAnhChiTiet", chiTietSP.getDsAnhChiTiet());

		List<String> ngayDangFormattedList = dsDanhGia.stream()
		        .map(dg -> dg.getNgayDang().format(formatter))
		        .collect(Collectors.toList());
		model.addAttribute("chiTiet", chiTietSP.getDanhSachBienThe()); 
		model.addAttribute("danhGia", dsDanhGia);
		model.addAttribute("ngayDangFormattedList", ngayDangFormattedList);
	    model.addAttribute("soLuongDanhGia", dsDanhGia != null ? dsDanhGia.size() : 0);
	    model.addAttribute("mota", chiTietSP.getMoTaSanPham());
//		List<DanhGiaSP> list = chiTietSP.getDanhSachDanhGia();
//		System.out.println("Số lượng đánh giá: " + (list == null ? 0 : list.size()));
//		list.forEach(dg -> System.out.println("DG id=" + dg.getMaDG() + ", ten=" + dg.getTaiKhoan().getHoTen()));

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
	        @RequestParam(required = false) Integer gioiTinh,
	        @RequestParam(required = false) String ngaySinh,
	        @RequestParam(name = "file", required = false) MultipartFile file,
	        @AuthenticationPrincipal CustomUserDetails userDetails,
	        HttpSession session,
	        RedirectAttributes redirectAttributes
	) {
	    TaiKhoan taiKhoan = userDetails.getTaiKhoan();

	    if (taiKhoan != null) {
	        taiKhoan.setTenDangNhap(tenDangNhap);
	        taiKhoan.setHoTen(hoTen);
	        taiKhoan.setEmail(email);
	        taiKhoan.setSoDT(soDT);
	        taiKhoan.setGioiTinh(gioiTinh);

	        if (gioiTinh != null) {
	            taiKhoan.setGioiTinh(gioiTinh);
	        }

	        if (ngaySinh != null && !ngaySinh.isBlank()) {
	            try {
	                taiKhoan.setNgaySinh(LocalDate.parse(ngaySinh));
	            } catch (Exception e) {
	                redirectAttributes.addFlashAttribute("error", "Ngày sinh không hợp lệ.");
	                return "redirect:/TrangCaNhan";
	            }
	        }


	        if (file != null && !file.isEmpty()) {
	            try {
	                long maxFileSize = 5 * 1024 * 1024; // 5MB
	                if (file.getSize() > maxFileSize) {
	                    redirectAttributes.addFlashAttribute("error", "❌ Dung lượng ảnh vượt quá 5MB!");
	                    return "redirect:/TrangCaNhan";
	                }
	            	
	                String originalFilename = file.getOriginalFilename();
	                String extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
	                List<String> allowedExtensions = Arrays.asList(".jpg", ".jpeg", ".png", ".gif");

	                if (!allowedExtensions.contains(extension)) {
	                    redirectAttributes.addFlashAttribute("error", "❌ Định dạng ảnh không hợp lệ!");
	                    return "redirect:/TrangCaNhan";
	                }

	                BufferedImage image = ImageIO.read(file.getInputStream());
	                if (image == null) {
	                    redirectAttributes.addFlashAttribute("error", "❌ File không phải là ảnh hợp lệ!");
	                    return "redirect:/TrangCaNhan";
	                }
	                int width = image.getWidth();
	                int height = image.getHeight();
	                int maxWidth = 1500;  // Giới hạn chiều rộng
	                int maxHeight = 1500; // Giới hạn chiều cao

	                if (width > maxWidth || height > maxHeight) {
	                    redirectAttributes.addFlashAttribute("error",
	                        "❌ Ảnh vượt quá kích thước cho phép (" + maxWidth + "x" + maxHeight + " px)!");
	                    return "redirect:/TrangCaNhan";
	                }

	                // Tạo tên file ngẫu nhiên
	                String randomFileName = UUID.randomUUID().toString() + extension;

	                // Đường dẫn thư mục lưu file
	                String uploadDir = new File("uploads/avatar").getAbsolutePath();
	                
	                // Tạo file đích và lưu
	                File destFile = new File(uploadDir, randomFileName);
	                file.transferTo(destFile);

	                // Đường dẫn truy cập ảnh
	                String url = "/uploads/avatar/" + randomFileName;
	                taiKhoan.setAvatar(url);

	                // Cập nhật session
	                userDetails.getTaiKhoan().setAvatar(url);
	                session.setAttribute("userDetails", userDetails);

	            } catch (IOException e) {
	                e.printStackTrace();
	                redirectAttributes.addFlashAttribute("error", "❌ Lỗi khi lưu ảnh!");
	                return "redirect:/TrangCaNhan";
	            }
	        }


	        taiKhoanService.saveTaiKhoan(taiKhoan);
	        redirectAttributes.addFlashAttribute("success", "✅ Cập nhật hồ sơ thành công!");
	    } else {
	        redirectAttributes.addFlashAttribute("error", "❌ Không tìm thấy tài khoản.");
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
		model.addAttribute("dsDiaChi", dsDiaChi);
		model.addAttribute("content","User/DiaChi.html");
		return "User/index";
	}
	@PostMapping("/DiaChi/CapNhat")
	public String hienFormCapNhatDiaChi(
	        @RequestParam("maDC") Integer maDC,
	        Model model,
	        @AuthenticationPrincipal CustomUserDetails userDetails) {
	    
	    Integer maTK = userDetails.getTaiKhoan().getMaTK();
	    TaiKhoan taiKhoan = taiKhoanRepository.findById(maTK).orElse(null);
	    DiaChi diaChi = diaChiRepository.findById(maDC).orElse(null);

	    model.addAttribute("tendn", taiKhoan.getTenDangNhap());
	    model.addAttribute("avatar", taiKhoan.getAvatar());
	    
	    List<DiaChi> dsDiaChi = diaChiRepository.findByTaiKhoan_MaTK(maTK);
	    for (DiaChi d : dsDiaChi) {
	        String sdt = SoDienThoaiUtils.chuyenDinhDangQuocTe(d.getSoDienThoai());
	        d.setSoDienThoai(sdt);
	    }
	    model.addAttribute("dsDiaChi", dsDiaChi);

	    if (diaChi == null || !diaChi.getTaiKhoan().getMaTK().equals(maTK)) {
	        return "redirect:/DiaChi";
	    }

	    // Đổi SDT về số 0
	    String sdtGoc = SoDienThoaiUtils.chuyenVeSo0(diaChi.getSoDienThoai());
	    diaChi.setSoDienThoai(sdtGoc);
	    
	    model.addAttribute("diaChi", diaChi);
	    
	    
	    model.addAttribute("content", "User/DiaChi.html");
	    return "User/index";
	}

	@PostMapping("/DiaChi/Xoa")
	public String xoaDiaChi(@RequestParam("maDC") Integer maDC,
	                        @AuthenticationPrincipal CustomUserDetails userDetails,
	                        RedirectAttributes redirectAttributes) {
	    Integer maTK = userDetails.getTaiKhoan().getMaTK();

	    DiaChi diaChi = diaChiRepository.findById(maDC).orElse(null);
	    if (diaChi != null && diaChi.getTaiKhoan().getMaTK().equals(maTK)) {
	        boolean laMacDinh = diaChi.getMacDinh(); 
	        diaChiRepository.deleteById(maDC);
	        if (laMacDinh) {
	            List<DiaChi> danhSachConLai = diaChiRepository.findByTaiKhoan_MaTK(maTK);

	            if (!danhSachConLai.isEmpty()) {
	                DiaChi moi = danhSachConLai.get(0);
	                moi.setMacDinh(true);
	                diaChiRepository.save(moi);
	            }
	        }

	        redirectAttributes.addFlashAttribute("success", "✅ Đã xóa địa chỉ thành công!");
	    } else {
	        redirectAttributes.addFlashAttribute("error", "❌ Không thể xóa địa chỉ.");
	    }

	    return "redirect:/DiaChi";
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
	@PostMapping("/Themdiachi")
	public String themHoacCapNhatDiaChi(@RequestParam(required = false) Integer maDC,
	                                    @RequestParam String fullName,
	                                    @RequestParam String phoneNumber,
	                                    @RequestParam String provinceName,
	                                    @RequestParam String districtName,
	                                    @RequestParam String wardName,
	                                    @RequestParam String street,
	                                    @AuthenticationPrincipal CustomUserDetails userDetails,
	                                    RedirectAttributes redirectAttributes) {

	    Integer maTK = userDetails.getTaiKhoan().getMaTK();
	    TaiKhoan taiKhoan = taiKhoanRepository.findById(maTK).orElse(null);

	    DiaChi diaChi;
	    boolean isUpdate = false;
	    if (maDC != null) {
	        // Nếu có mã địa chỉ, tìm và cập nhật
	        diaChi = diaChiRepository.findById(maDC).orElse(null);
	        if (diaChi == null || !diaChi.getTaiKhoan().getMaTK().equals(maTK)) {
	            redirectAttributes.addFlashAttribute("error", "Không thể cập nhật địa chỉ này.");
	            return "redirect:/DiaChi"; // Không hợp lệ
	        }
	        isUpdate = true;
	    } else {
	        // Nếu không có mã địa chỉ, tạo mới
	        diaChi = new DiaChi();
	        diaChi.setTaiKhoan(taiKhoan);

	        boolean daCoDiaChi = diaChiRepository.existsByTaiKhoan_MaTK(maTK);
	        diaChi.setMacDinh(!daCoDiaChi); // Lần đầu tạo thì đặt mặc định
	    }

	    // Gán thông tin
	    diaChi.setHoTen(fullName);

	    diaChi.setSoDienThoai(phoneNumber);
	    diaChi.setTinh(provinceName);
	    diaChi.setQuan(districtName);
	    diaChi.setPhuong(wardName);
	    diaChi.setDiaChiChiTiet(street);

	    diaChiRepository.save(diaChi);
	    if (isUpdate) {
	        redirectAttributes.addFlashAttribute("success", "✅ Cập nhật địa chỉ thành công!");
	    } else {
	        redirectAttributes.addFlashAttribute("success", "✅ Thêm địa chỉ mới thành công!");
	    }
	    
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
    public String XemDonHang(Model model,
                              @AuthenticationPrincipal CustomUserDetails userDetails,
                              @RequestParam(value = "trangThai", required = false) Integer maTTDH) {

    	Integer maTK = userDetails.getTaiKhoan().getMaTK();
    	TaiKhoan taiKhoan = taiKhoanRepository.findById(maTK).orElse(null);
		model.addAttribute("tendn",taiKhoan.getTenDangNhap());
		model.addAttribute("avatar",taiKhoan.getAvatar());	

        List<DonHangDTO> dsDonHang;

        if (maTTDH != null) {
            dsDonHang = donHangService.layDonHangVaChiTietTheoMaTKVaTrangThai(maTK, maTTDH);
        } else {
            dsDonHang = donHangService.layDonHangVaChiTietTheoMaTK(maTK);
        }

        model.addAttribute("dsDonHang", dsDonHang);
        model.addAttribute("trangThai", maTTDH); 
        model.addAttribute("content", "User/XemDonHang.html");
        return "User/index";
    }
    
    @PostMapping("/DonHang/huy")
    public String huyDonHang(@RequestParam("maDH") Integer maDH,
                             @RequestParam("lyDoHuy") String lyDoHuy,
                             RedirectAttributes redirectAttributes) {

        DonHang donHang = donHangRepository.findById(maDH)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        int maTT = donHang.getTrangThaiDH().getMaTTDH();

        if(maTT == 1) {
            // Đơn chờ xác nhận → hủy trực tiếp, cộng lại tồn kho
            List<ChiTietDonHang> chiTietList = chitietdonhangRepository.findByDonHang(donHang);
            for (ChiTietDonHang ct : chiTietList) {
                BienTheSanPham bienThe = ct.getBienTheSanPham();

                int soLuongTonKho = bienThe.getSoLuongTonKho() != null ? bienThe.getSoLuongTonKho() : 0;
                int soLuongDatGiu = bienThe.getSoLuongDatGiu() != null ? bienThe.getSoLuongDatGiu() : 0;

                bienThe.setSoLuongTonKho(soLuongTonKho + ct.getSoLuongSP());
                bienThe.setSoLuongDatGiu(Math.max(soLuongDatGiu - ct.getSoLuongSP(), 0));

                bienTheSanPhamService.capNhatTrangThaiKho(bienThe);
            }

            donHang.setTrangThaiDH(trangThaiRepository.findById(5).orElseThrow()); // Đã hủy

        } else if(maTT == 2 || maTT == 3) {
            donHang.setTrangThaiTruoc(donHang.getTrangThaiDH());
            // Đơn đã xác nhận hoặc đang giao → yêu cầu hủy
            donHang.setTrangThaiDH(trangThaiRepository.findById(8).orElseThrow()); // Yêu cầu hủy
        } else {
            redirectAttributes.addFlashAttribute("error", "Không thể hủy đơn hàng ở trạng thái này!");
            return "redirect:/DonHang";
        }

        donHang.setGhiChu(lyDoHuy);
        donHangRepository.save(donHang);

        redirectAttributes.addFlashAttribute("success", 
            maTT == 1 ? "Hủy đơn hàng thành công!" : "Đã gửi yêu cầu hủy đơn hàng!");
        return "redirect:/DonHang";
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
	public ResponseEntity<?> themVaoGio(@RequestBody Map<String, Object> data,
	                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
	    try {
	        TaiKhoan taiKhoan = userDetails.getTaiKhoan();

	        int maCT = Integer.parseInt(String.valueOf(data.get("maCT")));
	        int soLuong = Integer.parseInt(String.valueOf(data.get("soLuong")));
	        if (soLuong <= 0) {
	            return ResponseEntity.badRequest().body(Map.of("error", "Số lượng không hợp lệ"));
	        }

	        BienTheSanPham bienThe = bienTheSanPhamRepository.findById(maCT)
	            .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

	        // VÌ bạn đã trừ tồn khi đặt hàng → KHÔNG trừ đặt giữ nữa
	        int tonCoTheBan = Math.max(0, Optional.ofNullable(bienThe.getSoLuongTonKho()).orElse(0));

	        if (tonCoTheBan <= 0) {
	            return ResponseEntity.status(409).body(Map.of("error", "❌ Sản phẩm đã hết hàng"));
	        }
	        if (soLuong > tonCoTheBan) {
	            return ResponseEntity.status(409).body(Map.of(
	                "error", "⚠️ Chỉ còn " + tonCoTheBan + " sản phẩm trong kho",
	                "available", tonCoTheBan
	            ));
	        }

	        ChiTietGioHang ctghMoi = giohangService.themSanPhamVaoGio(taiKhoan, maCT, soLuong);
	        if (ctghMoi == null) {
	            return ResponseEntity.badRequest().body(Map.of("error", "❌ Không thể thêm sản phẩm."));
	        }
	        return ResponseEntity.ok(Map.of(
	            "message", "✅ Đã thêm vào giỏ hàng!",
	            "maCTGH", ctghMoi.getMaCTGH()
	        ));
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

	    	ThongTinDatHangResult ketQua1 = ThongTinDatHangValidator.kiemTraThongTinDatHang(taiKhoan);
	    	if (ketQua1 != null && ketQua1.coLoi()) {
	    	    String noiDung = ketQua1.getLoi();
	    	    String url = ketQua1.getUrl();
	    	    ThongBao thongBao = new ThongBao();
	    	    thongBao.setTaiKhoan(taiKhoan);
	    	    thongBao.setNoiDung(noiDung);
	    	    thongBao.setUrl(url);
	    	    thongBao.setNgayTao(LocalDateTime.now());
	    	    thongBao.setDaDoc(false);

	    	    ThongBao daLuu = thongBaoService.taoThongBao(thongBao);
	    	    webSocketNotificationController.guiThongBaoDonHang(maTK, noiDung, daLuu.getMaThongBao(), daLuu.getUrl() );
	            return "redirect:/gio-hang";
	        }
	    	DiaChi diaChiMacDinh = taiKhoan.getDiaChiMacDinh();
	    	model.addAttribute("diaChi", diaChiMacDinh.getDiaChiDayDu());

	        String sdtDinhDang = diaChiMacDinh.getSoDienThoaiQuocTe();
	        model.addAttribute("soDienThoai", sdtDinhDang);
	        model.addAttribute("hoTen", diaChiMacDinh.getHoTen());
	        model.addAttribute("macDinh", diaChiMacDinh.getMacDinh());
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
	        List<PhiVanChuyenUtils.KetQuaVanChuyen> dsPhuongThuc =
	                PhiVanChuyenUtils.tinhTatCaPhuongThuc(diaChiMacDinh.getDiaChiDayDu(), tongTien.intValue());
	        model.addAttribute("dsPhuongThuc", dsPhuongThuc);

	        BigDecimal tongThanhToan = tongTien.add(BigDecimal.valueOf(phiVanChuyen));
	        List<DiaChi> dsDiaChi = diaChiRepository.findByTaiKhoan_MaTK(maTK);
	        
	        model.addAttribute("dsDiaChi",dsDiaChi);
	        model.addAttribute("dsThanhToan", dsThanhToan);
	        model.addAttribute("tongSoLuong", tongSoLuong);
	        model.addAttribute("tongTien", tongTien);
	        model.addAttribute(PHI_VAN_CHUYEN2, phiVanChuyen);
	        model.addAttribute("tongThanhToan", tongThanhToan);
	        model.addAttribute("ngayGiaoTu", ngayGiaoTu);
	        model.addAttribute("ngayGiaoDen", ngayGiaoDen);
	    }

	    model.addAttribute("content", "User/thanhtoan.html");
	    return "User/index";
	}


	@GetMapping("/lay-phi-van-chuyen")
	@ResponseBody
	public ResponseEntity<?> layPhiVanChuyen(
	        @RequestParam String phuongThuc,
	        @RequestParam String diaChi,
	        @RequestParam int tongTien) {
	    PhiVanChuyenUtils.KetQuaVanChuyen ketQua =
	            PhiVanChuyenUtils.layKetQuaTheoPhuongThuc(diaChi, tongTien, phuongThuc);

	    if (ketQua == null) {
	        return ResponseEntity.badRequest()
	                .body(Map.of("error", "Phương thức vận chuyển không hợp lệ"));
	    }
	    Map<String, Object> result = new HashMap<>();
	    result.put("phiVanChuyen", ketQua.getPhiVanChuyen());
	    result.put("ngayGiaoTu", ketQua.getNgayGiaoDuKienTu());
	    result.put("ngayGiaoDen", ketQua.getNgayGiaoDuKienDen());

	    return ResponseEntity.ok(result);
	}




	
	
	
	@PostMapping("/gio-hang/xoa-nhieu")
	@ResponseBody
	public ResponseEntity<?> xoaNhieuSanPham(@RequestBody Map<String, List<Integer>> payload) {
	    List<Integer> dsMaCTGH = payload.get("dsMaCTGH");

	    try {
	        chitietgiohangService.xoaNhieuTheoMa(dsMaCTGH); 
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
	        @RequestParam(PHUONG_THUC_THANH_TOAN) String phuongThucThanhToan,
	        @RequestParam("hoTen") String hoTenForm,
	        @RequestParam("soDienThoai") String soDienThoaiForm,
	        @RequestParam("diaChi") String diaChiForm,
	        @RequestParam(PHUONG_THUC_SHIPPING) String phuongThucShipping,
	        @RequestParam(PHI_VAN_CHUYEN2) BigDecimal phiVanChuyen,
	        @RequestParam String ngayGiaoDuKien,
	        Model model,
	        @AuthenticationPrincipal CustomUserDetails userDetails,
	        RedirectAttributes redirectAttributes) {

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
    	String diaChiGiaoHang = diaChiForm;  // ✅ Đây là String
	    // ✅ Bước 2: Tạo đơn hàng mới
	    DonHang donHang = new DonHang();
	    donHang.setTaiKhoan(taiKhoan);
	    donHang.setTrangThaiDH(trangThaiDHRepo.findById(1).orElse(null)); // 1 = Chờ xác nhận
	    donHang.setNgayDat(LocalDateTime.now());
	    donHang.setHoTen(hoTenForm);
	    
	    donHang.setSoDienThoai(SoDienThoaiUtils.chuyenVeSo0(soDienThoaiForm));
	    donHang.setPhuongThucVanChuyen(phuongThucShipping);
	    donHang.setDiaChiGiaoHang(diaChiGiaoHang); // hoặc có thể lấy từ form
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

	        BienTheSanPham bienThe = bienthesanphamRepository.findById(maBT).orElse(null);
	        if (bienThe == null) continue;

	        // Kiểm tra tồn kho đủ (tồn kho - đặt giữ >= sl)
	        int tonKhoHienTai = bienThe.getSoLuongTonKho() != null ? bienThe.getSoLuongTonKho() : 0;
	        int datGiuHienTai = bienThe.getSoLuongDatGiu() != null ? bienThe.getSoLuongDatGiu() : 0;

	        if (tonKhoHienTai < sl) {
	            // Không đủ hàng, có thể xử lý trả về lỗi hoặc bỏ sản phẩm này
	            redirectAttributes.addFlashAttribute("error", "Sản phẩm " + bienThe.getSanPham().getTenSP() + " không đủ hàng.");
	            return "User/home"; // hoặc trang lỗi phù hợp
	        }

	        // Cộng đặt giữ và giảm tồn kho ngay lập tức
	        bienThe.setSoLuongDatGiu(datGiuHienTai + sl);
	        bienThe.setSoLuongTonKho(tonKhoHienTai - sl);

	        bienthesanphamRepository.save(bienThe);
	        bienTheSanPhamService.capNhatTrangThaiKho(bienThe);

	        // Tính tiền
	        BigDecimal tienThucTe = giaThucTe.multiply(BigDecimal.valueOf(sl));
	        BigDecimal tienGoc = donGia.multiply(BigDecimal.valueOf(sl));
	        BigDecimal giamGia = donGia.subtract(giaThucTe).multiply(BigDecimal.valueOf(sl));

	        // Tính tổng cộng
	        tongTienCTT = tongTienCTT.add(tienGoc);
	        tongGiamGia = tongGiamGia.add(giamGia);

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
	    List<Integer> dsMaBienTheDaDat = maBienTheList; // hoặc tự build lại nếu cần
	    donHangService.xoaSanPhamTrongGioSauKhiDatHang(maTK, dsMaBienTheDaDat);
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


	@GetMapping("/Xac-nhan-nhan-hang")
	public String XacNhan(@RequestParam String token,
		    @RequestParam Integer maDH,
		    Model model) {
	    DonHangDTO donHangDTO = donHangService.layDonHangVaChiTietTheoMaDH(maDH);
	    model.addAttribute("donHang", donHangDTO);
		model.addAttribute("content","User/XacnhanDH.html");
	    model.addAttribute("token", token);
		return "User/index";
	}
	


	@PostMapping("/xac-nhan-nhan-hang")
	public String xacNhanNhanHang(@RequestParam("maDH") Integer maDH,
	                              RedirectAttributes redirectAttributes) {


	    try {
	        donHangService.xacNhanNhanHang(maDH);
	        redirectAttributes.addFlashAttribute("success", "✅ Đã xác nhận đơn hàng thành công!");

	        String reviewToken = tokenService.createToken(maDH, 259200L);

	        return "redirect:/danh-gia?token=" + reviewToken + "&maDH=" + maDH;

	    } catch (RuntimeException e) {
	        redirectAttributes.addFlashAttribute("error", "❌ Xác nhận đơn hàng thất bại: " + e.getMessage());

	        String reviewToken = tokenService.createToken(maDH, 259200L);
	        return "redirect:/danh-gia?token=" + reviewToken + "&maDH=" + maDH;
	    }
	}
	
	@PostMapping("/bao-loi")
	public String baoLoi(
	        @RequestParam("maDH") Integer maDH,
	        @RequestParam(value = "maCTDH", required = false) Integer maCTDH,
	        @RequestParam(value = "ghiChu", required = false) String ghiChu,
	        @RequestParam(value = "files", required = false) List<MultipartFile> files,
	        @RequestParam(value = "token", required = false) String token,
	        RedirectAttributes redirectAttributes) {

	    String safeToken = token != null ? token : "";

	    try {
	        // Gọi service lưu báo lỗi
	        baoLoiService.baoLoi(maDH, maCTDH, ghiChu, files);

	        redirectAttributes.addFlashAttribute("success",
	                "🚩 Báo lỗi thành công! Chúng tôi sẽ xử lý sớm nhất.");

	        if (maCTDH == null) {
	            // Báo lỗi toàn đơn → về trang chủ
	            return "redirect:/";
	        }

	        // Báo lỗi 1 sản phẩm → quay lại trang xác nhận đơn hàng với token trước maDH
	        return "redirect:/Xac-nhan-nhan-hang?token=" + URLEncoder.encode(safeToken, StandardCharsets.UTF_8)
	                + "&maDH=" + maDH;

	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("error",
	                "❌ Báo lỗi thất bại: " + e.getMessage());

	        // Quay lại trang xác nhận đơn hàng với token trước maDH
	        return "redirect:/Xac-nhan-nhan-hang?token=" + URLEncoder.encode(safeToken, StandardCharsets.UTF_8)
	                + "&maDH=" + maDH;
	    }
	}





	
	@GetMapping("/danh-gia")
	public String danhGia(@RequestParam String token,
		    @RequestParam Integer maDH, Model model) {
	    DonHangDTO donHangDTO = donHangService.layDonHangVaChiTietTheoMaDH(maDH);
	    model.addAttribute("donHang", donHangDTO);
		model.addAttribute("content","User/DanhGia.html");
		return "User/index";
	}
	@PostMapping("/danh-gia")
	@ResponseBody
	public String DanhGia(
	        @RequestParam List<Integer> productId,
	        @RequestParam(required = false) List<Integer> variantId,
	        @RequestParam(required = false) List<Integer> rating,
	        @RequestParam(required = false) List<String> comment,
	        @RequestParam(name = "files", required = false) List<MultipartFile> files,
	        @AuthenticationPrincipal CustomUserDetails userDetails
	) {
	    try {
	        TaiKhoan taiKhoan = userDetails.getTaiKhoan();
	        int fileIndex = 0;

	        for (int i = 0; i < productId.size(); i++) {
	            DanhGiaSP dg = new DanhGiaSP();
	            dg.setTaiKhoan(taiKhoan);
	            dg.setSanPham(sanPhamRepository.findById(productId.get(i)).orElse(null));

	            if (variantId != null && variantId.size() > i) {
	                dg.setBienTheSanPham(bienthesanphamRepository.findById(variantId.get(i)).orElse(null));
	            }

	            dg.setSoSao(rating.get(i));
	            dg.setBinhLuan(comment != null && comment.size() > i ? comment.get(i) : ""); // bỏ trống nếu null
	            dg.setNgayDang(LocalDateTime.now());

	            danhGiaSPRepository.save(dg);

	            // Lưu file nếu có
	            if (files != null) {
	                while (fileIndex < files.size()) {
	                    MultipartFile f = files.get(fileIndex);
	                    if (f.isEmpty()) { fileIndex++; continue; }

	                    String url = saveFile(f, "review");
	                    DanhGiaMedia media = new DanhGiaMedia();
	                    media.setDanhGiaSP(dg);
	                    media.setUrl(url);
	                    media.setLoai(f.getContentType().startsWith("video") ? "video" : "image");

	                    danhGiaMediaRepository.save(media);
	                    fileIndex++;
	                }
	            }
	        }

	        return "✅ Gửi tất cả đánh giá thành công!";
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "❌ Có lỗi xảy ra khi gửi đánh giá!";
	    }
	}



	private String saveFile(MultipartFile file, String subDir) throws IOException {
	    // Lấy project root
	    String projectPath = new File("").getAbsolutePath();
	    // Tạo thư mục uploads/review tuyệt đối
	    File dir = new File(projectPath + "/uploads/" + subDir);
	    if (!dir.exists()) dir.mkdirs();

	    String originalFilename = file.getOriginalFilename();
	    String extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
	    String randomFileName = UUID.randomUUID().toString() + extension;

	    File dest = new File(dir, randomFileName);
	    file.transferTo(dest);

	    // Trả về URL dùng trong frontend
	    return "/uploads/" + subDir + "/" + randomFileName;
	}



	


	
	@GetMapping("/xac-nhan-da-thanh-toan/{maDH}")
	public ResponseEntity<?> xacNhanDaThanhToan(@PathVariable int maDH) {
	    DonHang dh = donHangService.capNhatTrangThaiThanhToan(maDH);
	    thanhToanService.capNhatNgayThanhToan(maDH);

	    String noiDung = "Đơn hàng: DH" + maDH + " của bạn đã giao. Vui lòng kiểm tra và xác nhận nếu không có vấn đề!";

	    Integer maTK = dh.getTaiKhoan().getMaTK();
	    TaiKhoan taiKhoan = new TaiKhoan();
	    taiKhoan.setMaTK(maTK);

	    ThongBao thongBao = new ThongBao();
	    thongBao.setTaiKhoan(taiKhoan);
	    thongBao.setNoiDung(noiDung);
	    thongBao.setNgayTao(LocalDateTime.now());
	    thongBao.setDaDoc(false);

	    // ✅ Chỉ tạo token ở đây
	    String token = tokenService.createToken(maDH, 259200L);
	    System.out.println(">>> Token tạo: " + token);

	    String url = "/Xac-nhan-nhan-hang?token=" + token + "&maDH=" + maDH;
	    thongBao.setUrl(url);

	    ThongBao daLuu = thongBaoService.taoThongBao(thongBao);

	    // ✅ Truyền token đã tạo xuống WebSocket
	    webSocketNotificationController.guiThongBaoDonHang(
	        maTK, noiDung, daLuu.getMaThongBao(), url, token
	    );

	    System.out.println(">>> Gửi WebSocket tới /topic/user/" + maTK + " với nội dung: " + noiDung);

	    return ResponseEntity.ok("Xác nhận thành công");
	}



	
	
	@ModelAttribute
	public void addCsrfToken(Model model, CsrfToken csrfToken) {
		model.addAttribute("_csrf", csrfToken);
	}

}
