package com.poly.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.entity.DiaChi;
import com.poly.entity.TaiKhoan;
import com.poly.repository.DiaChiRepository;
import com.poly.repository.TaiKhoanRepository;

@Service
public class DiaChiService {
	@Autowired
	private TaiKhoanRepository taiKhoanRepo;
	@Autowired
	private DiaChiRepository diaChiRepository;
	public DiaChi layDiaChiMacDinh(Integer maTK) {
	    TaiKhoan taiKhoan = taiKhoanRepo.findById(maTK).orElseThrow();
	    return taiKhoan.getDiaChiMacDinh();
	} 
	public List<DiaChi> findByMaTaiKhoan(Integer maTK) {
	    return diaChiRepository.findByTaiKhoan_MaTK(maTK);
	}
}
