package org.example;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class cartservice implements InitializingBean {
    Map<Integer,String> mp;

    public cartservice() {
        mp=new HashMap<>();
        System.out.println("cart const called");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("Initing");
        mp.put(10,"vivu");
        mp.put(20,"sanju");
    }

    public void addtocart(){
        System.out.println("Added to cart");
    }

    public String getval(int key){
        return mp.get(key);
    }

}
