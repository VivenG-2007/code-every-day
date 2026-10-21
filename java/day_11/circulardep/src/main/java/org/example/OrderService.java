package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderService {
    @Autowired
    private PaymentService p;

//    @Autowired
//    public OrderService(PaymentService p) {
//        this.p = p;
//    }
    public void placeorder(){
        System.out.println("Oder placed");
        p.pay();
        ordersetails();
    }
    public void ordersetails(){
        System.out.println("Oder details is as folows");
    }

}
