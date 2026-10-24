package org.example;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
@Component
public class OrderService {
    private PaymentService paymentService;

    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    public void order(){
        paymentService.pay();
        System.out.println("Order placed");
    }
}
