package com.example.demo.components;

import com.sun.security.auth.UserPrincipal;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class UserAuditAware implements AuditorAware<String> {
    private static final String SYSTEM_USER = "SYSTEM";

    @Override
    public Optional<String> getCurrentAuditor() {
        var authentication = SecurityContextHolder.getContext()
                                                  .getAuthentication();
        var userId = ((UserPrincipal) authentication.getPrincipal()).getName();
        return Optional.of(userId == null ? SYSTEM_USER : userId);
    }
}
