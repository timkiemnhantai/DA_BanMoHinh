package com.poly.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.entity.LoaiSanPham;
import com.poly.repository.LoaiSanPhamRepository;
@Service
public class LoaiSanPhamService {
	@Autowired
	private LoaiSanPhamRepository loaisanphamRepository;
	
    public List<LoaiSanPham> getAll() {
        return loaisanphamRepository.findAll();
    }
}
