package com.poly.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poly.model.BienTheSanPham;
import com.poly.model.BienThe_GiamGiaSP;
import com.poly.model.ChiTietGioHang;
import com.poly.model.GiamGiaSP;
import com.poly.model.GioHang;
import com.poly.model.TaiKhoan;
import com.poly.repository.BienTheSanPhamRepository;
import com.poly.repository.ChiTietGioHangRepository;
import com.poly.repository.GioHangRepository;

@Service
public class GioHangService {
	@Autowired
	private ChiTietGioHangRepository chitietgiohangRepository;
    @Autowired
    private GioHangRepository gioHangRepository;
    @Autowired
    private BienTheSanPhamRepository bienTheSanPhamRepository;


    @Transactional
    public ChiTietGioHang themSanPhamVaoGio(TaiKhoan tk, Integer maCT, Integer soLuong) {
        Optional<GioHang> optGioHang = gioHangRepository.findByTaiKhoan(tk);
        GioHang gioHang = optGioHang.orElseGet(() -> {
            GioHang gh = new GioHang();
            gh.setTaiKhoan(tk);
            gh.setNgayTao(LocalDateTime.now());
            gh.setTrangThaiGH("Đang xử lý");
            gh.setTongTien(BigDecimal.ZERO);
            return gioHangRepository.save(gh);
        });

        BienTheSanPham bienThe = bienTheSanPhamRepository.findById(maCT)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy biến thể sản phẩm."));

        BigDecimal giaGoc = bienThe.getGia();
        BigDecimal giamGiaThucTe = BigDecimal.ZERO;
        BigDecimal giaSauGiam = giaGoc;

        if (bienThe.getGiamGiaSPList() != null) {
            GiamGiaSP giamGiaSP = bienThe.getGiamGiaSPList().stream()
                .map(BienThe_GiamGiaSP::getGiamGiaSP)
                .filter(gg -> "Đang hoạt động".equalsIgnoreCase(gg.getTrangThai()) &&
                        gg.getThoiGianBatDau().isBefore(LocalDateTime.now()) &&
                        gg.getThoiGianKetThuc().isAfter(LocalDateTime.now()))
                .findFirst().orElse(null);

            if (giamGiaSP != null) {
                if (giamGiaSP.getPhanTramGiam() != null) {
                    giamGiaThucTe = giaGoc
                        .multiply(BigDecimal.valueOf(giamGiaSP.getPhanTramGiam()))
                        .divide(BigDecimal.valueOf(100));
                } else if (giamGiaSP.getGiaGiam() != null) {
                    giamGiaThucTe = BigDecimal.valueOf(giamGiaSP.getGiaGiam());
                }
                giaSauGiam = giaGoc.subtract(giamGiaThucTe);
            }
        }

        Optional<ChiTietGioHang> optCTGH = chitietgiohangRepository
            .findByGioHangAndChiTietSanPhamAndGiaTienThucTe(gioHang, bienThe, giaSauGiam);

        ChiTietGioHang ctgh;
        if (optCTGH.isPresent()) {
            ctgh = optCTGH.get();
            ctgh.setSoLuong(ctgh.getSoLuong() + soLuong);
            chitietgiohangRepository.save(ctgh);
        } else {
            ctgh = new ChiTietGioHang();
            ctgh.setGioHang(gioHang);
            ctgh.setChiTietSanPham(bienThe);
            ctgh.setSoLuong(soLuong);
            ctgh.setGiamGiaThucTe(giamGiaThucTe);
            ctgh.setGiaTienThucTe(giaSauGiam);
            ctgh.setNgayThem(LocalDateTime.now());
            chitietgiohangRepository.save(ctgh);
        }

        return ctgh; // ✅ Trả về ChiTietGioHang để controller gửi maCTGH cho frontend
    }



    @Transactional
    public void xoaSanPham(Integer maCTGH, TaiKhoan taiKhoan) {
        ChiTietGioHang ctgh = chitietgiohangRepository.findById(maCTGH)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm trong giỏ"));

        if (!ctgh.getGioHang().getTaiKhoan().getMaTK().equals(taiKhoan.getMaTK())) {
            throw new RuntimeException("Không có quyền xóa sản phẩm này");
        }

        chitietgiohangRepository.delete(ctgh);
    }


}
