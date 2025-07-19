package com.poly.service;

import java.math.BigDecimal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poly.dto.BienTheSanPhamDTO;
import com.poly.dto.ChiTietSanPhamDTO;
import com.poly.dto.SanPhamDTO;
import com.poly.model.AnhChiTiet;
import com.poly.model.AnhSanPham;
import com.poly.model.BienTheSanPham;
import com.poly.model.BienThe_GiamGiaSP;
import com.poly.model.DanhGiaSP;
import com.poly.model.GiamGiaSP;
import com.poly.model.SanPham;
import com.poly.repository.AnhChiTietRepository;
import com.poly.repository.AnhSanPhamRepository;
import com.poly.repository.BienTheSanPhamRepository;
import com.poly.repository.DanhGiaSPRepository;
import com.poly.repository.SanPhamRepository;
import com.poly.util.BoDau;

@Service
public class SanPhamService {

    @Autowired
    private SanPhamRepository sanphamRepository;

    @Autowired
    private BienTheSanPhamRepository bienthesanphamRepository;

    @Autowired
    private AnhChiTietRepository anhchitietRepository;

    @Autowired
    private DanhGiaSPRepository danhgiaRepository;

    @Autowired
    private AnhSanPhamRepository anhSanPhamRepository;

    // Lấy toàn bộ sản phẩm
    public List<SanPham> getAll() {
        return sanphamRepository.findAll();
    }

    public Optional<SanPham> getAnhSanPhamById(int id) {
        return sanphamRepository.findById(id);
    }

    public SanPham saveAnhSanPham(SanPham anhsp) {
        return sanphamRepository.save(anhsp);
    }

    public SanPham update(Integer id, SanPham SanPhamDetails) {
        return sanphamRepository.findById(id).map(SP -> {
            SP.setTenSP(SanPhamDetails.getTenSP());
            return sanphamRepository.save(SP);
        }).orElse(null);
    }

    public boolean deleteAnhSanPham(int id) {
        if (sanphamRepository.existsById(id)) {
            sanphamRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<SanPhamDTO> locSanPham(SanPhamDTO filter) {
        String keyword = filter.getKeyword();
        String loaiKeyword = filter.getLoaiKeyword();
        String sapXep = filter.getSapXep();
        Boolean giamGia = filter.getGiamGia();

        if (keyword != null) {
            keyword = BoDau.removeVietnameseTone(keyword).toLowerCase();
        }
        if (loaiKeyword != null) {
            loaiKeyword = BoDau.removeVietnameseTone(loaiKeyword).toLowerCase();
        }

        return sanphamRepository.locSanPhamDTO(keyword, loaiKeyword, sapXep, giamGia, "Đang hoạt động");
    }

    public List<SanPhamDTO> laySanPhamBanChay() {
        return sanphamRepository.laySanPhamBanChay("Đang hoạt động");
    }

    public SanPhamDTO findSanPhamDTOById(Integer id) {
        return sanphamRepository.findSanPhamDTOById(id, "Đang hoạt động");
    }

    public List<BienTheSanPham> getBienTheBySanPham(Integer maSP) {
        return bienthesanphamRepository.findBySanPham_MaSP(maSP);
    }

    public List<AnhChiTiet> getAnhChiTietByMaCTSP(Integer maCTSP) {
        return anhchitietRepository.findByChiTietSanPham_MaCTSP(maCTSP);
    }

    public List<DanhGiaSP> getDanhGiaBySanPham(Integer maSP) {
        return danhgiaRepository.findBySanPham_MaSP(maSP);
    }

    public List<AnhSanPham> getAnhSanPhamByMaSP(Integer maSP) {
        return anhSanPhamRepository.findBySanPham_MaSP(maSP);
    }

    // ✅ Hàm mới: Trả về DTO tổng hợp cho chi tiết sản phẩm
    @Transactional(readOnly = true)
    public ChiTietSanPhamDTO layChiTietSanPhamDTO(Integer maSP) {
        Optional<SanPham> optSanPham = sanphamRepository.findById(maSP);
        if (optSanPham.isEmpty()) return null;

        SanPham sanPham = optSanPham.get();
        List<BienTheSanPham> danhSachBienThe = bienthesanphamRepository.findBySanPham_MaSP(maSP);
        
        List<BienTheSanPhamDTO> listDTO = new ArrayList<>();

        for (BienTheSanPham bienThe : danhSachBienThe) {
            BienTheSanPhamDTO bienTheDTO = new BienTheSanPhamDTO();
            bienTheDTO.setBienThe(bienThe);
            bienTheDTO.setDsAnhChiTiet(anhchitietRepository.findByChiTietSanPham_MaCTSP(bienThe.getMaCTSP()));

            // Tính giảm giá
            Integer phanTramGiam = null;
            Long giaGiam = null;
            BigDecimal giaSauGiam = bienThe.getGia();

            if (bienThe.getGiamGiaSPList() != null) {
                GiamGiaSP giamGiaSP = bienThe.getGiamGiaSPList().stream()
                    .map(BienThe_GiamGiaSP::getGiamGiaSP)
                    .filter(gg -> "Đang hoạt động".equalsIgnoreCase(gg.getTrangThai()) &&
                            gg.getThoiGianBatDau().isBefore(LocalDateTime.now()) &&
                            gg.getThoiGianKetThuc().isAfter(LocalDateTime.now()))
                    .findFirst().orElse(null);

                if (giamGiaSP != null) {
                    if (giamGiaSP.getPhanTramGiam() != null) {
                        phanTramGiam = giamGiaSP.getPhanTramGiam();
                        giaSauGiam = bienThe.getGia().multiply(BigDecimal.valueOf(1 - phanTramGiam / 100.0));
                    } else if (giamGiaSP.getGiaGiam() != null) {
                        giaGiam = giamGiaSP.getGiaGiam();
                        giaSauGiam = bienThe.getGia().subtract(BigDecimal.valueOf(giaGiam));
                    }
                }
            }

            bienTheDTO.setPhanTramGiam(phanTramGiam);
            bienTheDTO.setGiaGiam(giaGiam);
            bienTheDTO.setGiaSauGiam(giaSauGiam);
            listDTO.add(bienTheDTO);
        }

        // Tính điểm trung bình và số lượng bán
        List<DanhGiaSP> danhGiaList = danhgiaRepository.findBySanPham_MaSP(maSP);
        double diemTrungBinh = danhGiaList.isEmpty() ? 0 :
            danhGiaList.stream().mapToInt(DanhGiaSP::getSoSao).average().orElse(0.0);

        long tongSoLuongBan = danhSachBienThe.stream()
            .flatMap(bt -> bt.getChiTietDonHangs().stream())
            .filter(ct -> ct.getDonHang() != null &&
                          ct.getDonHang().getTrangThaiDH() != null &&
                          ct.getDonHang().getTrangThaiDH().getMaTTDH() == 4)
            .mapToLong(ct -> ct.getSoLuongSP() == null ? 0 : ct.getSoLuongSP())
            .sum();

        // Trả DTO
        ChiTietSanPhamDTO dto = new ChiTietSanPhamDTO();
        dto.setSanPham(sanPham);
        dto.setDanhSachBienThe(listDTO);
        dto.setDsAnhSanPham(sanPham.getAnhSanPham());
        dto.setDiemTrungBinh(diemTrungBinh);
        dto.setTongSoLuongBan(tongSoLuongBan);
        return dto;
    }

}
