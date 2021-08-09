package com.jwtserver.jwt.config.auth;

import com.jwtserver.jwt.model.User;
import com.jwtserver.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//http://localhost:8080/login 으로 오면 동작한다. -> 그런데 지금은 loginForm().disable()을 해놔서 동작안한다.
@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("PrincipalDetailsService  loadUserByUsername");
        User user = userRepository.findByUsername(username);
        return new PrincipalDetails(user);
    }
}
