package org.example;

import org.example.payment.payment;
import org.springframework.beans.factory.annotation.Autowired;

//@Component
public class orderservice {
    payment p;
    @Autowired
    public orderservice(payment p) {
        this.p = p;
    }

    void placeorder(){
        System.out.println("Order placed" );
        p.pay();
    }
}
