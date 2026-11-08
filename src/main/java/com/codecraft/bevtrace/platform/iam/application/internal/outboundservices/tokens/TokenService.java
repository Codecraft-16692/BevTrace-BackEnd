package com.codecraft.bevtrace.platform.iam.application.internal.outboundservices.tokens;

import com.codecraft.bevtrace.platform.iam.domain.model.aggregates.User;

import java.util.Set;

/**
 * Port to the bearer token used by the API.
 */
public interface TokenService {

    String generateToken(User user, Set<String> roles);

    boolean validateToken(String token);

    String getEmailFromToken(String token);
}
