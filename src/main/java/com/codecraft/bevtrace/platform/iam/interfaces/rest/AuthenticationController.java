package com.codecraft.bevtrace.platform.iam.interfaces.rest;

import com.codecraft.bevtrace.platform.iam.application.commandservices.UserCommandService;
import com.codecraft.bevtrace.platform.iam.interfaces.rest.resources.SignInResource;
import com.codecraft.bevtrace.platform.iam.interfaces.rest.resources.SignUpResource;
import com.codecraft.bevtrace.platform.iam.interfaces.rest.transform.AuthenticatedUserResourceFromEntityAssembler;
import com.codecraft.bevtrace.platform.iam.interfaces.rest.transform.UserCommandFromResourceAssembler;
import com.codecraft.bevtrace.platform.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;
import com.codecraft.bevtrace.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Sign-in and sign-up endpoints.
 */
@RestController
@RequestMapping(value = "/api/v1/authentication", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Authentication", description = "Authentication and user registration endpoints")
public class AuthenticationController {

    private final UserCommandService userCommandService;

    public AuthenticationController(UserCommandService userCommandService) {
        this.userCommandService = userCommandService;
    }

    @PostMapping("/sign-in")
    @Operation(summary = "Sign in", description = "Authenticates a user and returns a JWT to send as Bearer token.")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInResource resource) {
        var result = userCommandService.handle(UserCommandFromResourceAssembler.toCommandFromResource(resource));
        return ResponseEntityAssembler.toResponseEntityFromResult(result,
                AuthenticatedUserResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }

    @PostMapping("/sign-up")
    @Operation(summary = "Register a user", description = "Administrators cannot self-register.")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpResource resource) {
        var result = userCommandService.handle(UserCommandFromResourceAssembler.toCommandFromResource(resource));
        return ResponseEntityAssembler.toResponseEntityFromResult(result,
                UserResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }
}
