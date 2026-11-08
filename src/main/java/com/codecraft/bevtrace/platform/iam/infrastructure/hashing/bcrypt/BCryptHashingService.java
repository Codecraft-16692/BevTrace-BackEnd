package com.codecraft.bevtrace.platform.iam.infrastructure.hashing.bcrypt;

import com.codecraft.bevtrace.platform.iam.application.internal.outboundservices.hashing.HashingService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * BCrypt implementation of the hashing port.
 */
@Service
public class BCryptHashingService implements HashingService {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String encode(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
}
