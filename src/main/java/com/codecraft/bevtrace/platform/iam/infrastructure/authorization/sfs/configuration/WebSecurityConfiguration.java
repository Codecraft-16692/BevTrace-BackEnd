package com.codecraft.bevtrace.platform.iam.infrastructure.authorization.sfs.configuration;

import com.codecraft.bevtrace.platform.iam.application.internal.outboundservices.tokens.TokenService;
import com.codecraft.bevtrace.platform.iam.infrastructure.authorization.sfs.pipeline.BearerAuthorizationRequestFilter;
import com.codecraft.bevtrace.platform.iam.infrastructure.authorization.sfs.pipeline.UnauthorizedRequestHandlerEntryPoint;
import com.codecraft.bevtrace.platform.iam.infrastructure.authorization.sfs.services.UserDetailsServiceImpl;
import com.codecraft.bevtrace.platform.iam.infrastructure.tokens.jwt.BearerTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Stateless JWT security of the API. Sign-in/up, the plan catalog, the public forms and the API docs are
 * open; everything else needs a valid token, and each endpoint refines access with {@code @PreAuthorize}.
 */
@Configuration
@EnableMethodSecurity
public class WebSecurityConfiguration {

    private final UserDetailsServiceImpl userDetailsService;
    private final UnauthorizedRequestHandlerEntryPoint unauthorizedEntryPoint;
    private final BearerTokenService bearerTokenService;
    private final TokenService tokenService;
    private final List<String> allowedOrigins;

    public WebSecurityConfiguration(UserDetailsServiceImpl userDetailsService,
                                    UnauthorizedRequestHandlerEntryPoint unauthorizedEntryPoint,
                                    BearerTokenService bearerTokenService, TokenService tokenService,
                                    @Value("${bevtrace.cors.allowed-origins}") List<String> allowedOrigins) {
        this.userDetailsService = userDetailsService;
        this.unauthorizedEntryPoint = unauthorizedEntryPoint;
        this.bearerTokenService = bearerTokenService;
        this.tokenService = tokenService;
        this.allowedOrigins = allowedOrigins;
    }

    @Bean
    public BearerAuthorizationRequestFilter bearerAuthorizationRequestFilter() {
        return new BearerAuthorizationRequestFilter(bearerTokenService, tokenService, userDetailsService);
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        var configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization"));
        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   BearerAuthorizationRequestFilter bearerFilter) throws Exception {
        http
                .csrf(CsrfConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedEntryPoint))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**",
                                "/actuator/health", "/api/v1/authentication/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/roles", "/api/v1/subscription-plans").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/contact-requests", "/api/v1/newsletter-subscribers").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(bearerFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
