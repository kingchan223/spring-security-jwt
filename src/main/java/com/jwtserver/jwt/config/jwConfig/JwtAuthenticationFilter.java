package com.jwtserver.jwt.config.jwConfig;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter가 있음.
// ㅅlogin 요청해서 username, password 전송하면 (post)
// UsernamePasswordAuthenticationFilter가 동작
// 근데 현재 loginform.disable때문에 동작안함. 그래서 요 필터를 security에 직접 등록해줘야한다.

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    // '/login'요청을 하면 로그인 시도를 위해 실행되는 함수
    // 이제 여기서 id, pw을 확인하고 정상인지 로그인 시도를 해본다. 이때 authenticationManager로 로그인 시도를 하는데, 시도하면 PrincipalDetailsService가 호출된다.
    // -> 그러면 loadUserByUsername이 실행된다.
    // -> PrincipalDetails를 세션에 담고 (권한 관리를 위해서. 권환관리를 안한다면 굳이 안 담아도된다.)
    // -> JWT토큰을 만들어서 응답한다.
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        System.out.println("로그인 시도중");
        return super.attemptAuthentication(request, response);
    }
}
