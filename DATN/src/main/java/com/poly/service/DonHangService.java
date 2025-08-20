package com.poly.service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poly.repository.BienTheSanPhamRepository;
import com.poly.repository.ChiTietDonHangRepository;
import com.poly.repository.ChiTietGioHangRepository;
import com.poly.repository.DonHangRepository;
import com.poly.repository.ThanhToanRepository;
import com.poly.repository.TrangThaiDHRepository;


import com.poly.dto.DonHangDTO;
import com.poly.dto.DonHangTrangThaiDTO;
import com.poly.model.BienTheSanPham;
import com.poly.model.ChiTietDonHang;
import com.poly.model.DonHang;
import com.poly.model.ThanhToan;
import com.poly.model.TrangThaiDH;

@Service
public class DonHangService {
    @Autowired
    private DonHangRepository donHangRepo;

    @Autowired
    private ChiTietDonHangRepository chiTietDonHangRepo;
    @Autowired
    private TrangThaiDHRepository trangThaiDHRepo;
    @Autowired
    private BienTheSanPhamRepository bienTheSanPhamRepo;    
    @Autowired
    private ThanhToanRepository thanhToanRepo;  
    
	public List<DonHangDTO> layDonHangVaChiTietTheoMaTK(int maTK) {
        List<DonHang> ds = donHangRepo.findByTaiKhoan_MaTKOrderByNgayDatDesc(maTK);
        List<DonHangDTO> dsDTO = new ArrayList<>();

        for (DonHang dh : ds) {
            List<ChiTietDonHang> chiTiet = chiTietDonHangRepo.findByDonHang_MaDH(dh.getMaDH());
            DonHangDTO dto = new DonHangDTO();
            dto.setDonHang(dh);
            dto.setChiTietDonHangs(chiTiet);
            dsDTO.add(dto);
        }

        return dsDTO;
    }
	
    @Transactional(readOnly = true)
    public List<DonHangTrangThaiDTO> getDonHangTheoTrangThai(Integer maTTDH) {
        List<DonHang> donHangs = donHangRepo.findByTrangThaiDH_MaTTDH(maTTDH);
        return donHangs.stream().map(dh -> {
            DonHangTrangThaiDTO dto = new DonHangTrangThaiDTO();
            dto.setMaDH(dh.getMaDH());
            dto.setTenNguoiDung(dh.getTaiKhoan().getHoTen());
            dto.setTenTrangThai(dh.getTrangThaiDH().getTenTTDH());
            dto.setNgayDat(dh.getNgayDat());
            dto.setThanhTien(dh.getThanhTien());
            return dto;
        }).toList();
    }
    
    public List<DonHangDTO> layDonHangVaChiTietTheoMaTKVaTrangThai(int maTK, int maTTDH) {
        List<DonHang> ds = donHangRepo.findByTaiKhoan_MaTKAndTrangThaiDH_MaTTDHOrderByNgayDatDesc(maTK, maTTDH);
        List<DonHangDTO> dsDTO = new ArrayList<>();

        for (DonHang dh : ds) {
            List<ChiTietDonHang> chiTiet = chiTietDonHangRepo.findByDonHang_MaDH(dh.getMaDH());
            DonHangDTO dto = new DonHangDTO();
            dto.setDonHang(dh);
            dto.setChiTietDonHangs(chiTiet);
            dsDTO.add(dto);
        }

        return dsDTO;
    }




