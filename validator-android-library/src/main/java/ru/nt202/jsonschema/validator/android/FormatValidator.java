package ru.nt202.jsonschema.validator.android;

import java8.util.Optional;

/**
 * Implementations perform the validation against the "format" keyword (see JSON Schema spec section
 * 7).
 */
@FunctionalInterface
public interface FormatValidator {

    /**
     * No-operation implementation (never throws {always returns {@link Optional#empty()}).
     */
    FormatValidator NONE = subject -> Optional.empty();

    /**
     * Implementation-specific validation of {@code subject}. If a validation error occurs then
     * implementations should return a programmer-friendly error message as a String wrapped in an
     * Optional. If the validation succeeded then {@link Optional#empty() an empty optional} should be
     * returned.
     *
     * @param subject
     *         the string to be validated
     * @return an {@code Optional} wrapping the error message if a validation error occured, otherwise
     * {@link Optional#empty() an empty optional}.
     */
    Optional<String> validate(String subject);
}
