package com.jwtserver.jwt.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String username){
        super("'"+username+"' 으로 가입된 회원을 찾을 수 없습니다."); }
}

