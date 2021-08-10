package com.jwtserver.jwt.config;

public interface JwtProperties {
    public static String SECRET = "secer_ID_anything";
    public static String SUBJECT = "LEETOKEN";
    public static int EXPIRED_TIME = 60000 * 10;
}
