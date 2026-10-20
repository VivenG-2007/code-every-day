package org.example.payment;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

//@Component
//@Qualifier("up")
public class upipay implements payment{

    @Override
    public void pay() {
        System.out.println("Payed via upi");
    }
}
