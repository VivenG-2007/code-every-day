package org.example;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
//@Lazy
public class orderservice {
    paymentservice paymentservice;
    public orderservice(@Lazy paymentservice paymentservice) {
        this.paymentservice=paymentservice;
        System.out.println("Oderservice creted");
    }
    public void order(){
        paymentservice.pay();
        System.out.println("Order places");
    }
    public void ordersev(){
        System.out.println("ORdering");
    }
}
