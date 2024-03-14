package org.example.config;

import jakarta.servlet.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class CORSConfig implements Filter{
    private final String[] allowedDomain = {"http://localhost:8081"};


    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        Set<String> allowedOrigins= new HashSet<>(Arrays.asList(allowedDomain));
        String origin=httpRequest.getHeader("Origin");
        if (origin == null) {
            chain.doFilter(request, response);
            return;
        }
        if (allowedOrigins.contains(origin)){
            httpResponse.setHeader("Access-Control-Allow-Origin", origin);
            httpResponse.setContentType("application/json;charset=UTF-8");
            httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
            httpResponse.setHeader("Access-Control-Max-Age", "3600");
            httpResponse.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With, userId, token, ut");//表明服务器支持的所有头信息字段
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true"); //如果要把Cookie发到服务器，需要指定Access-Control-Allow-Credentials字段为true;
            httpResponse.setHeader("XDomainRequestAllowed","1");
        }
        chain.doFilter(request, response);
    }
}
