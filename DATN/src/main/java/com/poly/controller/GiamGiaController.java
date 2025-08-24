package com.poly.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.poly.model.*;
import com.poly.repository.*;
import com.poly.service.GiamGiaSPService;

@RestController
@RequestMapping("/api/giamgia")
public class GiamGiaController {

	private final GiamGiaSPRepository giamGiaRepo;
	private final BienTheSanPhamRepository bienTheRepo;
	private final BienTheGiamGiaSPRepository btggRepo;
	private final GiamGiaSPService giamGiaService; // để trigger capNhatTrangThaiGiamGia()

	public GiamGiaController(GiamGiaSPRepository giamGiaRepo, BienTheSanPhamRepository bienTheRepo,
			BienTheGiamGiaSPRepository btggRepo, GiamGiaSPService giamGiaService) {
		this.giamGiaRepo = giamGiaRepo;
		this.bienTheRepo = bienTheRepo;
		this.btggRepo = btggRepo;
		this.giamGiaService = giamGiaService;
	}

	// ---------- help: parse datetime from frontend ----------
	private LocalDateTime parseDateTime(Object o) {
		if (o == null)
			return null;
		if (o instanceof LocalDateTime)
			return (LocalDateTime) o;
		String s = String.valueOf(o).trim();
		if (s.isEmpty())
			return null;
		// try ISO first
		try {
			return LocalDateTime.parse(s);
		} catch (DateTimeParseException ex) {
			// try replace space with T
			try {
				return LocalDateTime.parse(s.replace(' ', 'T'));
			} catch (Exception e) {
				return null;
			}
		}
	}

