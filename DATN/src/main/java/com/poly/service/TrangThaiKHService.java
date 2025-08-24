package com.poly.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.model.TrangThaiKH;
import com.poly.repository.TrangThaiKHRepository;

@Service
public class TrangThaiKHService {

    @Autowired
    private TrangThaiKHRepository repo;

    public List<TrangThaiKH> getAll() {
        return repo.findAll();
    }

    public TrangThaiKH getById(Integer id) {
        return repo.findById(id).orElse(null);
    }
    
    public TrangThaiKH getByTen(String ten) {
        return repo.findByTenTrangThai(ten).orElseThrow(
            () -> new RuntimeException("Không tìm thấy trạng thái: " + ten)
        );
    }

}
