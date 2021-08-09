package com.jwtserver.jwt.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter3 implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("filter3");

        HttpServletRequest req= (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // 토큰 이름: 찰리
        // 토큰을 만들어줘야함. id, pw이 정상저긍로 들어와서 로그인이 완료되면 토큰을 만들어서 응답으로 보낸다.
        // 클라이언트는 요청마다 헤더에 authorization:토큰을 가죠온다.
        // 그때 토큰이 넘어오면 이 토큰이 내가 만든 토큰인지 검증한다.(RSA, HS256)
        if(req.getMethod().equals("POST")){
            String headerAuth = req.getHeader("Authorization");
            System.out.println("headerAuth = " + headerAuth);
            if(headerAuth.equals("chicha")){
                chain.doFilter(request, response);
            }
            PrintWriter writer = res.getWriter();
            writer.println("인증 안됨");
        }
    }
}
