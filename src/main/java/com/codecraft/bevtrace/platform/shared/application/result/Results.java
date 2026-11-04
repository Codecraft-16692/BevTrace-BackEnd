package com.codecraft.bevtrace.platform.shared.application.result;

import org.jspecify.annotations.NullMarked;

import java.util.function.Supplier;

/**
 * Runs a use case and converts the exceptions thrown by the domain into a {@link Result}.
 *
 * <p>This is the single place where domain failures become {@link ApplicationError}s, so command
 * services do not repeat a {@code try/catch} per handler (the redundancy of the reference project).
 * The convention for aggregates is:</p>
 * <ul>
 *   <li>{@link IllegalArgumentException}: an input value is invalid ({@code VALIDATION_ERROR}, 400).</li>
 *   <li>{@link IllegalStateException}: the operation breaks a business rule in the current state
 *       ({@code BUSINESS_RULE_VIOLATION}, 422). The message is the i18n key of the rule
 *       (e.g. {@code dispatch.errors.capacity-exceeded}) and travels in {@code details}.</li>
 * </ul>
 *
 * <p>Addition of BevTrace: this file does not exist in Qualitrack's shared package; every other
 * shared class is unchanged apart from the package name.</p>
 */
@NullMarked
public final class Results {

    private Results() {
    }

    /**
     * Executes the supplier and wraps its value, or the failure it raised, in a Result.
     *
     * @param useCase the use case body
     * @param <T>     the success type
     * @return success with the value, or failure with the translated error
     */
    public static <T> Result<T, ApplicationError> attempt(Supplier<T> useCase) {
        try {
            return Result.success(useCase.get());
        } catch (IllegalArgumentException ex) {
            return Result.failure(ApplicationError.validationError("request", String.valueOf(ex.getMessage())));
        } catch (IllegalStateException ex) {
            return Result.failure(ApplicationError.businessRuleViolation(
                    String.valueOf(ex.getMessage()), String.valueOf(ex.getMessage())));
        }
    }

    /**
     * Same as {@link #attempt(Supplier)} for use cases that return nothing.
     *
     * @param useCase the use case body
     * @return success with the id of the affected aggregate is not needed here, so {@code Boolean.TRUE}
     */
    public static Result<Boolean, ApplicationError> attemptVoid(Runnable useCase) {
        return attempt(() -> {
            useCase.run();
            return Boolean.TRUE;
        });
    }
}
