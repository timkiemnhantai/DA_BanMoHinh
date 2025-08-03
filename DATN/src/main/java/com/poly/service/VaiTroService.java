package com.poly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.model.VaiTro;
import com.poly.repository.VaiTroRepository;

@Service
public class VaiTroService {
	@Autowired
	private VaiTroRepository vaitroRepository;
	
    public VaiTro findByTenVaiTro(String tenVaiTro) {
        return vaitroRepository.findByTenVaiTro(tenVaiTro);
    }
}
