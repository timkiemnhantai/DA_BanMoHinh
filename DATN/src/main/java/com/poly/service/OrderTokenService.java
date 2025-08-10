package com.poly.service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class OrderTokenService {

    // Lưu token: "token:maThongBao" -> thời gian hết hạn (millis)
    private final Map<String, Long> tokenStore = new ConcurrentHashMap<>();

    // Tạo token mới với thời hạn expirySeconds (giây)
    public String createToken(int maThongBao, long expirySeconds) {
        String token = UUID.randomUUID().toString();
        long expireAt = System.currentTimeMillis() + expirySeconds * 1000L;
        tokenStore.put(token + ":" + maThongBao, expireAt);
        return token;
    }

    // Kiểm tra token hợp lệ, nếu hợp lệ thì xóa (chỉ dùng 1 lần)
    public boolean validateToken(String token, int maThongBao) {
        String key = token + ":" + maThongBao;
        Long expireAt = tokenStore.get(key);
        if (expireAt != null && expireAt > System.currentTimeMillis()) {
            tokenStore.remove(key); // Xóa để token không tái sử dụng
            return true;
        }
        return false;
    }
}
