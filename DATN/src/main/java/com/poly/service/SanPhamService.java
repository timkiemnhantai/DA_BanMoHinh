package com.poly.service;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poly.dto.BienTheSanPhamDTO;
import com.poly.dto.ChiTietSanPhamDTO;
import com.poly.dto.GiaGiamDTO;
import com.poly.dto.SanPhamDTO;
import com.poly.model.AnhChiTiet;
import com.poly.model.AnhSanPham;
import com.poly.model.BienTheSanPham;
import com.poly.model.ChiTietDonHang;
import com.poly.model.DanhGiaSP;

import com.poly.model.SanPham;
import com.poly.repository.AnhChiTietRepository;
import com.poly.repository.AnhSanPhamRepository;
import com.poly.repository.BienTheSanPhamRepository;
import com.poly.repository.DanhGiaSPRepository;
import com.poly.repository.SanPhamRepository;
import com.poly.util.BoDau;
import com.poly.util.GiamGiaUtil;

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

	@Autowired
	private GiamGiaUtil giamGiaUtil;

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

	@Transactional(readOnly = true)
	public List<SanPhamDTO> locSanPham(SanPhamDTO filter) {
	    String keyword = filter.getKeyword();
	    String loaiKeyword = filter.getLoaiKeyword();
	    String sapXep = filter.getSapXep();
	    Boolean giamGia = filter.getGiamGia();
	    Boolean banChay = filter.getBanChay(); // ✅ đã dùng để lọc bán chạy

	    if (keyword != null) {
	        keyword = BoDau.removeVietnameseTone(keyword).toLowerCase();
	    }
	    if (loaiKeyword != null) {
	        loaiKeyword = BoDau.removeVietnameseTone(loaiKeyword).toLowerCase();
	    }

	    List<SanPham> danhSachSP = sanphamRepository.findAll();
	    List<SanPhamDTO> ketQua = new ArrayList<>();

	    for (SanPham sp : danhSachSP) {
	        // Lọc theo keyword
	        if (keyword != null && !keyword.isBlank()) {
	            if (!(sp.getTenKhongDau().toLowerCase().contains(keyword)
	                    || sp.getMoTaChung().toLowerCase().contains(keyword)
	                    || sp.getLoaiSanPham().getTenLoaiKhongDau().toLowerCase().contains(keyword))) {
	                continue;
	            }
	        }

	        // Lọc theo loại
	        if (loaiKeyword != null && !loaiKeyword.isBlank()) {
	            if (!sp.getLoaiSanPham().getTenLoaiKhongDau().toLowerCase().contains(loaiKeyword)) {
	                continue;
	            }
	        }

	        List<BienTheSanPham> bienThes = sp.getBienTheSanPham();
	        if (bienThes == null || bienThes.isEmpty()) continue;

	        Integer maxPhanTramGiam = null;
	        Long maxGiaGiam = null;
	        BigDecimal minGiaSauGiam = null;
	        long tongSLBan = 0;
	        boolean hasValidBienThe = false;

	        for (BienTheSanPham bt : bienThes) {
	            GiaGiamDTO giam = giamGiaUtil.tinhGiaSauGiam(bt);

	            // Nếu lọc giảm giá thì bỏ qua biến thể không có khuyến mãi
	            if (Boolean.TRUE.equals(giamGia) && giam.getPhanTramGiam() == null && giam.getGiaGiam() == null) {
	                continue;
	            }

	            hasValidBienThe = true;

	            if (giam.getPhanTramGiam() != null) {
	                maxPhanTramGiam = (maxPhanTramGiam == null) ? giam.getPhanTramGiam()
	                        : Math.max(maxPhanTramGiam, giam.getPhanTramGiam());
	            }

	            if (giam.getGiaGiam() != null) {
	                maxGiaGiam = (maxGiaGiam == null) ? giam.getGiaGiam() : Math.max(maxGiaGiam, giam.getGiaGiam());
	            }

	            if (minGiaSauGiam == null || giam.getGiaSauGiam().compareTo(minGiaSauGiam) < 0) {
	                minGiaSauGiam = giam.getGiaSauGiam();
	            }

	            // Tổng số lượng bán
	            List<ChiTietDonHang> ctdhs = bt.getChiTietDonHangs();
	            if (ctdhs != null) {
	                for (ChiTietDonHang ctdh : ctdhs) {
	                    if (ctdh.getDonHang() != null && ctdh.getDonHang().getTrangThaiDH() != null
	                            && ctdh.getDonHang().getTrangThaiDH().getMaTTDH() == 4) {
	                        tongSLBan += (ctdh.getSoLuongSP() != null ? ctdh.getSoLuongSP() : 0);
	                    }
	                }
	            }
	        }

	        // Bỏ qua nếu lọc giảm giá mà không có biến thể giảm giá hợp lệ
	        if (Boolean.TRUE.equals(giamGia) && !hasValidBienThe) {
	            continue;
	        }

	        // ✅ Bỏ qua nếu lọc bán chạy mà chưa bán sản phẩm nào
	        if (Boolean.TRUE.equals(banChay) && tongSLBan == 0) {
	            continue;
	        }

	        // Tính điểm đánh giá trung bình
	        List<DanhGiaSP> danhGias = sp.getDanhGiaList();
	        double diemTB = 0;
	        if (danhGias != null && !danhGias.isEmpty()) {
	            diemTB = danhGias.stream().mapToInt(DanhGiaSP::getSoSao).average().orElse(0.0);
	        }

	        ketQua.add(new SanPhamDTO(sp, maxPhanTramGiam, maxGiaGiam, minGiaSauGiam, tongSLBan, diemTB));
	    }

	    // ✅ Nếu đang lọc bán chạy nhưng không truyền sapXep = "banchay", ta vẫn sắp theo bán chạy
	    if (Boolean.TRUE.equals(banChay)) {
	        sapXep = "banchay";
	    }

	    // Sắp xếp kết quả
	    if (sapXep == null) {
	        sapXep = "moinhat";
	    }

	    Comparator<SanPhamDTO> comparator;
	    switch (sapXep) {
	        case "banchay":
	            comparator = Comparator.comparing(SanPhamDTO::getTongSoLuongBan, Comparator.nullsLast(Long::compareTo))
	                    .reversed();
	            break;
	        case "giathap":
	            comparator = Comparator.comparing(SanPhamDTO::getGiaSauGiam, Comparator.nullsLast(BigDecimal::compareTo));
	            break;
	        case "giacao":
	            comparator = Comparator.comparing(SanPhamDTO::getGiaSauGiam, Comparator.nullsLast(BigDecimal::compareTo))
	                    .reversed();
	            break;
	        default:
	            comparator = Comparator.comparing(
	                    (SanPhamDTO dto) -> dto.getSanPham() != null ? dto.getSanPham().getNgayTao() : null,
	                    Comparator.nullsLast(Comparator.reverseOrder()));
	            break;
	    }

	    ketQua.sort(comparator);
	    return ketQua;
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
		if (optSanPham.isEmpty())
			return null;

		SanPham sanPham = optSanPham.get();
		List<BienTheSanPham> danhSachBienThe = bienthesanphamRepository.findBySanPham_MaSP(maSP);

		List<BienTheSanPhamDTO> listDTO = new ArrayList<>();

		for (BienTheSanPham bienThe : danhSachBienThe) {
			BienTheSanPhamDTO bienTheDTO = new BienTheSanPhamDTO();
			bienTheDTO.setBienThe(bienThe);
			bienTheDTO.setDsAnhChiTiet(anhchitietRepository.findByChiTietSanPham_MaCTSP(bienThe.getMaCTSP()));

			// Sử dụng util để tính giá sau giảm
			GiaGiamDTO giaGiamDTO = giamGiaUtil.tinhGiaSauGiam(bienThe);
			bienTheDTO.setPhanTramGiam(giaGiamDTO.getPhanTramGiam());
			bienTheDTO.setGiaGiam(giaGiamDTO.getGiaGiam());
			bienTheDTO.setGiaSauGiam(giaGiamDTO.getGiaSauGiam());

			listDTO.add(bienTheDTO);
		}

		// Tính điểm trung bình và số lượng bán
		List<DanhGiaSP> danhGiaList = danhgiaRepository.findBySanPham_MaSP(maSP);
		double diemTrungBinh = danhGiaList.isEmpty() ? 0
				: danhGiaList.stream().mapToInt(DanhGiaSP::getSoSao).average().orElse(0.0);

		long tongSoLuongBan = danhSachBienThe.stream().flatMap(bt -> bt.getChiTietDonHangs().stream())
				.filter(ct -> ct.getDonHang() != null && ct.getDonHang().getTrangThaiDH() != null
						&& ct.getDonHang().getTrangThaiDH().getMaTTDH() == 4)
				.mapToLong(ct -> ct.getSoLuongSP() == null ? 0 : ct.getSoLuongSP()).sum();

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
