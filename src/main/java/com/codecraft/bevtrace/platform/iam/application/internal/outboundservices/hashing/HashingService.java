package com.codecraft.bevtrace.platform.iam.application.internal.outboundservices.hashing;

/**
 * Port to the password hashing algorithm.
 */
public interface HashingService {

    String encode(String rawPassword);

    boolean matches(String rawPassword, String encodedPassword);
}
