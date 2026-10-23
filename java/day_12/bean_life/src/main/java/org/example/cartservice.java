package org.example;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class cartservice implements BeanNameAware, ApplicationContextAware/*, DisposableBean*/ {
    Map<Integer,String> mp;

    public cartservice() {
        mp=new HashMap<>();
        System.out.println("cart const called");
    }
    @Override
    public void setBeanName(String name) {
        System.out.println("Bean name "+name);
    }
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("Bean name "+applicationContext.getClass());
    }
//    @Override
    @PostConstruct
    public void afterPropertiesSet() {
        System.out.println("Initing");
        mp.put(10,"vivu");
        mp.put(20,"sanju");
    }
    @PreDestroy
    public void stop(){
        mp.clear();
        System.out.println("bean is destorying");
    }

    public void addtocart(){
        System.out.println("Added to cart");
    }

    public String getval(int key){
        return mp.get(key);
    }


//    @Override
//    public void destroy() throws Exception {
//        mp.clear();
//        System.out.println("bean is destorying");
//    }

//    public void start(){
//        System.out.println("Bean starting");
//        mp.put(10,"vivu");
//        mp.put(20,"sanju");
//    }
//    public void stop(){
//        mp.clear();
//       System.out.println("bean is destorying");
//
//    }
}
