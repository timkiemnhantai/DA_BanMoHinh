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
	        String avatarFolderPath = "uploads/avatar"; // hoặc đường dẫn tuyệt đối nếu muốn
	        File avatarFolder = new File(avatarFolderPath);
	        if (!avatarFolder.exists()) {
	            boolean created = avatarFolder.mkdirs();
	            if (created) {
	                System.out.println(">>> ✅ Đã tạo thư mục avatar tại: " + avatarFolder.getAbsolutePath());
	            } else {
	                System.err.println(">>> ❌ Không thể tạo thư mục avatar!");
	            }
	        } else {
	            System.out.println(">>> 📂 Thư mục avatar đã tồn tại: " + avatarFolder.getAbsolutePath());
	        }
	    }
}