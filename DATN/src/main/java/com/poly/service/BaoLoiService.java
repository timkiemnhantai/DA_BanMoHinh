package com.poly.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.poly.model.BaoLoi;
import com.poly.model.BaoLoiMedia;
import com.poly.model.ChiTietDonHang;
import com.poly.model.DonHang;
import com.poly.model.TaiKhoan;
import com.poly.model.TrangThaiDH;
import com.poly.repository.BaoLoiRepository;
import com.poly.repository.ChiTietDonHangRepository;
import com.poly.repository.DonHangRepository;
import com.poly.repository.TrangThaiDHRepository;

import jakarta.transaction.Transactional;
@Service
public class BaoLoiService {
	@Autowired
	private DonHangRepository donHangRepository;
//	@Autowired
//	private BienTheSanPhamRepository bienTheSanPhamRepository;
	@Autowired
	private ChiTietDonHangRepository chiTietDonHangRepository;
	@Autowired
	private BaoLoiRepository baoLoiRepository;
	@Autowired 
	private TrangThaiDHRepository trangThaiDHRepository;
	@Transactional
	public void baoLoi(Integer maDH, Integer maCTDH, String ghiChu, List<MultipartFile> files) throws IOException {
	    DonHang donHang = donHangRepository.findById(maDH)
	            .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

	    ChiTietDonHang chiTietDonHang = null;

	    if (maCTDH != null) {
	        // Báo lỗi 1 sản phẩm
	        chiTietDonHang = chiTietDonHangRepository.findById(maCTDH)
	                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết đơn hàng"));

	        chiTietDonHang.setDaBaoLoi(true);
	        chiTietDonHangRepository.save(chiTietDonHang);
	    } else {
	        // Báo lỗi TOÀN BỘ đơn => đánh dấu tất cả chi tiết đơn là đã báo lỗi
	        List<ChiTietDonHang> chiTietList = chiTietDonHangRepository.findByDonHang(donHang);
	        for (ChiTietDonHang ct : chiTietList) {
	            ct.setDaBaoLoi(true);
	            // Ghi chung lý do nếu có
	            if (ghiChu != null && !ghiChu.trim().isEmpty()) ;
	            chiTietDonHangRepository.save(ct);
	        }
	    }

	    TaiKhoan taiKhoan = donHang.getTaiKhoan();

	    BaoLoi baoLoi = new BaoLoi();
	    baoLoi.setDonHang(donHang);
	    baoLoi.setChiTietDonHang(chiTietDonHang);
	    baoLoi.setTaiKhoan(taiKhoan);
	    baoLoi.setGhiChu(ghiChu);
	    baoLoi.setTrangThai(0); // 0 = mới báo lỗi
	    baoLoi.setNgayBao(LocalDateTime.now());

	    // đảm bảo danh sách media không null
	    if (baoLoi.getMediaList() == null) {
	        baoLoi.setMediaList(new java.util.ArrayList<>());
	    }

	    if (files != null) {
	        for (MultipartFile file : files) {
	            if (file.isEmpty()) continue;

	            String url = saveFile(file, "baoloi");

	            BaoLoiMedia media = new BaoLoiMedia();
	            media.setBaoLoi(baoLoi);
	            media.setUrl(url);
	            media.setLoai(file.getContentType() != null && file.getContentType().startsWith("video")
	                          ? "video" : "image");

	            baoLoi.getMediaList().add(media);
	        }
	    }

	    // set trạng thái đơn là "Báo lỗi" (id = 10 theo hệ thống của bạn)
	    TrangThaiDH trangThai = trangThaiDHRepository.findById(10).orElse(null);
	    if (trangThai != null) {
	        donHang.setTrangThaiDH(trangThai);
	    }
//	    // cập nhật ghi chú đơn
//	    donHang.setGhiChu(ghiChu);
	    donHangRepository.save(donHang);

	    // Lưu báo lỗi cùng media (nếu mapping cascade đúng thì sẽ lưu media tự động)
	    baoLoiRepository.save(baoLoi);
	}


	// ====== Hàm lưu file ======
	private String saveFile(MultipartFile file, String subDir) throws IOException {
	    // Lấy project root (nơi chứa target/, src/)
	    String projectPath = new File("").getAbsolutePath();

	    // Tạo thư mục uploads/baoloi tuyệt đối
	    File dir = new File(projectPath + "/uploads/" + subDir);
	    if (!dir.exists()) dir.mkdirs();

	    // Đặt tên file random để tránh trùng
	    String originalFilename = file.getOriginalFilename();
	    String extension = "";
	    if (originalFilename != null && originalFilename.contains(".")) {
	        extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
	    }
	    String randomFileName = UUID.randomUUID().toString() + extension;

	    File dest = new File(dir, randomFileName);
	    file.transferTo(dest);

	    // Trả về URL dùng trong frontend (mapping tĩnh /uploads/** trong Spring)
	    return "/uploads/" + subDir + "/" + randomFileName;
	}



}
