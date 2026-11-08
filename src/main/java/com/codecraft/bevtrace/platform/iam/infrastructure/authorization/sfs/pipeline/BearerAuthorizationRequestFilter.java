package com.codecraft.bevtrace.platform.iam.infrastructure.authorization.sfs.pipeline;

import com.codecraft.bevtrace.platform.iam.application.internal.outboundservices.tokens.TokenService;
import com.codecraft.bevtrace.platform.iam.infrastructure.authorization.sfs.model.UsernamePasswordAuthenticationTokenBuilder;
import com.codecraft.bevtrace.platform.iam.infrastructure.authorization.sfs.services.UserDetailsServiceImpl;
import com.codecraft.bevtrace.platform.iam.infrastructure.tokens.jwt.BearerTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Authenticates a request from its bearer token. Requests without a valid token continue anonymous and
 * are rejected later by the authorization rules.
 */
public class BearerAuthorizationRequestFilter extends OncePerRequestFilter {

    private final BearerTokenService bearerTokens;
    private final TokenService tokens;
    private final UserDetailsServiceImpl userDetails;

    public BearerAuthorizationRequestFilter(BearerTokenService bearerTokens, TokenService tokens,
                                            UserDetailsServiceImpl userDetails) {
        this.bearerTokens = bearerTokens;
        this.tokens = tokens;
        this.userDetails = userDetails;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        var token = bearerTokens.extractToken(request);
        if (token != null && tokens.validateToken(token)) {
            try {
                var principal = userDetails.loadUserByUsername(tokens.getEmailFromToken(token));
                if (principal.isEnabled()) {
                    SecurityContextHolder.getContext()
                            .setAuthentication(UsernamePasswordAuthenticationTokenBuilder.build(principal, request));
                }
            } catch (UsernameNotFoundException ex) {
                logger.debug("Token of an unknown user ignored");
            }
        }
        chain.doFilter(request, response);
    }
}
