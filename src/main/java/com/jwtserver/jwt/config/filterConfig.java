package com.jwtserver.jwt.config;

import com.jwtserver.jwt.filter.MyFilter1;
import com.jwtserver.jwt.filter.MyFilter2;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class filterConfig{

    @Bean
    public FilterRegistrationBean<MyFilter1> filter1(){
        FilterRegistrationBean<MyFilter1> myFilter1 = new FilterRegistrationBean<>(new MyFilter1());
        myFilter1.addUrlPatterns("/*");
        myFilter1.setOrder(1);
        return myFilter1;
    }

    @Bean
    public FilterRegistrationBean<MyFilter2> filter2(){
        FilterRegistrationBean<MyFilter2> myFilter2 = new FilterRegistrationBean<>(new MyFilter2());
        myFilter2.addUrlPatterns("/*");
        myFilter2.setOrder(0);
        return myFilter2;
    }
}
