package com.jwtserver.jwt.config.jwtFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.jwtserver.jwt.config.JwtProperties;
import com.jwtserver.jwt.config.auth.PrincipalDetails;
import com.jwtserver.jwt.model.User;
import com.jwtserver.jwt.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//시큐리티가 filter를 가지고 있는데 그 필터 중 BAsicAuthenticationFilter가 있다.
//권한이나 인증이 필요한 특정 주소를 요청했을 시 위 필터를 타게된다.
//만약 권한이나 인증이 필요한 주소가 아니면 위 필터를 건너뛴다.
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    //인증이나 권한이 필요한 주소요청이 있을 때, 해당 필터를 타게 된다.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//        super.doFilterInternal(request, response, chain);
        System.out.println("BasicAuthenticationFilter에서 인증, 권한 요청을 확인");
        String jwtheader = request.getHeader("Authorization");
        System.out.println("jwtheader = " + jwtheader);

        //header가 있는지 확인
        if(jwtheader == null || !jwtheader.startsWith("Bearer")){
            chain.doFilter(request, response);
            return;
        }
        //jwt토큰을 검증하여 정상적인 사용자인지 확인하기
        String jwtToken = jwtheader.replace("Bearer ", "");
        String username =
                JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build()
                        .verify(jwtToken)
                        .getClaim("username")
                        .asString();
        //서명 정상적으로 됨
        if(username!=null){
            User userEntity = userRepository.findByUsername(username);

            PrincipalDetails principalDetails = new PrincipalDetails(userEntity);

            //Jwt 토큰 서명을 통해 서명이 정상이면 Authentication객체 강제로 만들기.// 인증 완료 후의 객체생성
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails,
                    null, principalDetails.getAuthorities());
            // SecurityContextHolder.getContext(): 세션공간 찾고, setAuthentication(authentication):authentication객체 넣기
            SecurityContextHolder.getContext().setAuthentication(authentication);

            chain.doFilter(request, response);
        }
    }
}
