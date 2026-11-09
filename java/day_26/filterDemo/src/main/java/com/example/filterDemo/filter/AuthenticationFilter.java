package com.example.filterDemo.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest=(HttpServletRequest) request;
        HttpServletResponse httpServletResponse=(HttpServletResponse) response;
        String token=httpServletRequest.getHeader("token");
        String apikey=httpServletRequest.getHeader("X-API-KEY");
        if(token==null||!token.equals("12345")){
            httpServletResponse.setStatus(401);
            return;
        }
        if(apikey==null||!apikey.equals("secret123")){
            httpServletResponse.setStatus(401);
            httpServletResponse.setContentType("application/json");
            httpServletResponse.getWriter().write(
                    "{\n" +
                            "    \"message\":\"Invalid api key\"\n" +
                            "}"
            );
            return;
        }
        chain.doFilter(request,response);
    }
}
