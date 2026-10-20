package org.example.payment;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

//@Component
//@Qualifier("cp")
public class cardpay implements payment{
    @Override
    public void pay(){
        System.out.println("Paying via card");
    }
}
