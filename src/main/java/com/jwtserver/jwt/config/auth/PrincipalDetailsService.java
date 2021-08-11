package com.jwtserver.jwt.config.auth;

import com.jwtserver.jwt.exception.UserNotFoundException;
import com.jwtserver.jwt.model.User;
import com.jwtserver.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//http://localhost:8080/login 으로 오면 동작한다. -> 그런데 지금은 loginForm().disable()을 해놔서 동작안한다.
@Slf4j
@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("PrincipalDetailsService  loadUserByUsername 호출!");
        log.info("username={}", username);
        try{
            User user = userRepository.findByUsername(username);
            log.info("username={}", user.getUsername());

            return new PrincipalDetails(user);
        }catch(Exception e){
            throw new UserNotFoundException(username);
        }
    }
}
