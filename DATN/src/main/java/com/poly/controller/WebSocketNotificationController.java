package com.poly.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
@Component
public class WebSocketNotificationController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void guiThongBaoDonHang(int maTK, String noiDung) {
        // Gửi thông báo đến client (ví dụ /topic/thong-bao/6)
        messagingTemplate.convertAndSend("/topic/thong-bao/" + maTK, noiDung);
    }
}
