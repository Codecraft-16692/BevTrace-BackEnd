package com.codecraft.bevtrace.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

/**
 * Entry point of the BevTrace platform.
 *
 * <p>All timestamps are handled in UTC, so the JVM default time zone is fixed before
 * the Spring context starts. This keeps JPA, JDBC and JSON serialization consistent.</p>
 */
@SpringBootApplication
@ConfigurationPropertiesScan
@EnableJpaAuditing
@EnableScheduling
public class BevtracePlatformApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(BevtracePlatformApplication.class, args);
    }
}
