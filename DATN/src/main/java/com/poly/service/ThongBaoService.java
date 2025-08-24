package com.poly.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poly.model.ThongBao;
import com.poly.repository.ThongBaoRepository;

@Service
@Transactional
public class ThongBaoService {

    @Autowired
    private ThongBaoRepository thongBaoRepo;

    public ThongBao taoThongBao(ThongBao thongBao) {
        return thongBaoRepo.save(thongBao);
    }

    // -- lấy latest: ưu tiên daDoc=false, rồi mới theo ngayTao desc, giới hạn bằng limit --
    public List<ThongBao> layThongBaoMoiNhat(Integer maTK, int limit) {
        // Sort: daDoc asc (false trước true), sau đó ngayTao desc (mới trước)
        PageRequest pr = PageRequest.of(0, Math.max(1, limit),
                Sort.by(Sort.Order.asc("daDoc"), Sort.Order.desc("ngayTao")));
        return thongBaoRepo.findByTaiKhoan_MaTK(maTK, pr).getContent();
    }

    // nếu bạn muốn chỉ lấy tất cả chưa đọc
    public List<ThongBao> layThongBaoChuaDocTheoMaTK(Integer maTK) {
        return thongBaoRepo.findByTaiKhoan_MaTKAndDaDocFalseOrderByNgayTaoDesc(maTK);
    }

    public long demThongBaoChuaDoc(Integer maTK) {
        return thongBaoRepo.countByTaiKhoan_MaTKAndDaDocFalse(maTK);
    }

    public void danhDauDaDoc(Integer maThongBao) {
        thongBaoRepo.findById(maThongBao).ifPresent(tb -> {
            if (tb.getDaDoc() == null || !tb.getDaDoc()) {
                tb.setDaDoc(true);
                thongBaoRepo.save(tb);
            }
        });
    }

    // đánh dấu tất cả đã đọc cho user (dùng @Modifying repo nếu có)
    public int danhDauTatCaDaDoc(Integer maTK) {
        return thongBaoRepo.markAllAsReadByMaTK(maTK); // trả số bản ghi đã update
    }
    @Transactional
    public void xoaThongBao(Integer maThongBao) {
        thongBaoRepo.deleteById(maThongBao);
    }

    @Transactional
    public void xoaTatCaThongBaoCuaUser(Integer maTK) {
        thongBaoRepo.deleteByTaiKhoan_MaTK(maTK);
    }

}
