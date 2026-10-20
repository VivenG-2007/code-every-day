package org.example;

import org.example.payment.payment;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
public class Main {
    static void main() {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        orderservice s = context.getBean(orderservice.class);
        s.placeorder();
        user u=context.getBean(user.class);
        System.out.println(u.getName());
    }
}
