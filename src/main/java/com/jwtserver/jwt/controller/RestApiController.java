package com.jwtserver.jwt.controller;

import com.jwtserver.jwt.model.User;
import com.jwtserver.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class RestApiController {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    @GetMapping("home")
    public String home() {
        return "<h1>home</h1>";
    }

    @PostMapping("token")
    public String token() {
        return "<h1>token</h1>";
    }

    @PostMapping("join")
    public String join(@RequestBody User user){
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles("ROLE_USER");
        userRepository.save(user);
        return "회원가입 완료";
    }
    //user권한 이상 접근 가능
    @GetMapping("/api/v1/user")
    public String user(){
        return "user";
    }
    //manager권한 이상 접근 가능
    @GetMapping("/api/v1/manager")
    public String manager(){
        return "manager";
    }
    //admin권한 이상 접근 가능
    @GetMapping("/api/v1/admin")
    public String admin(){
        return "admin";
    }


}
