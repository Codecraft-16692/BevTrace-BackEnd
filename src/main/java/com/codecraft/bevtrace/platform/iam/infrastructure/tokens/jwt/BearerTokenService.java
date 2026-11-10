package com.codecraft.bevtrace.platform.iam.infrastructure.tokens.jwt;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

/**
 * Extracts the bearer token of a request.
 */
@Service
public class BearerTokenService {

    private static final String PREFIX = "Bearer ";

    /**
     * @param request incoming request
     * @return the token, or null when the request carries no bearer token
     */
    public String extractToken(HttpServletRequest request) {
        var header = request.getHeader(HttpHeaders.AUTHORIZATION);
        return header != null && header.startsWith(PREFIX) ? header.substring(PREFIX.length()) : null;
    }
}
