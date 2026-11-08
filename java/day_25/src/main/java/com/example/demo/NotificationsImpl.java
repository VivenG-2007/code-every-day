package com.example.demo;


import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("prod")
public class NotificationsImpl implements NotificatioService{

    @Override
    public String send() {
        //real notificatio part

        return "Notification send";
    }
}
