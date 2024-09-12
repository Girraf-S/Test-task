package com.modsen.nnewsservice.service;

import org.springframework.security.core.Authentication;

import java.util.Map;

public class AuthUtil {
    public static String extractClaimStringValue(final Authentication authentication, final String claimName) {

        if (authentication != null && authentication.getDetails() instanceof Map) {
            final Object value = ((Map<?, ?>) authentication.getDetails()).get(claimName);
            if (value != null) {
                return value.toString();
            }
        }
        throw new RuntimeException("No claim with name " + claimName);
    }
}
