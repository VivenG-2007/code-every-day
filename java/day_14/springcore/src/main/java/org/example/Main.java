package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication //(scanBasePackages = "org")
public class Main {
    public static void main(String[] args) {
        ApplicationContext context =
                SpringApplication.run(Main.class, args);

        OrderService service = context.getBean(OrderService.class);
        service.order();
    }


//    @Bean
//    public UserService getUserService(){
//        return new UserService();
//    }
}