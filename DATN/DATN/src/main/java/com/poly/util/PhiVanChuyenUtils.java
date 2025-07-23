package com.poly.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PhiVanChuyenUtils {

    private static final Set<String> NOI_THANH = Set.of("Hồ Chí Minh");
    private static final Set<String> VUNG_XA = Set.of("Cà Mau", "Hà Giang", "Điện Biên", "Lai Châu");

    private static final int PHI_NOI_THANH = 20000;
    private static final int PHI_THUONG = 30000;
    private static final int PHI_VUNG_XA = 40000;
    private static final int MUC_MIEN_PHI = 1_000_000;
    private static final int GIO_CAT_DON = 14;

    private static final String API_TINH = "https://provinces.open-api.vn/api/p/";
    private static final Set<String> danhSachTinhThanh = new HashSet<>();

    static {
        try {
            napDanhSachTinhThanh();
        } catch (IOException | InterruptedException e) {
            System.err.println("Không thể tải danh sách tỉnh thành từ API: " + e.getMessage());
        }
    }

    public static KetQuaVanChuyen tinhVanChuyen(String diaChiDayDu, int tongTien) {
        int phi = tinhPhi(diaChiDayDu, tongTien);

        int soNgayTu = 3, soNgayDen = 5; // Mặc định: tỉnh thường

        String diaChiChuan = chuanHoaChuoi(diaChiDayDu);

        for (String tp : NOI_THANH) {
            if (diaChiChuan.contains(chuanHoaChuoi(tp))) {
                soNgayTu = 1;
                soNgayDen = 2;
                break;
            }
        }

        for (String xa : VUNG_XA) {
            if (diaChiChuan.contains(chuanHoaChuoi(xa))) {
                soNgayTu = 4;
                soNgayDen = 6;
                break;
            }
        }

        LocalDate ngayBatDau = LocalTime.now().getHour() >= GIO_CAT_DON
                ? LocalDate.now().plusDays(1)
                : LocalDate.now();

        LocalDate ngayTu = ngayBatDau.plusDays(soNgayTu);
        LocalDate ngayDen = ngayBatDau.plusDays(soNgayDen);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return new KetQuaVanChuyen(phi, ngayTu.format(formatter), ngayDen.format(formatter));
    }

    private static int tinhPhi(String diaChiDayDu, int tongTien) {
        if (tongTien >= MUC_MIEN_PHI) return 0;
        if (diaChiDayDu == null || diaChiDayDu.isBlank()) return PHI_THUONG;

        String diaChiChuan = chuanHoaChuoi(diaChiDayDu);

        for (String tp : NOI_THANH) {
            if (diaChiChuan.contains(chuanHoaChuoi(tp))) return PHI_NOI_THANH;
        }

        for (String xa : VUNG_XA) {
            if (diaChiChuan.contains(chuanHoaChuoi(xa))) return PHI_VUNG_XA;
        }

        for (String tinh : danhSachTinhThanh) {
            if (diaChiChuan.contains(chuanHoaChuoi(tinh))) return PHI_THUONG;
        }

        return PHI_THUONG;
    }

    private static void napDanhSachTinhThanh() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(API_TINH)).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(response.body());

        for (JsonNode node : json) {
            danhSachTinhThanh.add(node.get("name").asText());
        }
    }

    private static String chuanHoaChuoi(String input) {
        if (input == null) return "";
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("[^a-zA-Z0-9 ]", "")
                .toLowerCase()
                .trim();
    }

    // ✅ DTO trả về gồm phí và khoảng ngày
    public static class KetQuaVanChuyen {
        private int phiVanChuyen;
        private String ngayGiaoDuKienTu;
        private String ngayGiaoDuKienDen;

        public KetQuaVanChuyen(int phiVanChuyen, String ngayTu, String ngayDen) {
            this.phiVanChuyen = phiVanChuyen;
            this.ngayGiaoDuKienTu = ngayTu;
            this.ngayGiaoDuKienDen = ngayDen;
        }

        public int getPhiVanChuyen() {
            return phiVanChuyen;
        }

        public String getNgayGiaoDuKienTu() {
            return ngayGiaoDuKienTu;
        }

        public String getNgayGiaoDuKienDen() {
            return ngayGiaoDuKienDen;
        }
    }
}
