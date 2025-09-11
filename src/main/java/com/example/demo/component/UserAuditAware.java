package com.example.demo.component;

import com.example.demo.util.JwtUtil;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class UserAuditAware implements AuditorAware<String> {
    private static final String SYSTEM_USER = "SYSTEM";

    @Override
    public Optional<String> getCurrentAuditor() {
        var userId = JwtUtil.getUserId();
        return Optional.of(userId == null ? SYSTEM_USER : userId);
    }
}
