package com.poly.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.entity.AnhSanPham;
import com.poly.repository.AnhSanPhamRepository;

@Service
public class AnhSanPhamService {
	@Autowired
	private AnhSanPhamRepository anhsanphamRepository;
	
    public List<AnhSanPham> getAll() {
        return anhsanphamRepository.findAll();
    }

    public Optional<AnhSanPham> getAnhSanPhamById(int id) {
        return anhsanphamRepository.findById(id);
    }

    public AnhSanPham saveAnhSanPham(AnhSanPham anhsp) {
        return anhsanphamRepository.save(anhsp);
    }

    public AnhSanPham update(Integer id, AnhSanPham anhSanPhamDetails) {
        return anhsanphamRepository.findById(id).map(anhSP -> {
            anhSP.setSanPham(anhSanPhamDetails.getSanPham());
            anhSP.setUrlAnhSP(anhSanPhamDetails.getUrlAnhSP());
            return anhsanphamRepository.save(anhSP);
        }).orElse(null);
    }

    public boolean deleteAnhSanPham(int id) {
        if (anhsanphamRepository.existsById(id)) {
        	anhsanphamRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
