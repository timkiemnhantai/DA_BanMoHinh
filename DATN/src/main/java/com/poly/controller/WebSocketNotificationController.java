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
        // Gửi thông báo đến client (ví dụ /topic/thong-bao/6)
    	Map<String, Object> data = new HashMap<>();
    	data.put("noiDung", noiDung);
    	data.put("maThongBao", maThongBao);
    	data.put("url", url);
        messagingTemplate.convertAndSend("/topic/thong-bao/" + maTK, data);
    }
}
