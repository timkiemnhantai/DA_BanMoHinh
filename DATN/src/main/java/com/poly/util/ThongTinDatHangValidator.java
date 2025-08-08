package com.poly.util;

import com.poly.model.DiaChi;
import com.poly.model.TaiKhoan;

public class ThongTinDatHangValidator {

    public static ThongTinDatHangResult kiemTraThongTinDatHang(TaiKhoan taiKhoan) {
        if (taiKhoan == null) {
            return new ThongTinDatHangResult("Không tìm thấy tài khoản!", "/TrangCaNhan");
        }

        DiaChi diaChi = taiKhoan.getDiaChiMacDinh();
        if (diaChi == null) {
            return new ThongTinDatHangResult("Bạn chưa có địa chỉ mặc định!", "/DiaChi");
        }

        if (diaChi.getHoTen() == null || diaChi.getHoTen().trim().isEmpty()) {
            return new ThongTinDatHangResult("Vui lòng nhập họ tên trong địa chỉ giao hàng.", "/DiaChi");
        }

        if (diaChi.getSoDienThoaiQuocTe() == null || diaChi.getSoDienThoaiQuocTe().trim().isEmpty()) {
            return new ThongTinDatHangResult("Vui lòng nhập số điện thoại trong địa chỉ giao hàng.", "/DiaChi");
        }

        if (diaChi.getDiaChiDayDu() == null || diaChi.getDiaChiDayDu().trim().isEmpty()) {
            return new ThongTinDatHangResult("Vui lòng nhập địa chỉ đầy đủ để giao hàng.", "/DiaChi");
        }

        return null; // Không có lỗi
    }

    // ✅ Inner class: Kết quả kiểm tra
    public static class ThongTinDatHangResult {
        private final String loi;
        private final String url;

        public ThongTinDatHangResult(String loi, String url) {
            this.loi = loi;
            this.url = url;
        }

        public String getLoi() {
            return loi;
        }

        public String getUrl() {
            return url;
        }

        public boolean coLoi() {
            return loi != null && !loi.trim().isEmpty();
        }
    }
}

