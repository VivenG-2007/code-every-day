package org.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(appconfig.class);
//        OrderService o=context.getBean(OrderService.class);
//        o.placeorder();
            cartservice c=context.getBean(cartservice.class);
            System.out.println(c.getval(10));
            context.close();
    }
}
