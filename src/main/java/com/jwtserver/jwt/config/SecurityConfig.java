package com.jwtserver.jwt.config;

import com.jwtserver.jwt.config.jwConfig.JwtAuthenticationFilter;
import com.jwtserver.jwt.filter.MyFilter3;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.filter.CorsFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsFilter corsFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*일반적으로 시큐리티 필터가 서블릿 필터 보다 먼저 실행된다. 하여 특정 필터를 시큐리티 필터보다 먼저 쓰고 싶으면 아래처럼 등록해주면 된다.*/
//        http.addFilterBefore(시큐리티 보다 먼저 등록하고 싶은 내가 만든 필터, 시큐리티 필터 중 하나);
//        http.addFilterBefore(new MyFilter3(), SecurityContextPersistenceFilter.class);
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//세션을 사용하지 않습니다.
                .and()
                .addFilter(corsFilter)//cors요청이 와도 요 필터땜에 허용된다.//@CrossOrigin은 인증이 없으면 거부됨
                .formLogin().disable()//폼 로그인 비홠성화
                .httpBasic().disable()
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))//AuthenticationManager를 파라미터로 넣어줘야함
                .authorizeRequests()
                .antMatchers("/api/v1/user/**")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/manager/**")
                .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/admin/**")
                .access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll();
    }
}
