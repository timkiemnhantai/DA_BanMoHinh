package com.poly.service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.repository.BienTheSanPhamRepository;
import com.poly.repository.ChiTietDonHangRepository;
import com.poly.repository.DonHangRepository;
import com.poly.repository.ThanhToanRepository;
import com.poly.repository.TrangThaiDHRepository;


import com.poly.dto.DonHangDTO;
import com.poly.entity.BienTheSanPham;
import com.poly.entity.ChiTietDonHang;
import com.poly.entity.DonHang;
import com.poly.entity.ThanhToan;
import com.poly.entity.TrangThaiDH;

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
	
//	public void capNhatTrangThaiDonHang(int maDH, int trangThaiMoi, String trangThaiThanhToan) {
//	    Optional<DonHang> optionalDonHang = donHangRepo.findById(maDH);
//	    if (optionalDonHang.isPresent()) {
//	        DonHang donHang = optionalDonHang.get();
//	        ThanhToan thanhToan = thanhToanRepo.findByDonHang_MaDH(maDH);
//	        if ("Đã thanh toán".equalsIgnoreCase(trangThaiThanhToan)
//	                && (thanhToan == null || !"Đã thanh toán".equalsIgnoreCase(thanhToan.getTrangThai()))){
//
//	            // Nếu trạng thái đơn hàng chưa phải 4 (đã thanh toán), mới thực hiện trừ tồn kho
//	            if (donHang.getTrangThaiDH().getMaTTDH() != 4 && trangThaiMoi == 4) {
//	                List<ChiTietDonHang> chiTietList = chiTietDonHangRepo.findByDonHang(donHang);
//
//	                for (ChiTietDonHang chiTiet : chiTietList) {
//	                    BienTheSanPham bienThe = chiTiet.getBienTheSanPham();
//
//	                    int tonKhoTruoc = bienThe.getSoLuongTonKho();
//	                    int soLuongMua = chiTiet.getSoLuongSP();
//
//	                    bienThe.setSoLuongTonKho(tonKhoTruoc - soLuongMua);
//
//	                    System.out.println(">> Trừ tồn kho cho mã biến thể: " + bienThe.getMaCTSP() +
//	                            " | Từ " + tonKhoTruoc + " → " + bienThe.getSoLuongTonKho() +
//	                            " | Số lượng mua: " + soLuongMua);
//
//	                    bienTheSanPhamRepo.save(bienThe);
//	                }
//	            }
//	        }
//
//	        // Cập nhật trạng thái đơn hàng dù có trừ kho hay không
//	        TrangThaiDH trangThai = trangThaiDHRepo.findById(trangThaiMoi)
//	                .orElseThrow(() -> new RuntimeException("Không tìm thấy trạng thái"));
//
//	        donHang.setTrangThaiDH(trangThai);
//	        donHangRepo.save(donHang);
//	    } else {
//	        throw new RuntimeException("Không tìm thấy đơn hàng có mã: " + maDH);
//	    }
//	}





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

	
	
	public void xacNhanNhanHang(int maDH) {
	    Optional<DonHang> optionalDonHang = donHangRepo.findById(maDH);
	    if (optionalDonHang.isPresent()) {
	        DonHang donHang = optionalDonHang.get();
	        ThanhToan thanhToan = thanhToanRepo.findByDonHang_MaDH(maDH);

	        // Chỉ khi trạng thái thanh toán là "Đã thanh toán", mới cho cập nhật trạng thái đơn hàng
	        if (thanhToan != null && "Đã thanh toán".equalsIgnoreCase(thanhToan.getTrangThai())) {
	            // Tránh trừ kho nhiều lần
	            if (donHang.getTrangThaiDH().getMaTTDH() != 7) {
	                List<ChiTietDonHang> chiTietList = chiTietDonHangRepo.findByDonHang(donHang);
	                for (ChiTietDonHang chiTiet : chiTietList) {
	                    BienTheSanPham bienThe = chiTiet.getBienTheSanPham();
	                    bienThe.setSoLuongTonKho(bienThe.getSoLuongTonKho() - chiTiet.getSoLuongSP());
	                    bienTheSanPhamRepo.save(bienThe);
	                }
	            }

	            // Cập nhật trạng thái đơn hàng
	            TrangThaiDH trangThai = trangThaiDHRepo.findById(6) // 4: Đã thanh toán
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






}
