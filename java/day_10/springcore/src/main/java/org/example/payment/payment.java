package org.example.payment;

import org.example.orderservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public interface payment {
    public void pay();
}
