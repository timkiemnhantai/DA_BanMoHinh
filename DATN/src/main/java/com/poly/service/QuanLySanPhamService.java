package com.poly.service;

import com.poly.model.SanPham;
import com.poly.repository.QuanLySanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class QuanLySanPhamService {

    @Autowired
    private QuanLySanPhamRepository repo;

    public List<SanPham> getAllSanPham() {
        return repo.findAll();
    }

    public List<SanPham> searchSanPham(String keyword, Integer maLoai) {
        if ((keyword == null || keyword.isEmpty()) && (maLoai == null)) {
            return repo.findAll();
        }
        if (keyword != null && !keyword.isEmpty() && maLoai != null) {
            return repo.findByTenSPContainingIgnoreCaseAndLoaiSanPham_MaLoaiSanPham(keyword, maLoai);
        }
        if (keyword != null && !keyword.isEmpty()) {
            return repo.findByTenSPContainingIgnoreCase(keyword);
        }
        if (maLoai != null) {
            return repo.findByLoaiSanPham_MaLoaiSanPham(maLoai);
        }
        return repo.findAll();
    }

    public SanPham saveSanPham(SanPham sanPham) { return repo.save(sanPham); }

    public void deleteSanPham(Integer id) { repo.deleteById(id); }

    public SanPham getSanPhamById(Integer id) { return repo.findById(id).orElse(null); }

    public SanPham updateSanPham(SanPham sp) { return repo.save(sp); }
}
