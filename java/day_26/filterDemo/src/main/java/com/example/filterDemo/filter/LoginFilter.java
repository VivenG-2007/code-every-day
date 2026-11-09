package com.example.filterDemo.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class LoginFilter implements Filter {


    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        long starttime=System.currentTimeMillis();

        HttpServletRequest httpServletRequest=(HttpServletRequest) request;
        HttpServletResponse httpServletResponse=(HttpServletResponse) response;

        String requestid= java.util.UUID.randomUUID().toString();
        httpServletResponse.setHeader("X-REQUEST-ID",requestid);



        System.out.println("Incoming Request : "
                +httpServletRequest.getMethod()
                +" "
                +httpServletRequest.getRequestURI());
        try{
            chain.doFilter(request,response);
        }finally{
            long duration=System.currentTimeMillis()-starttime;

            System.out.println("Response Status : "
                    +httpServletResponse.getStatus()
                    +" Duration: "+duration
            );

        }
    }
}
