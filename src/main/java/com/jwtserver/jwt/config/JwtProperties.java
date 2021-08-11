package com.jwtserver.jwt.config;

public interface JwtProperties {
    public static String ACCESS_SECRET = "jwt_access_secret";
    public static String REFRESH_SECRET = "jwt_refresh_secret";
    public static String SUBJECT = "LEETOKEN";
    public static long ACCESS_EXPIRED_TIME = 60*10;//10분
    public static long REFRESH_EXPIRED_TIME = 60*10*6*24*14;//2주
}
