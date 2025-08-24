package com.poly.service;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.model.BienTheSanPham;
import com.poly.model.TrangThaiKH;
import com.poly.repository.BienTheSanPhamRepository;
import com.poly.repository.TrangThaiKHRepository;

import jakarta.transaction.Transactional;



@Service
public class BienTheSanPhamService {
	@Autowired
	private BienTheSanPhamRepository bienthesanphamRepository;
	@Autowired 
	private TrangThaiKHRepository trangThaiKHRepository;
	
	@Transactional
	public void capNhatTrangThaiKho(BienTheSanPham bienThe) {
	    int tonThucTe = bienThe.getSoLuongTonKho();
	    int maTrangThai = tonThucTe > 0 ? 1 : 2;

	    TrangThaiKH trangThai = trangThaiKHRepository.findById(maTrangThai)
	        .orElseThrow(() -> new RuntimeException("Không tìm thấy trạng thái"));

	    bienThe.setTrangThaiKH(trangThai);
	    bienthesanphamRepository.save(bienThe);
	}



	

    public BienTheSanPham findById(Integer id) {
        Optional<BienTheSanPham> optional = bienthesanphamRepository.findById(id);
        return optional.orElse(null); 
    }
}
