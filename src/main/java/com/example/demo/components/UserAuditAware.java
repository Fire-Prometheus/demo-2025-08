package com.example.demo.components;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class UserAuditAware implements AuditorAware<String> {
    private static final String SYSTEM_USER = "SYSTEM";

    @Override
    public Optional<String> getCurrentAuditor() {
        var authentication = SecurityContextHolder.getContext()
                                                  .getAuthentication();
        var credentials = ((String) authentication.getCredentials());
        return Optional.of(credentials == null ? SYSTEM_USER : credentials);
    }
}
