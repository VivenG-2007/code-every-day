package com.example.demo;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
public class DummyNotificationService implements NotificatioService{


    @Override
    public String send() {
        return "Dummy notification";
    }
}
