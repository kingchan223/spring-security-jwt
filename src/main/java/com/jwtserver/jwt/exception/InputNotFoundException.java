package com.jwtserver.jwt.exception;

public class InputNotFoundException extends RuntimeException{

    public InputNotFoundException(String message) {
        super(message);
    }
}
