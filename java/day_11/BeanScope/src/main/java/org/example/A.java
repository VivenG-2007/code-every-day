package org.example;

import org.springframework.stereotype.Component;

@Component
public class A {
    private orderservice order;

    public A(orderservice order) {
        this.order = order;
    }
}
