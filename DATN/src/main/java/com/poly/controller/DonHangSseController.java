package com.poly.controller;


import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.poly.dto.DonHangDTO;

import java.io.IOException;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/don-hang")
public class DonHangSseController {

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @GetMapping("/stream")
    public SseEmitter streamOrders() {
        SseEmitter emitter = new SseEmitter(0L); // vô hạn
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError((e) -> emitters.remove(emitter));

        return emitter;
    }

    public void sendNewOrder(DonHangDTO dto) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                    .name("new-order")
                    .data(dto));
            } catch (IOException e) {
                emitters.remove(emitter);
            }
        }
    }
}
