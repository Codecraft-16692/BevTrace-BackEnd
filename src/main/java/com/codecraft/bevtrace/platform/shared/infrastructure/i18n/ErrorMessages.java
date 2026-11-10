package com.codecraft.bevtrace.platform.shared.infrastructure.i18n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;

/**
 * Localized messages (English default, Spanish) of the shared error handling.
 * Kept in code so the application only needs the application*.properties files as resources.
 */
public final class ErrorMessages {

    private static final Map<String, String> ENGLISH = Map.of(
            "validation.field.prefix", "Field",
            "validation.request.failed", "Request validation failed",
            "validation.request.argument", "request-argument",
            "error.validation.message", "Validation failed: {0}",
            "error.business-rule.message", "A business rule prevents this operation: {0}",
            "error.not-found.message", "The {1} you asked for was not found",
            "error.conflict.message", "The operation conflicts with the current state of the {1}: {0}",
            "error.unexpected.message", "An unexpected error occurred",
            "error.generic.message", "The request could not be completed");

    private static final Map<String, String> SPANISH = Map.of(
            "validation.field.prefix", "Campo",
            "validation.request.failed", "La validación de la solicitud falló",
            "validation.request.argument", "argumento-de-solicitud",
            "error.validation.message", "Validación fallida: {0}",
            "error.business-rule.message", "Una regla de negocio impide esta operación: {0}",
            "error.not-found.message", "No se encontró el recurso solicitado ({1})",
            "error.conflict.message", "La operación entra en conflicto con el estado actual de {1}: {0}",
            "error.unexpected.message", "Ocurrió un error inesperado",
            "error.generic.message", "No se pudo completar la solicitud");

    private ErrorMessages() {
    }

    /**
     * Resolves a message for the locale, formatted with the arguments, or {@code null} when the key is unknown.
     */
    public static String resolve(String key, Locale locale, Object... args) {
        var bundle = "es".equals(locale.getLanguage()) ? SPANISH : ENGLISH;
        var template = bundle.get(key);
        return template == null ? null : MessageFormat.format(template, args);
    }
}