	// ---------- CRUD ----------
	@GetMapping
	public List<GiamGiaSP> listAll() {
		return giamGiaRepo.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<GiamGiaSP> getOne(@PathVariable Integer id) {
		return giamGiaRepo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	/**
	 * Create: nếu payload chứa PhanTramGiam hoặc GiaGiam và giá trị = 0 (hoặc chuỗi rỗng)
	 * -> lưu NULL cho field đó. Nếu khác 0 thì lưu giá trị.
	 */
	@PostMapping
	@Transactional
	public ResponseEntity<GiamGiaSP> create(@RequestBody Map<String, Object> payload) {
		GiamGiaSP e = new GiamGiaSP();
		if (payload.containsKey("TenGG"))
			e.setTenGG(String.valueOf(payload.get("TenGG")));

		// Phần trăm giảm: nếu không có hoặc =0 => null
		if (payload.containsKey("PhanTramGiam")) {
			Object v = payload.get("PhanTramGiam");
			if (v == null || String.valueOf(v).trim().isEmpty()) {
				e.setPhanTramGiam(null);
			} else {
				Integer parsed = null;
				try {
					if (v instanceof Number) parsed = ((Number) v).intValue();
					else parsed = Integer.parseInt(String.valueOf(v).trim());
				} catch (Exception ex) {
					parsed = null;
				}
				e.setPhanTramGiam((parsed == null || parsed.intValue() == 0) ? null : parsed);
			}
		}

		// GiaGiam: nếu không có hoặc =0 => null
		if (payload.containsKey("GiaGiam")) {
			Object v = payload.get("GiaGiam");
			if (v == null || String.valueOf(v).trim().isEmpty()) {
				e.setGiaGiam(null);
			} else {
				Long parsed = null;
				try {
					if (v instanceof Number) parsed = ((Number) v).longValue();
					else parsed = Long.parseLong(String.valueOf(v).trim());
				} catch (Exception ex) {
					parsed = null;
				}
				e.setGiaGiam((parsed == null || parsed.longValue() == 0L) ? null : parsed);
			}
		}

		if (payload.containsKey("TrangThai"))
			e.setTrangThai(String.valueOf(payload.get("TrangThai")));
		if (payload.containsKey("ThoiGianBatDau"))
			e.setThoiGianBatDau(parseDateTime(payload.get("ThoiGianBatDau")));
		if (payload.containsKey("ThoiGianKetThuc"))
			e.setThoiGianKetThuc(parseDateTime(payload.get("ThoiGianKetThuc")));

		GiamGiaSP saved = giamGiaRepo.save(e);

		// Nếu muốn xử lý variants ngay lúc create, có thể đọc payload.get("variants") tại đây
		// (mình giữ nguyên hành vi hiện tại — nếu cần mình thêm phần đó)

		return ResponseEntity.status(201).body(saved);
	}

	/**
	 * Update: tương tự create, nếu PhanTramGiam hoặc GiaGiam = 0 hoặc chuỗi rỗng -> set null
	 */
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<GiamGiaSP> update(@PathVariable Integer id, @RequestBody Map<String, Object> payload) {
		Optional<GiamGiaSP> opt = giamGiaRepo.findById(id);
		if (!opt.isPresent())
			return ResponseEntity.notFound().build();
		GiamGiaSP e = opt.get();

		if (payload.containsKey("TenGG"))
			e.setTenGG(String.valueOf(payload.get("TenGG")));

		// PhanTramGiam
		if (payload.containsKey("PhanTramGiam")) {
			Object v = payload.get("PhanTramGiam");
			if (v == null || String.valueOf(v).trim().isEmpty()) {
				e.setPhanTramGiam(null);
			} else {
				Integer parsed = null;
				try {
					if (v instanceof Number) parsed = ((Number) v).intValue();
					else parsed = Integer.parseInt(String.valueOf(v).trim());
				} catch (Exception ex) {
					parsed = null;
				}
				e.setPhanTramGiam((parsed == null || parsed.intValue() == 0) ? null : parsed);
			}
		}

		// GiaGiam
		if (payload.containsKey("GiaGiam")) {
			Object v = payload.get("GiaGiam");
			if (v == null || String.valueOf(v).trim().isEmpty()) {
				e.setGiaGiam(null);
			} else {
				Long parsed = null;
				try {
					if (v instanceof Number) parsed = ((Number) v).longValue();
					else parsed = Long.parseLong(String.valueOf(v).trim());
				} catch (Exception ex) {
					parsed = null;
				}
				e.setGiaGiam((parsed == null || parsed.longValue() == 0L) ? null : parsed);
			}
		}

		if (payload.containsKey("TrangThai"))
			e.setTrangThai(String.valueOf(payload.get("TrangThai")));
		if (payload.containsKey("ThoiGianBatDau"))
			e.setThoiGianBatDau(parseDateTime(payload.get("ThoiGianBatDau")));
		if (payload.containsKey("ThoiGianKetThuc"))
			e.setThoiGianKetThuc(parseDateTime(payload.get("ThoiGianKetThuc")));

		GiamGiaSP saved = giamGiaRepo.save(e);
		return ResponseEntity.ok(saved);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		if (!giamGiaRepo.existsById(id))
			return ResponseEntity.notFound().build();
		// xóa mapping trước để tránh foreign key constraint
		btggRepo.deleteByGiamGiaId(id);
		giamGiaRepo.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	// ---------- Attach variants to discount ----------
	/**
	 * Body: array of MaCTSP (integers) e.g. [101,102] Behaviour: remove existing
	 * mappings for this discount, then create new ones for provided IDs (skips
	 * not-found variants).
	 */
	@PostMapping(value = "/{id}/variants", consumes = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseEntity<?> attachVariants(@PathVariable Integer id, @RequestBody List<Integer> maCtspList) {
		if (!giamGiaRepo.existsById(id)) {
			return ResponseEntity.notFound().build();
		}

		// chuẩn hoá list (null -> empty)
		List<Integer> ids = (maCtspList == null) ? Collections.emptyList()
				: maCtspList.stream().filter(Objects::nonNull).collect(Collectors.toList());

		// xóa mapping cũ (repository phải có deleteByGiamGiaId)
		btggRepo.deleteByGiamGiaId(id);

		// lấy đối tượng giảm giá 1 lần
		GiamGiaSP giamGia = giamGiaRepo.findById(id).orElse(null);
		if (giamGia == null)
			return ResponseEntity.notFound().build();

		List<BienThe_GiamGiaSP> toSave = new ArrayList<>();
		for (Integer maCtsp : ids) {
			Optional<BienTheSanPham> bOpt = bienTheRepo.findById(maCtsp);
			if (!bOpt.isPresent())
				continue; // bỏ qua id không tồn tại

			BienTheSanPham bt = bOpt.get();

			BienTheGiamGiaSPId pk = new BienTheGiamGiaSPId();
			pk.setMaCTSP(bt.getMaCTSP());
			pk.setMaGiamGia(id);

			BienThe_GiamGiaSP mapping = new BienThe_GiamGiaSP();
			mapping.setId(pk);
			mapping.setBienTheSanPham(bt);
			mapping.setGiamGiaSP(giamGia);

			toSave.add(mapping);
		}

		if (!toSave.isEmpty()) {
			btggRepo.saveAll(toSave);
		}

		return ResponseEntity.ok(Collections.singletonMap("ok", true));
	}

	public static class VariantDto {
		public Integer maCTSP;
		public String phienBan;
		public String moTaChiTiet;
		public BigDecimal gia;
		public boolean selected;
		public String productName; // <-- thêm trường này

		public VariantDto(Integer maCTSP, String phienBan, BigDecimal gia, boolean selected,
				String productName) {
			this.maCTSP = maCTSP;
			this.phienBan = phienBan;
			this.gia = gia;
			this.selected = selected;
			this.productName = productName;
		}
	}

	/**
	 * Trả về danh sách tất cả biến thể kèm flag "selected" (true nếu biến thể đã
	 * được gắn với giảm giá id)
	 */
	@GetMapping("/{id}/variants")
	public ResponseEntity<List<VariantDto>> getVariantsForDiscount(@PathVariable Integer id) {
		if (!giamGiaRepo.existsById(id)) {
			return ResponseEntity.notFound().build();
		}

		List<Integer> attached = btggRepo.findMaCtspByGiamGiaId(id);
		Set<Integer> attachedSet = new HashSet<>(attached != null ? attached : Collections.emptyList());

		List<BienTheSanPham> all = bienTheRepo.findAll();

		List<VariantDto> dto = all.stream().map(v -> {
			String productName = "";
			try {
				if (v.getSanPham() != null && v.getSanPham().getTenSP() != null) {
					productName = v.getSanPham().getTenSP();
				}
			} catch (Exception ignore) {
			}
			return new VariantDto(v.getMaCTSP(), v.getPhienBan(),  v.getGia(),
					attachedSet.contains(v.getMaCTSP()), productName);
		}).collect(Collectors.toList());

		return ResponseEntity.ok(dto);
	}

	// ---------- Admin utility: trigger status recalculation ----------
	@PostMapping("/recalc-status")
	public ResponseEntity<?> recalcStatus() {
		giamGiaService.capNhatTrangThaiGiamGia();
		return ResponseEntity.ok(Collections.singletonMap("ok", true));
	}

}
