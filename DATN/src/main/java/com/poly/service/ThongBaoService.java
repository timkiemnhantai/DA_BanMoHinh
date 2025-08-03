package com.poly.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.model.ThongBao;
import com.poly.repository.ThongBaoRepository;

@Service
public class ThongBaoService {

    @Autowired
    private ThongBaoRepository thongBaoRepo;



    public ThongBao taoThongBao(ThongBao thongBao) {
        return thongBaoRepo.save(thongBao);
    }

    public List<ThongBao> layThongBaoChuaDocTheoMaTK(Integer maTK) {
        return thongBaoRepo.findByTaiKhoan_MaTKAndDaDocFalseOrderByNgayTaoDesc(maTK);
    }
    public void danhDauDaDoc(Integer maThongBao) {
        thongBaoRepo.findById(maThongBao).ifPresent(tb -> {
            tb.setDaDoc(true);
            thongBaoRepo.save(tb);
        });
    }

}
