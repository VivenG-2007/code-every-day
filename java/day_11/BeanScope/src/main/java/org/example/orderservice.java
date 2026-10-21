package org.example;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
//@Scope("prototype") //does lazy initialization
@Scope("singleton")  //eager initialization
public class orderservice {
    public orderservice() {
        System.out.println("Order service created");
    }
}
