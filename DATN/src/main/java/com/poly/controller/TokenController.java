package com.poly.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poly.service.OrderTokenService;

@RestController
public class TokenController {
    @Autowired
    private OrderTokenService tokenService;

    @GetMapping("/verify-token")
    public ResponseEntity<Void> verifyToken(
            @RequestParam String token,
            @RequestParam int maDH) {
        boolean valid = tokenService.validateToken(token, maDH);
        if (valid) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

}
