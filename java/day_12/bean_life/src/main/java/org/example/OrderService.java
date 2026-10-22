package org.example;

import org.springframework.stereotype.Component;

@Component
public class OrderService {
    private PaymentService PaymentService;

    public OrderService(PaymentService paymentService) {
        PaymentService = paymentService;
    }
    public void placeorder(){
        PaymentService.pay();
        System.out.println("ordered");
    }
}
