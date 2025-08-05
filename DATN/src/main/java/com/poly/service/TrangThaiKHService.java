package com.poly.service;


import com.poly.model.TrangThaiKH;
import com.poly.repository.TrangThaiKHRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrangThaiKHService {

    @Autowired
    private TrangThaiKHRepository trangThaiKHRepository;

    // Lấy tất cả trạng thái
    public List<TrangThaiKH> getAll() {
        return trangThaiKHRepository.findAll();
    }

    // Lấy trạng thái theo ID
    public Optional<TrangThaiKH> findById(Integer id) {
        return trangThaiKHRepository.findById(id);
    }

    // Lưu hoặc cập nhật
    public TrangThaiKH save(TrangThaiKH trangThaiKH) {
        return trangThaiKHRepository.save(trangThaiKH);
    }

    // Xóa
    public boolean delete(Integer id) {
        if (trangThaiKHRepository.existsById(id)) {
            trangThaiKHRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
