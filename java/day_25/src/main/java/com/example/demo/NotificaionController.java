package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notification")
public class NotificaionController {
    NotificatioService notificatioService;

    @Autowired
    public NotificaionController(NotificatioService notificatioService) {
        this.notificatioService = notificatioService;
    }

    @PostMapping
    public ResponseEntity<String> sendNotification(){
        String s=notificatioService.send();
        return ResponseEntity.ok(s);
    }
}
