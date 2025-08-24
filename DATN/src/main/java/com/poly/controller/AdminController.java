package com.poly.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poly.dto.DonHang1DTO;
import com.poly.dto.DonHangDTO;
import com.poly.dto.SanPhamDTO;
import com.poly.model.BaoLoi;
import com.poly.model.BienTheSanPham;
import com.poly.model.ChiTietDonHang;
import com.poly.model.DanhGiaSP;
import com.poly.model.DonHang;
import com.poly.model.SanPham;
import com.poly.model.TaiKhoan;
import com.poly.model.ThanhToan;
import com.poly.model.ThongBao;
import com.poly.model.TrangThaiDH;
import com.poly.repository.BaoLoiRepository;
import com.poly.repository.ChiTietDonHangRepository;
import com.poly.repository.DanhGiaSPRepository;
import com.poly.repository.DonHangRepository;
import com.poly.repository.SanPhamRepository;
import com.poly.repository.TaiKhoanRepository;
import com.poly.repository.ThanhToanRepository;
import com.poly.repository.TrangThaiDHRepository;
import com.poly.service.BienTheSanPhamService;
import com.poly.service.SanPhamService;
import com.poly.service.TaiKhoanService;
import com.poly.service.ThongBaoService;

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
	@Autowired
	private BienTheSanPhamService bienTheSanPhamService;
	@Autowired 
	private ThongBaoService thongBaoService;
    @Autowired
    private WebSocketNotificationController webSocketNotificationController;
    @Autowired
    private SanPhamRepository sanPhamRepository;
    @Autowired
    private BaoLoiRepository baoLoiRepository;
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
        long baoLoi = dsDonHang.stream().filter(dh -> "Báo lỗi".equals(dh.getTrangThaiDH().getTenTTDH())).count();
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
        model.addAttribute("countBaoLoi", baoLoi);
        	
        model.addAttribute("content", "Admin_Staff/QuanLyDonHang.html");
        return "Admin_Staff/curd";
    }

    @Transactional
    @PostMapping("/don-hang/{id}/update-status")
    @ResponseBody
    public Map<String, String> updateTrangThai(@PathVariable("id") Integer id,
                                               @RequestParam(value = "yeuCauHuy", required = false) Boolean yeuCauHuy,
                                               @RequestParam(value = "tuChoiHuy", required = false) Boolean tuChoiHuy,
                                               @RequestParam(value = "baoLoi", required = false) Boolean baoLoi,
                                               @RequestParam(value = "lyDoLoi", required = false) String lyDoLoi) {
        DonHang dh = donHangRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        int currentStatusId = dh.getTrangThaiDH().getMaTTDH();
        int newStatusId = currentStatusId;
        String newStatus = dh.getTrangThaiDH().getTenTTDH();

        // Nếu báo lỗi không giao được
        if (Boolean.TRUE.equals(baoLoi)) {
            newStatusId = 9; // 9 = Lỗi/Không giao được
            if (lyDoLoi != null && !lyDoLoi.isBlank()) {
                dh.setGhiChu("Báo lỗi: " + lyDoLoi);

                List<ChiTietDonHang> chiTietList = chiTietDonHangRepository.findByDonHang(dh);
                for (ChiTietDonHang ct : chiTietList) {
                    BienTheSanPham bienThe = ct.getBienTheSanPham();
                    if (bienThe == null) continue;

                    int soLuongTonKho = bienThe.getSoLuongTonKho() != null ? bienThe.getSoLuongTonKho() : 0;
                    int soLuongDatGiu = bienThe.getSoLuongDatGiu() != null ? bienThe.getSoLuongDatGiu() : 0;

                    bienThe.setSoLuongTonKho(soLuongTonKho + ct.getSoLuongSP());
                    bienThe.setSoLuongDatGiu(Math.max(soLuongDatGiu - ct.getSoLuongSP(), 0));

                    bienTheSanPhamService.capNhatTrangThaiKho(bienThe);
                    String noiDung = "Đơn hàng DH" + dh.getMaDH() + " không thể giao vì: " + lyDoLoi +"\n Mong bạn thông cảm cho shop";
                    guiThongBao(dh, noiDung, "");
                }

            }
        }
        // Nếu từ chối yêu cầu hủy
        else if (Boolean.TRUE.equals(tuChoiHuy) && currentStatusId == 8) {
            TrangThaiDH trangThaiTruoc = dh.getTrangThaiTruoc();
            if (trangThaiTruoc != null) {
                dh.setTrangThaiDH(trangThaiTruoc);
                dh.setTrangThaiTruoc(null); // clear để tránh rác
                newStatusId = trangThaiTruoc.getMaTTDH();
                newStatus = trangThaiTruoc.getTenTTDH();
                String noiDung = "Yêu cầu hủy đơn hàng DH" + dh.getMaDH() + " của bạn đã bị từ chối.";
                guiThongBao(dh, noiDung,"");
            }
        }
        // Nếu khách gửi yêu cầu hủy
        else if (Boolean.TRUE.equals(yeuCauHuy)) {
            dh.setTrangThaiTruoc(dh.getTrangThaiDH()); // lưu trạng thái trước
            newStatusId = 8; // yêu cầu hủy
        }
        // Xử lý chuyển trạng thái bình thường
        else {
            switch (currentStatusId) {
                case 1: newStatusId = 2; break;
                case 2: newStatusId = 3; break;
                case 3: newStatusId = 4; break;
                case 8: newStatusId = 5; break; // admin xác nhận hủy
            }
        }

        // Nếu xác nhận hủy thật sự → cộng tồn kho
        if (currentStatusId == 8 && newStatusId == 5) {
            List<ChiTietDonHang> chiTietList = chiTietDonHangRepository.findByDonHang(dh);
            for (ChiTietDonHang ct : chiTietList) {
                BienTheSanPham bienThe = ct.getBienTheSanPham();
                if (bienThe == null) continue;

                int soLuongTonKho = bienThe.getSoLuongTonKho() != null ? bienThe.getSoLuongTonKho() : 0;
                int soLuongDatGiu = bienThe.getSoLuongDatGiu() != null ? bienThe.getSoLuongDatGiu() : 0;

                bienThe.setSoLuongTonKho(soLuongTonKho + ct.getSoLuongSP());
                bienThe.setSoLuongDatGiu(Math.max(soLuongDatGiu - ct.getSoLuongSP(), 0));

                bienTheSanPhamService.capNhatTrangThaiKho(bienThe);
                String noiDung = "Đơn hàng DH" + dh.getMaDH() + " đã được hủy thành công.";
                String url = "/DonHang?trangThai=5";
                guiThongBao(dh, noiDung, url);
            }
        }

        // Cập nhật trạng thái nếu có thay đổi
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
    private void guiThongBao(DonHang dh, String noiDung, String url) {
        Integer maTK = dh.getTaiKhoan().getMaTK();
        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setMaTK(maTK);

        ThongBao thongBao = new ThongBao();
        thongBao.setTaiKhoan(taiKhoan);
        thongBao.setNoiDung(noiDung);
        thongBao.setNgayTao(LocalDateTime.now());
        thongBao.setDaDoc(false);

        thongBao.setUrl(url);

        ThongBao daLuu = thongBaoService.taoThongBao(thongBao);

        webSocketNotificationController.guiThongBaoDonHang(
            maTK, noiDung, daLuu.getMaThongBao(), url, ""
        );
    }


    @GetMapping("/don-hang/latest")
    @ResponseBody
    @Transactional
    public List<DonHang1DTO> getLatestOrders(@RequestParam("afterId") Integer afterId) {
        List<DonHang> newOrders = donHangRepository.findByMaDHGreaterThanOrderByNgayDatDesc(afterId);

        return newOrders.stream()
                .map(dh -> {
                    List<ChiTietDonHang> chiTiet = chiTietDonHangRepository.findByDonHang(dh);
                    return new DonHang1DTO(dh, chiTiet);
                })
                .toList();
    }


    @GetMapping("/don-hang/{maDH}/chi-tiet")
    public ResponseEntity<?> getChiTietDonHang(@PathVariable int maDH) {
        DonHang dh = donHangRepository.findById(maDH).orElseThrow();
        List<ChiTietDonHang> chiTiet = chiTietDonHangRepository.findByDonHang(dh);

        return ResponseEntity.ok(chiTiet.stream().map(ct -> Map.of(
        	"maSP", ct.getBienTheSanPham().getSanPham().getMaSP(),	
            "tenSP", ct.getBienTheSanPham().getSanPham().getTenSP(),
            "hinhAnh", ct.getBienTheSanPham().getAnhChiTietList().get(0).getUrlAnhCT(),
            "soLuong", ct.getSoLuongSP(),
            "donGia", ct.getDonGia(),
            "thanhTien", ct.getThanhTien(),
            "daBaoLoi", ct.getDaBaoLoi() != null ? ct.getDaBaoLoi() : false
        )));
    }
    @GetMapping("/don-hang/{maDH}/bao-loi")
    @ResponseBody
    @Transactional
    public ResponseEntity<?> getBaoLoiByOrder(@PathVariable int maDH) {
        List<BaoLoi> list = baoLoiRepository.findByDonHang_MaDH(maDH);
        if (list == null || list.isEmpty()) {
            // trả mảng rỗng để frontend dễ xử lý
            return ResponseEntity.ok(Collections.emptyList());
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        List<Map<String, Object>> out = list.stream().map(b -> {
            Map<String, Object> m = new HashMap<>();
            m.put("maBaoLoi", b.getMaBaoLoi());
            m.put("maCTDH", b.getChiTietDonHang() != null ? b.getChiTietDonHang().getMaCTDH() : null);
            m.put("maTK", b.getTaiKhoan() != null ? b.getTaiKhoan().getMaTK() : null);
            m.put("ghiChu", b.getGhiChu());
            m.put("trangThai", b.getTrangThai());
            m.put("ngayBao", b.getNgayBao() != null ? b.getNgayBao().format(fmt) : null);

            // mediaList -> list of maps { url, loai }
            List<Map<String,String>> medias = b.getMediaList() == null ? Collections.emptyList()
                : b.getMediaList().stream()
                    .map(mm -> {
                        Map<String,String> mmmap = new HashMap<>();
                        mmmap.put("url", mm.getUrl());
                        mmmap.put("loai", mm.getLoai());
                        return mmmap;
                    }).collect(Collectors.toList());

            m.put("media", medias);
            return m;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(out);
    }
    @PostMapping("/bao-loi/{maBaoLoi}/cap-nhat-trang-thai")
    @ResponseBody
    @Transactional
    public ResponseEntity<?> updateTrangThaiBaoLoi(@PathVariable int maBaoLoi,
                                                   @RequestParam int trangThai) {
        Optional<BaoLoi> optBaoLoi = baoLoiRepository.findById(maBaoLoi);
        if (optBaoLoi.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy báo lỗi");
        }

        BaoLoi baoLoi = optBaoLoi.get();
        baoLoi.setTrangThai(trangThai); // 1 = Xác nhận, 2 = Từ chối
        baoLoiRepository.save(baoLoi);

        // Lấy thông tin đơn hàng và tài khoản
        DonHang dh = baoLoi.getDonHang();
        if (dh != null && dh.getTaiKhoan() != null) {
            String noiDung;

            if (trangThai == 1) { // Đã xác nhận báo lỗi
                noiDung = "Báo lỗi của bạn cho đơn hàng DH" + dh.getMaDH() + " đã được xác nhận. Chúng tôi sẽ xử lý sớm nhất.";
            } else if (trangThai == 2) { // Đã hủy báo lỗi
                noiDung = "Báo lỗi của bạn cho đơn hàng DH" + dh.getMaDH() + " đã bị từ chối.";
            } else {
                noiDung = "Báo lỗi của bạn cho đơn hàng DH" + dh.getMaDH() + " đã được cập nhật.";
            }

            guiThongBao(dh, noiDung, "");
        }

        return ResponseEntity.ok(Map.of(
            "status", "success",
            "message", "Cập nhật trạng thái thành công",
            "maBaoLoi", maBaoLoi,
            "trangThai", trangThai
        ));
    }

    @GetMapping("/QuanLyGiamGia")
    public String quanLyGiamGia(Model model) {
    	model.addAttribute("content", "Admin_Staff/QuanLyGiamGia.html");
    	
        return "Admin_Staff/curd";
    }
    







}
