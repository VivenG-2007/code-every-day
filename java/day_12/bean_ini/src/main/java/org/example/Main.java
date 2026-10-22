package org.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
public class Main {
    static void main() {
        ApplicationContext context=new AnnotationConfigApplicationContext(appconfig.class);
//        paymentservice pay=context.getBean(paymentservice.class);
        orderservice o=context.getBean(orderservice.class);
        o.order();
    }
}
