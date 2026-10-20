package org.example;

import org.example.payment.cardpay;
import org.example.payment.payment;
import org.example.payment.upipay;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("org.example")
public class AppConfig {
    @Bean
    public user createuser(){
        return new user("viven",20);
    }

    @Bean
    public payment p(){
        return new cardpay();
    }
    @Bean
    public orderservice order(payment p){
        return new orderservice(p);
    }
}
