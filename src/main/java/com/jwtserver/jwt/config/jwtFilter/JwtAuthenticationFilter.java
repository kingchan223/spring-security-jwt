package com.jwtserver.jwt.config.jwtFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwtserver.jwt.config.JwtProperties;
import com.jwtserver.jwt.config.auth.PrincipalDetails;
import com.jwtserver.jwt.exception.InputNotFoundException;
import com.jwtserver.jwt.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Date;

// 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter가 있음.
// login 요청해서 username, password 전송하면 (post)
// UsernamePasswordAuthenticationFilter가 동작
// 근데 현재 loginform.disable때문에 동작안함. 그래서 요 필터를 security에 직접 등록해줘야한다.
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private ObjectMapper om = new ObjectMapper();
    // '/login'요청을 하면 로그인 시도를 위해 실행되는 함수
    // 이제 여기서 id, pw을 확인하고 정상인지 로그인 시도를 해본다. 이때 authenticationManager로 로그인 시도를 하는데, 시도하면 PrincipalDetailsService가 호출된다.
    // -> 그러면 loadUserByUsername이 실행된다.
    // -> PrincipalDetails를 세션에 담고 (권한 관리를 위해서. 권환관리를 안한다면 굳이 안 담아도된다.)
    // -> JWT토큰을 만들어서 응답한다.
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("로그인 시도중");

        //json으로 받은 사용자의 username, password를 user객체로 바꾼다.
//            User user = om.readValue(request.getInputStream(), User.class);
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        System.out.println("user = " + user.getUsername());
        System.out.println("user = " + user.getPassword());

        //인증 전의 객체 생성
        if(user==null || (user.getUsername()==null || user.getUsername().equals("")) || (user.getPassword()==null || user.getPassword().equals(""))){
            return null;
        }
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());//인증토큰 만들기
        //PrincipalDetailsService의 loadUserByUsername() 함수가 실행된 후 정상이면 authentication객체가 반환된다.
        // -> 즉 authentication객체가 반환된다는 것은 DB에 있는 username, password가 일치한다는 것이다.
        // + ( 아래 PrincipalDetailsService의 실행 결과로 반환된 PrincipalDetails객체가 authentication에 담긴다.)
        // 아래 authenticationManager.athenticate(authenticationToken)으로 인해 PrincipalDetailsService의  loadUserByUsername가 호출된다. 로그인을 하여 Authentication객체 만들기
        Authentication authentication = authenticationManager.authenticate(authenticationToken);//인증토큰을 통한 로그인 시도
        //            PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
//            System.out.println("로그인 완료됨: "+principalDetails.getUser().getUsername());
        return authentication;//---> 반환하면 세션에 authentication 객체가 저장된다.
        // 이걸 굳이 반환해서 리턴하는 이유는 권한 관리를 security가 해줘서 편하기 떄문이다.
        // 원래 JWT토큰을 사용하면서 세션을 만들필요가 없지만 우리는 권한 관리의 편의성 때매 세션을 사용중이다.
    }

    // attemptAuthentication 실행 후 인증이 정상적으로 되었으면 아래의 successfulAuthentication 메서드 실행
    // 여기서 JWT 토큰을 만들어서 request한 사용자에게 JWT토큰을 response해주면 된다.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {

        System.out.println("successfulAuthentication실행. 그믄큼 인증되었다느 소리");

        //Hash암호화 방식
        PrincipalDetails principalDetails = (PrincipalDetails)authResult.getPrincipal();
        //user정보를 사용하여 jwt토큰 만들기
        String jwtAccessToken = JWT.create()
                .withSubject(JwtProperties.SUBJECT)//아무거나 넣어도 상관없음
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRED_TIME))//토큰 만료시간
                .withClaim("id", principalDetails.getUser().getId())
                .withClaim("username", principalDetails.getUser().getUsername())
                .withClaim("role", principalDetails.getUser().getRoles())
                .sign(Algorithm.HMAC512(JwtProperties.ACCESS_SECRET));//

        String jwtRefreshToken = JWT.create()
                .withSubject(JwtProperties.SUBJECT)//아무거나 넣어도 상관없음
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.REFRESH_EXPIRED_TIME))//토큰 만료시간
                .withClaim("id", principalDetails.getUser().getId())
                .withClaim("username", principalDetails.getUser().getUsername())
                .withClaim("role", principalDetails.getUser().getRoles())
                .sign(Algorithm.HMAC512(JwtProperties.REFRESH_SECRET));

        //response.addHeader("Authorization", "Bearer "+jwtAccessToken);//클라이언트에게 JWT토큰을 보내주기 -> 이제 필터를 추가하여 이 토큰이 유효한지 검사
        ResponseCookie accessTokenCookie = ResponseCookie.from("myserverAccessCookie", jwtAccessToken)
                .httpOnly(true)
                .sameSite("Lax")
                .maxAge(JwtProperties.ACCESS_EXPIRED_TIME)
                .path("/")
                .build();
        response.addHeader("Set-Cookie", accessTokenCookie.toString());

        ResponseCookie refreshTokenCookie = ResponseCookie.from("myserverRefreshCookie", jwtRefreshToken)
                .httpOnly(true)
                .sameSite("Lax")
                .maxAge(JwtProperties.REFRESH_EXPIRED_TIME)
                .path("/")
                .build();
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
    }
}
