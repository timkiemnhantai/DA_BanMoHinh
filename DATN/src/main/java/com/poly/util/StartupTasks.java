package com.poly.util;

import com.poly.service.GiamGiaSPService;

import java.io.File;

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

        // Tạo thư mục avatar nếu chưa tồn tại
        createFolderIfNotExists("uploads/avatar");

        // Tạo thư mục reviews để lưu ảnh/video đánh giá
        createFolderIfNotExists("uploads/review");
        
        createFolderIfNotExists("uploads/baoloi");
    }

    private void createFolderIfNotExists(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists()) {
            boolean created = folder.mkdirs();
            if (created) {
                System.out.println(">>> ✅ Đã tạo thư mục tại: " + folder.getAbsolutePath());
            } else {
                System.err.println(">>> ❌ Không thể tạo thư mục: " + folder.getAbsolutePath());
            }
        } else {
            System.out.println(">>> 📂 Thư mục đã tồn tại: " + folder.getAbsolutePath());
        }
    }
}
