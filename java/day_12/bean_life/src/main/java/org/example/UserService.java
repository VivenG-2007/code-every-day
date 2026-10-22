package org.example;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.stereotype.Component;

@Component
public class UserService implements BeanNameAware {
    public UserService() {
        System.out.println("user contructor called");
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("User name is "+name);
    }
}