	public DonHang capNhatTrangThaiThanhToan(int maDH) {
	    ThanhToan thanhToan = thanhToanRepo.findByDonHang_MaDH(maDH);
	    if (thanhToan != null) {
	        thanhToan.setTrangThai("Đã thanh toán");
	        thanhToanRepo.save(thanhToan);
	        
	        DonHang donHang = thanhToan.getDonHang(); 
	        TrangThaiDH trangThai = trangThaiDHRepo.findById(4)
	            .orElseThrow(() -> new RuntimeException("Không tìm thấy trạng thái đơn hàng 'Đã giao'"));

	        donHang.setTrangThaiDH(trangThai);
	        donHangRepo.save(donHang);

	        return donHang; // Trả về để controller sử dụng
	    } else {
	        throw new RuntimeException("Không tìm thấy thông tin thanh toán của đơn hàng mã: " + maDH);
	    }
	}
	public DonHang capNhatTrangThaiDaHuy(int maDH) {
	    ThanhToan thanhToan = thanhToanRepo.findByDonHang_MaDH(maDH);
	    if (thanhToan != null) {
	        // Cập nhật trạng thái thanh toán nếu cần (có thể thay đổi hoặc bỏ dòng này tùy yêu cầu)
	        thanhToan.setTrangThai("Đã hủy");
	        thanhToanRepo.save(thanhToan);

	        DonHang donHang = thanhToan.getDonHang();
	        TrangThaiDH trangThai = trangThaiDHRepo.findById(5)
	            .orElseThrow(() -> new RuntimeException("Không tìm thấy trạng thái đơn hàng 'Đã hủy'"));

	        donHang.setTrangThaiDH(trangThai);
	        donHangRepo.save(donHang);

	        return donHang; // Trả về để controller sử dụng
	    } else {
	        throw new RuntimeException("Không tìm thấy thông tin thanh toán của đơn hàng mã: " + maDH);
	    }
	}

	
	
	public void xacNhanNhanHang(int maDH) {
	    Optional<DonHang> optionalDonHang = donHangRepo.findById(maDH);
	    if (optionalDonHang.isPresent()) {
	        DonHang donHang = optionalDonHang.get();
	        ThanhToan thanhToan = thanhToanRepo.findByDonHang_MaDH(maDH);

	        if (thanhToan != null && "Đã thanh toán".equalsIgnoreCase(thanhToan.getTrangThai())) {
	            if (donHang.getTrangThaiDH().getMaTTDH() != 6) { // 6 = Giao hàng thành công
	                List<ChiTietDonHang> chiTietList = chiTietDonHangRepo.findByDonHang(donHang);
	                for (ChiTietDonHang chiTiet : chiTietList) {
	                    BienTheSanPham bienThe = chiTiet.getBienTheSanPham();

	                    int slGiao = chiTiet.getSoLuongSP();

	                    int slDatGiu = bienThe.getSoLuongDatGiu() != null ? bienThe.getSoLuongDatGiu() : 0;
	                    int soLuongDatGiuMoi = slDatGiu - slGiao;
	                    if (soLuongDatGiuMoi < 0) soLuongDatGiuMoi = 0;
	                    bienThe.setSoLuongDatGiu(soLuongDatGiuMoi);

	                    bienTheSanPhamRepo.save(bienThe);
	                }
	            }

	            // Cập nhật trạng thái đơn hàng sang "Giao hàng thành công" (6)
	            TrangThaiDH trangThai = trangThaiDHRepo.findById(6)
	                    .orElseThrow(() -> new RuntimeException("Không tìm thấy trạng thái"));
	            donHang.setTrangThaiDH(trangThai);
	            donHangRepo.save(donHang);
	        } else {
	            throw new RuntimeException("Chưa xác nhận thanh toán cho đơn hàng này.");
	        }
	    } else {
	        throw new RuntimeException("Không tìm thấy đơn hàng mã: " + maDH);
	    }
	}


    @Autowired
    private ChiTietGioHangRepository chiTietGioHangRepository;


    @Transactional
    public void xoaSanPhamTrongGioSauKhiDatHang(Integer maTK, List<Integer> dsMaBienTheDaDat) {
        chiTietGioHangRepository.deleteByGioHang_TaiKhoan_MaTKAndChiTietSanPham_MaCTSPIn(maTK, dsMaBienTheDaDat);
    }

    public DonHangDTO layDonHangVaChiTietTheoMaDH(int maDH) {
        Optional<DonHang> optionalDonHang = donHangRepo.findById(maDH);
        if (optionalDonHang.isEmpty()) {
            return null; // hoặc ném exception tùy ý
        }
        DonHang donHang = optionalDonHang.get();
        List<ChiTietDonHang> chiTiet = chiTietDonHangRepo.findByDonHang_MaDH(maDH);

        DonHangDTO dto = new DonHangDTO();
        dto.setDonHang(donHang);
        dto.setChiTietDonHangs(chiTiet);
        return dto;
    }









}
