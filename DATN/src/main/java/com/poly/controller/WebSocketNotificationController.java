package com.poly.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebSocketNotificationController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void guiThongBaoDonHang(int maTK, String noiDung, int maThongBao, String url) {
        guiThongBaoDonHang(maTK, noiDung, maThongBao, url, null);
    }
    
    public void guiThongBaoDonHang(int maTK, String noiDung, int maThongBao, String url, String token) {
        // 2. Đóng gói dữ liệu gửi qua WebSocket
        Map<String, Object> data = new HashMap<>();
        data.put("noiDung", noiDung);
        data.put("maThongBao", maThongBao);
        data.put("url", url);
        data.put("token", token); // Dùng token truyền từ ngoài vào

        // 3. Gửi tới client
        messagingTemplate.convertAndSend("/topic/thong-bao/" + maTK, data);
    }

}


