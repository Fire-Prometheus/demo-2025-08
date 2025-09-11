package com.example.demo.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class JwtUtil {
    public static String getUserId(Jwt jwt) {
        return jwt.getId();
    }

    public static String getUserId() {
        var principal = (Jwt) SecurityContextHolder.getContext()
                                                   .getAuthentication()
                                                   .getPrincipal();
        return getUserId(principal);
    }
}
