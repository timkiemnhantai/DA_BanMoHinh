package com.poly.util;

import com.poly.service.GiamGiaSPService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupTasks {

    private final GiamGiaSPService giamGiaSPService;

    public StartupTasks(GiamGiaSPService giamGiaSPService) {
        this.giamGiaSPService = giamGiaSPService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void capNhatTrangThaiGiamGiaSauKhiChay() {
        giamGiaSPService.capNhatTrangThaiGiamGia();
        System.out.println(">>> ✅ Đã cập nhật trạng thái giảm giá khi khởi động.");
    }
}