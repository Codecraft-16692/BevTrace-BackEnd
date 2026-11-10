package com.codecraft.bevtrace.platform.iam.infrastructure.authorization.sfs.model;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

/**
 * Builds the authentication of a request from the user details loaded for its token.
 */
public final class UsernamePasswordAuthenticationTokenBuilder {

    private UsernamePasswordAuthenticationTokenBuilder() {
    }

    public static UsernamePasswordAuthenticationToken build(UserDetailsImpl principal, HttpServletRequest request) {
        var authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authentication;
    }
}
