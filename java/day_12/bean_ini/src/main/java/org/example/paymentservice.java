package org.example;

import jdk.jfr.Label;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
//@Lazy
public class paymentservice {
    orderservice orderservice;

    public paymentservice(@Lazy orderservice orderservice) {
        this.orderservice=orderservice;
        System.out.println("payment service created");
    }
    public void pay(){
        System.out.println("Payment done");
        orderservice.ordersev();
    }

}
