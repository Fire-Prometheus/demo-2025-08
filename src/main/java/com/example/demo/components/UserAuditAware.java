package com.example.demo.components;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class UserAuditAware implements AuditorAware<String> {
    private static final String SYSTEM_USER = "SYSTEM";

    @Override
    public Optional<String> getCurrentAuditor() {
//        return Optional.of().or("SYSTEM");
        return Optional.of(SYSTEM_USER);
    }
}
