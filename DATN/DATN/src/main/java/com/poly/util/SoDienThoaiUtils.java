package com.poly.util;

public class SoDienThoaiUtils {

    public static String chuyenDinhDangQuocTe(String soDienThoai) {
        if (soDienThoai == null || soDienThoai.isBlank()) return "";
        soDienThoai = soDienThoai.trim();

        if (soDienThoai.startsWith("0")) {
            return "(+84)" + soDienThoai.substring(1);
        }

        // Nếu đã có định dạng quốc tế rồi thì giữ nguyên
        return soDienThoai;
    }
}