package com.poly.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.poly.model.ThongBao;
import com.poly.service.ThongBaoService;

@RestController
@RequestMapping("/api/thong-bao")
public class ThongBaoController {

    private final ThongBaoService thongBaoService;
    public ThongBaoController(ThongBaoService thongBaoService) {
        this.thongBaoService = thongBaoService;
    }

    // Lấy latest notifications + unread count
    @GetMapping("/latest")
    public ResponseEntity<Map<String, Object>> latest(@RequestParam Integer maTK, @RequestParam(defaultValue = "10") int limit) {
        List<ThongBao> items = thongBaoService.layThongBaoMoiNhat(maTK, limit);
        long unread = thongBaoService.demThongBaoChuaDoc(maTK);
        Map<String,Object> res = new HashMap<>();
        res.put("unread", unread);
        res.put("items", items);
        return ResponseEntity.ok(res);
    }

    // Đánh dấu một thông báo đã đọc
    @PostMapping("/{id}/read")
    public ResponseEntity<Void> markRead(@PathVariable Integer id) {
        thongBaoService.danhDauDaDoc(id);
        return ResponseEntity.noContent().build();
    }

    // Đánh dấu tất cả đã đọc cho user
    @PostMapping("/mark-all-read")
    public ResponseEntity<Void> markAllRead(@RequestParam Integer maTK) {
        thongBaoService.danhDauTatCaDaDoc(maTK);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOne(@PathVariable Integer id) {
        thongBaoService.xoaThongBao(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-all")
    public ResponseEntity<Void> deleteAll(@RequestParam Integer maTK) {
        thongBaoService.xoaTatCaThongBaoCuaUser(maTK);
        return ResponseEntity.noContent().build();
    }
}
