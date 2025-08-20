package com.poly.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.model.ThanhToan;
import com.poly.repository.ThanhToanRepository;

import jakarta.transaction.Transactional;

@Service
public class ThanhToanService {
	@Autowired
	private ThanhToanRepository thanhToanRepository;
	 @Transactional
	    public ThanhToan capNhatNgayThanhToan(Integer maDH) {
	        ThanhToan tt = thanhToanRepository.findByDonHang_MaDH(maDH)
	                .orElseThrow(() -> new RuntimeException("Không tìm thấy ThanhToan cho đơn hàng " + maDH));

	        tt.setNgayThanhToan(LocalDateTime.now());
	        tt.setTrangThai("Đã thanh toán"); // Có thể cập nhật trạng thái luôn
	        return thanhToanRepository.save(tt);
	    }
}
