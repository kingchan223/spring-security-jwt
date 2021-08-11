package com.jwtserver.jwt.config.jwtFilter;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
/* 이거 모르겠음 */
//AuthenticationManager는 전달받은 UsernamePasswordToken을 순차적으로 AuthenticaionProvider들에게 전달
public class JwtAutheticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}
