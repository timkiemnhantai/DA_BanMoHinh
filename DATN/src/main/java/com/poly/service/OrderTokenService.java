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

    // Kiểm tra token hợp lệ (còn hạn) -> KHÔNG xoá, cho phép dùng nhiều lần đến khi hết hạn
    public boolean validateToken(String token, int maThongBao) {
        String key = token + ":" + maThongBao;
        Long expireAt = tokenStore.get(key);

        if (expireAt != null) {
            if (expireAt > System.currentTimeMillis()) {
                return true; // còn hạn, hợp lệ
            } else {
                tokenStore.remove(key); // hết hạn thì xoá khỏi store
            }
        }
        return false;
    }

    // (tuỳ chọn) dọn dẹp token hết hạn bằng tay
    public void cleanupExpiredTokens() {
        long now = System.currentTimeMillis();
        tokenStore.entrySet().removeIf(entry -> entry.getValue() < now);
    }
}
