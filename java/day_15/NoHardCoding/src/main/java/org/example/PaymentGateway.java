package org.example;

import org.springframework.stereotype.Component;

@Component
public class PaymentGateway {
//    @Value("${PaymentGateway.type:Viven}")
//    private String type;
//
//    @Value("${PaymentGateway.retryCount:8}")
//    private  int retryCount;


    // this is constructor injection
//    public PaymentGateway(@Value("${PaymentGateway.type}") String type,@Value("${PaymentGateway.retryCount}") int retryCount) {
//        this.type = type;
//        this.retryCount = retryCount;
//    }
    private PaymentProperties paymentProperties;
    private PaymentGateway(PaymentProperties paymentProperties){
        this.paymentProperties=paymentProperties;
    }

    public String getType(){
        return paymentProperties.getType();
    }

    public int getRetry(){
        return paymentProperties.getRetry();
    }
    public boolean getIsenable(){
        return paymentProperties.isIsenable();
    }
    public int getTimeout(){
        return paymentProperties.getTimeout();
    }
    public void print(){
        System.out.println(getType());
        System.out.println(getIsenable());
        System.out.println(getRetry());
        System.out.println(getTimeout());

    }
}


//@value