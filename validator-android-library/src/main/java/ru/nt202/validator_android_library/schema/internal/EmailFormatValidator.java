package ru.nt202.validator.everit.json.schema.internal;

import java8.util.Optional;

import org.apache.commons.validator.routines.EmailValidator;

/**
 * Implementation of the "email" format value.
 */
public class EmailFormatValidator extends AFormatValidator {

    @Override
    public Optional<String> validate(final String subject) {
        if (EmailValidator.getInstance(false, true).isValid(subject)) {
            return Optional.empty();
        }
        return Optional.of(String.format("[%s] is not a valid email address", subject));
    }

    @Override
    public String formatName() {
        return "email";
    }
}
