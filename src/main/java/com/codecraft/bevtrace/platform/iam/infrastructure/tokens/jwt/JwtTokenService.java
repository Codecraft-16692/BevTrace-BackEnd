package com.codecraft.bevtrace.platform.iam.infrastructure.tokens.jwt;

import com.codecraft.bevtrace.platform.iam.application.internal.outboundservices.tokens.TokenService;
import com.codecraft.bevtrace.platform.iam.domain.model.aggregates.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Set;

/**
 * JWT (HS256) implementation of the token port. The token carries the e-mail as subject plus the
 * user id, name and roles, so the frontend can render the session without another call.
 */
@Service
public class JwtTokenService implements TokenService {

    private final String secret;
    private final Integer expirationDays;

    public JwtTokenService(@Value("${authorization.jwt.secret}") String secret,
                           @Value("${authorization.jwt.expiration.days}") Integer expirationDays) {
        this.secret = secret;
        this.expirationDays = expirationDays;
    }

    @Override
    public String generateToken(User user, Set<String> roles) {
        var now = Instant.now();
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId", user.getId())
                .claim("name", user.getName())
                .claim("roles", roles)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(expirationDays * 24L * 60L * 60L)))
                .signWith(signingKey())
                .compact();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            claims(token);
            return true;
        } catch (RuntimeException ex) {
            return false;
        }
    }

    @Override
    public String getEmailFromToken(String token) {
        return claims(token).getSubject();
    }

    private Claims claims(String token) {
        return Jwts.parser().verifyWith(signingKey()).build().parseSignedClaims(token).getPayload();
    }

    private SecretKey signingKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
