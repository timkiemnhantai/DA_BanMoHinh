package com.poly.service;

import java.util.List;
import java.util.ArrayList;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.repository.ChiTietDonHangRepository;
import com.poly.repository.DonHangRepository;
import com.poly.dto.DonHangDTO;
import com.poly.model.DonHang;
import com.poly.model.ChiTietDonHang;

@Service
public class DonHangService {
    @Autowired
    private DonHangRepository donHangRepo;

    @Autowired
    private ChiTietDonHangRepository chiTietDonHangRepo;
	
	public List<DonHangDTO> layDonHangVaChiTietTheoMaTK(int maTK) {
        List<DonHang> ds = donHangRepo.findByTaiKhoan_MaTKOrderByNgayDatDesc(maTK);
        List<DonHangDTO> dsDTO = new ArrayList<>();

        for (DonHang dh : ds) {
            List<ChiTietDonHang> chiTiet = chiTietDonHangRepo.findByDonHang_MaDH(dh.getMaDH());
            DonHangDTO dto = new DonHangDTO();
            dto.setDonHang(dh);
            dto.setChiTietDonHangs(chiTiet);
            dsDTO.add(dto);
        }

        return dsDTO;
    }
}
