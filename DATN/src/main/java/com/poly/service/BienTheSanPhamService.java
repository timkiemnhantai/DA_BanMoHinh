package com.poly.service;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.model.BienTheSanPham;
import com.poly.repository.BienTheSanPhamRepository;



@Service
public class BienTheSanPhamService {
	@Autowired
	private BienTheSanPhamRepository bienthesanphamRepository;
	

	

    public BienTheSanPham findById(Integer id) {
        Optional<BienTheSanPham> optional = bienthesanphamRepository.findById(id);
        return optional.orElse(null); 
    }
}
