package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentService {
        @Autowired
      OrderService o;
//
//    public PaymentService(OrderService o) {
//        this.o = o;
//    }


    public void pay(){
        System.out.println("Payment done");
        //not its responsbility
        //o.ordersetails();
    }
}
