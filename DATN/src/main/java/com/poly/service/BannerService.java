package com.poly.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poly.model.Banner;
import com.poly.repository.BannerRepository;

@Service
public class BannerService {

    @Autowired
    private BannerRepository bannerRepo;

    // Lấy tất cả banner
    @Transactional(readOnly = true)
    public List<Banner> getAllBanners() {
        return bannerRepo.findAll();
    }

    // Lấy banner theo ID
    @Transactional(readOnly = true)
    public Banner getBannerById(Integer id) {
        return bannerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy banner có mã: " + id));
    }

    // Thêm banner mới
    @Transactional
    public Banner createBanner(Banner banner) {
        banner.setNgayTao(LocalDate.now());
        return bannerRepo.save(banner);
    }

    // Cập nhật banner
    @Transactional
    public Banner updateBanner(Integer id, Banner bannerDetails) {
        Banner banner = bannerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy banner có mã: " + id));

        banner.setTieuDeBanner(bannerDetails.getTieuDeBanner());
        banner.setAnhBanner(bannerDetails.getAnhBanner());
        banner.setLink(bannerDetails.getLink());
        banner.setLoaiBanner(bannerDetails.getLoaiBanner());
        banner.setNgayKetThuc(bannerDetails.getNgayKetThuc());

        return bannerRepo.save(banner);
    }

    // Xóa banner
    @Transactional
    public void deleteBanner(Integer id) {
        if (!bannerRepo.existsById(id)) {
            throw new RuntimeException("Không tìm thấy banner có mã: " + id);
        }
        bannerRepo.deleteById(id);
    }
}
