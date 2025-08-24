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
    private static final Set<String> VUNG_XA = Set.of(
    	    "Cà Mau", "Hà Giang", "Điện Biên", "Lai Châu", "Trường Sa", "Hoàng Sa"
    	);


    // Quy định phí mới
    private static final int PHI_NHANH = 20_000;          // NHANH cố định 20k
    private static final int PHI_THUONG_DELTA = 5_000;    // THUONG = NHANH - 5k
    private static final int PHI_SIEUTOC_DELTA = 10_000;  // SIEU_TOC = NHANH + 10k

    // Phí cộng thêm nếu vùng xa
    private static final int PHI_VUNG_XA = 10_000;        // phí cộng thêm cho vùng xa

    // miễn phí nếu >= MUC_MIEN_PHI
    private static final int MUC_MIEN_PHI = 1_000_000;
    private static final int GIO_CAT_DON = 14;

    private static final String API_TINH = "https://provinces.open-api.vn/api/p/";
    private static final Set<String> danhSachTinhThanh = new HashSet<>();

    public enum PhuongThuc { NHANH, THUONG, SIEU_TOC }

    public static List<KetQuaVanChuyen> tinhTatCaPhuongThuc(String diaChiDayDu, int tongTien) {
        List<KetQuaVanChuyen> ketQuaList = new ArrayList<>();

        KetQuaVanChuyen base = tinhVanChuyen(diaChiDayDu, tongTien);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate ngayTuBase = LocalDate.parse(base.getNgayGiaoDuKienTu(), formatter);
        LocalDate ngayDenBase = LocalDate.parse(base.getNgayGiaoDuKienDen(), formatter);

        if (base.getPhiVanChuyen() == 0 || tongTien >= MUC_MIEN_PHI) {
            ketQuaList.add(new KetQuaVanChuyen(PhuongThuc.NHANH.name(), 0,
                    ngayTuBase.format(formatter), ngayDenBase.format(formatter)));
            ketQuaList.add(new KetQuaVanChuyen(PhuongThuc.THUONG.name(), 0,
                    ngayTuBase.format(formatter), ngayDenBase.format(formatter)));
            ketQuaList.add(new KetQuaVanChuyen(PhuongThuc.SIEU_TOC.name(), 0,
                    ngayTuBase.format(formatter), ngayDenBase.format(formatter)));
            return ketQuaList;
        }

        LocalDate today = LocalDate.now();

        // Kiểm tra có vùng xa hay không
        boolean laVungXa = isVungXa(diaChiDayDu);
        int phiCongThem = laVungXa ? PHI_VUNG_XA : 0;

        // NHANH
        int phiNhanh = PHI_NHANH + phiCongThem;
        ketQuaList.add(new KetQuaVanChuyen(PhuongThuc.NHANH.name(), phiNhanh,
                maxDate(ngayTuBase, today).format(formatter),
                maxDate(ngayDenBase, today).format(formatter)));

        // THUONG
        int phiThuong = Math.max(0, PHI_NHANH - PHI_THUONG_DELTA) + phiCongThem;
        ketQuaList.add(new KetQuaVanChuyen(PhuongThuc.THUONG.name(), phiThuong,
                maxDate(ngayTuBase.plusDays(1), today).format(formatter),
                maxDate(ngayDenBase.plusDays(1), today).format(formatter)));

        // SIEU_TOC
        int phiSieuToc = PHI_NHANH + PHI_SIEUTOC_DELTA + phiCongThem;
        ketQuaList.add(new KetQuaVanChuyen(PhuongThuc.SIEU_TOC.name(), phiSieuToc,
                maxDate(ngayTuBase.minusDays(1), today).format(formatter),
                maxDate(ngayDenBase.minusDays(1), today).format(formatter)));

        return ketQuaList;
    }

    public static KetQuaVanChuyen layKetQuaTheoPhuongThuc(String diaChi, int tongTien, String phuongThuc) {
        if (phuongThuc == null) phuongThuc = "NHANH";

        String keyTmp = phuongThuc.trim().toUpperCase(Locale.ROOT);
        if ("TIET_KIEM".equals(keyTmp)) keyTmp = "THUONG";
        else if ("HOA_TOC".equals(keyTmp) || "HOA_TỐC".equals(keyTmp)) keyTmp = "SIEU_TOC";

        final String keyFinal = keyTmp;

        List<KetQuaVanChuyen> ds = tinhTatCaPhuongThuc(diaChi, tongTien);
        return ds.stream()
                .filter(kq -> kq.getPhuongThuc().equalsIgnoreCase(keyFinal))
                .findFirst()
                .orElse(ds.get(0));
    }

    public static KetQuaVanChuyen tinhVanChuyen(String diaChiDayDu, int tongTien) {
        int phi = tinhPhi(diaChiDayDu, tongTien);

        int soNgayTu = 3, soNgayDen = 5;

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

        if (danhSachTinhThanh.isEmpty()) {
            napDanhSachTinhThanh();
        }

        boolean khopTinhAPI = danhSachTinhThanh.stream()
                .map(PhiVanChuyenUtils::chuanHoaChuoi)
                .anyMatch(diaChiChuan::contains);

        if (!khopTinhAPI) {
            soNgayTu = 5;
            soNgayDen = 7;
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
        if (diaChiDayDu == null || diaChiDayDu.isBlank()) return PHI_NHANH;

        if (danhSachTinhThanh.isEmpty()) napDanhSachTinhThanh();

        boolean laVungXa = isVungXa(diaChiDayDu);

        return PHI_NHANH + (laVungXa ? PHI_VUNG_XA : 0);
    }

    private static boolean isVungXa(String diaChi) {
        String diaChiChuan = chuanHoaChuoi(diaChi);
        for (String xa : VUNG_XA) {
            if (diaChiChuan.contains(chuanHoaChuoi(xa))) return true;
        }
        return false;
    }

    private static void napDanhSachTinhThanh() {
        try {
            HttpClient client = HttpClient.newBuilder()
                    .followRedirects(HttpClient.Redirect.ALWAYS)
                    .build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_TINH))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response.body());

            for (JsonNode node : json) {
                if (node.has("name")) danhSachTinhThanh.add(node.get("name").asText());
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Không thể tải danh sách tỉnh thành từ API: " + e.getMessage());
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

    private static LocalDate maxDate(LocalDate a, LocalDate b) {
        return a.isBefore(b) ? b : a;
    }

    public static class KetQuaVanChuyen {
        private String phuongThuc;
        private int phiVanChuyen;
        private String ngayGiaoDuKienTu;
        private String ngayGiaoDuKienDen;

        public KetQuaVanChuyen(int phiVanChuyen, String ngayTu, String ngayDen) {
            this.phiVanChuyen = phiVanChuyen;
            this.ngayGiaoDuKienTu = ngayTu;
            this.ngayGiaoDuKienDen = ngayDen;
            this.phuongThuc = PhuongThuc.NHANH.name();
        }

        public KetQuaVanChuyen(String phuongThuc, int phiVanChuyen, String ngayTu, String ngayDen) {
            this.phuongThuc = phuongThuc;
            this.phiVanChuyen = phiVanChuyen;
            this.ngayGiaoDuKienTu = ngayTu;
            this.ngayGiaoDuKienDen = ngayDen;
        }

        public String getPhuongThuc() { return phuongThuc; }
        public int getPhiVanChuyen() { return phiVanChuyen; }
        public String getNgayGiaoDuKienTu() { return ngayGiaoDuKienTu; }
        public String getNgayGiaoDuKienDen() { return ngayGiaoDuKienDen; }

        public void setPhuongThuc(String phuongThuc) { this.phuongThuc = phuongThuc; }
        public void setPhiVanChuyen(int phiVanChuyen) { this.phiVanChuyen = phiVanChuyen; }
        public void setNgayGiaoDuKienTu(String ngayGiaoDuKienTu) { this.ngayGiaoDuKienTu = ngayGiaoDuKienTu; }
        public void setNgayGiaoDuKienDen(String ngayGiaoDuKienDen) { this.ngayGiaoDuKienDen = ngayGiaoDuKienDen; }
    }
}
