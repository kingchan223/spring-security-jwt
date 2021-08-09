package com.jwtserver.jwt.repository;

import com.jwtserver.jwt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {

    // select * from user where username = 1?
    public User findByUsername(String username);

}
