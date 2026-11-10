package com.codecraft.bevtrace.platform.iam.infrastructure.authorization.sfs.pipeline;

import com.codecraft.bevtrace.platform.shared.interfaces.rest.resources.ErrorResource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Answers 403 to {@code @PreAuthorize} rejections. It runs before the shared handler, which would
 * otherwise report them as unexpected errors.
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ForbiddenRequestAdvice {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResource> handle(AccessDeniedException ex) {
        return new ResponseEntity<>(new ErrorResource("FORBIDDEN", "You do not have permission for this operation"),
                HttpStatus.FORBIDDEN);
    }
}
