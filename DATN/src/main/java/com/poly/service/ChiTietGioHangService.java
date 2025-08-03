package com.poly.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poly.dto.GioHangDTO;
import com.poly.model.BienTheSanPham;
import com.poly.model.BienThe_GiamGiaSP;
import com.poly.model.ChiTietGioHang;
import com.poly.model.GiamGiaSP;
import com.poly.repository.ChiTietGioHangRepository;
@Service

public class ChiTietGioHangService {
	@Autowired
	private ChiTietGioHangRepository chitietgiohangRepository;
	
	@Transactional(readOnly = true)
	public List<GioHangDTO> layDanhSachGioHangDTO(Integer maTaiKhoan) {
		List<ChiTietGioHang> dsChiTiet = chitietgiohangRepository.findByMaTKOrderByNgayThemDesc(maTaiKhoan);

	    List<GioHangDTO> list = new ArrayList<>();

	    for (ChiTietGioHang ctgh : dsChiTiet) {
	        GioHangDTO dto = new GioHangDTO();
	        BienTheSanPham bienThe = ctgh.getChiTietSanPham();

	        dto.setChiTietGioHang(ctgh);
	        dto.setBienThe(bienThe);

	        // Tính giảm giá
	        Integer phanTramGiam = null;
	        Long giaGiam = null;
	        BigDecimal giaSauGiam = bienThe.getGia();

	        if (bienThe.getGiamGiaSPList() != null) {
	            GiamGiaSP giamGiaSP = bienThe.getGiamGiaSPList().stream()
	                .map(BienThe_GiamGiaSP::getGiamGiaSP)
	                .filter(gg -> "Đang hoạt động".equalsIgnoreCase(gg.getTrangThai()) &&
	                        gg.getThoiGianBatDau().isBefore(LocalDateTime.now()) &&
	                        gg.getThoiGianKetThuc().isAfter(LocalDateTime.now()))
	                .findFirst().orElse(null);

	            if (giamGiaSP != null) {
	                if (giamGiaSP.getPhanTramGiam() != null) {
	                    phanTramGiam = giamGiaSP.getPhanTramGiam();
	                    giaSauGiam = bienThe.getGia().multiply(BigDecimal.valueOf(1 - phanTramGiam / 100.0));
	                } else if (giamGiaSP.getGiaGiam() != null) {
	                    giaGiam = giamGiaSP.getGiaGiam();
	                    giaSauGiam = bienThe.getGia().subtract(BigDecimal.valueOf(giaGiam));
	                }
	            }
	        }

	        dto.setPhanTramGiam(phanTramGiam);
	        dto.setGiaGiam(giaGiam);
	        dto.setGiaSauGiam(giaSauGiam);

	        list.add(dto);
	    }

	    return list;
	}
	@Transactional
	public boolean capNhatSoLuong(Integer maCTGH, Integer soLuongMoi) {
	    Optional<ChiTietGioHang> optional = chitietgiohangRepository.findById(maCTGH);
	    if (optional.isEmpty()) return false;

	    ChiTietGioHang ctgh = optional.get();

	    BigDecimal giamGiaThucTe = ctgh.getGiamGiaThucTe() == null ? BigDecimal.ZERO : ctgh.getGiamGiaThucTe();
	    boolean laGiaGoc = giamGiaThucTe.compareTo(BigDecimal.ZERO) == 0;

	    // Kiểm tra sản phẩm hiện tại có đang giảm giá không
	    boolean hienDangGiamGia = ctgh.getChiTietSanPham().getGiamGiaSPList().stream()
	        .map(BienThe_GiamGiaSP::getGiamGiaSP)
	        .anyMatch(gg ->
	            "Đang hoạt động".equalsIgnoreCase(gg.getTrangThai()) &&
	            gg.getThoiGianBatDau().isBefore(LocalDateTime.now()) &&
	            gg.getThoiGianKetThuc().isAfter(LocalDateTime.now())
	        );

	    // ❌ Nếu là giá gốc nhưng hiện tại đang giảm giá → không cho tăng số lượng
	    if (laGiaGoc && hienDangGiamGia) {
	        return false;
	    }

	    // ✅ Nếu không vi phạm thì cập nhật
	    ctgh.setSoLuong(soLuongMoi);
	    chitietgiohangRepository.save(ctgh);
	    return true;
	}
	@Transactional(readOnly = true)
	public List<GioHangDTO> layGioHangDTOTheoDanhSachCTGH(Integer maTK, List<Integer> maCTGHList) {
	    return layDanhSachGioHangDTO(maTK).stream()
	        .filter(dto -> maCTGHList.contains(dto.getChiTietGioHang().getMaCTGH()))
	        .collect(Collectors.toList());
	}
	public void xoaNhieuTheoMa(List<Integer> dsMaCTGH) {
	    for (Integer maCTGH : dsMaCTGH) {
	        chitietgiohangRepository.deleteById(maCTGH);
	    }
	}



}
